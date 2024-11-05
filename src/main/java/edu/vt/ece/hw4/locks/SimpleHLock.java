package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleHLock implements Lock {

    private final Lock globalLock;
    private final Lock[] localLocks;
    private final int clusters;
    private final int BATCH_COUNT;
    private final AtomicInteger[] localCount; // number of threads waiting per cluster
    private final AtomicBoolean[] globalLockHeld;

    public SimpleHLock(int clusters, int batchCount) {
        this.globalLock = new TTASLock();
        this.localLocks = new Lock[clusters];
        this.clusters = clusters;
        this.BATCH_COUNT = batchCount;
        this.localCount = new AtomicInteger[clusters];
        this.globalLockHeld = new AtomicBoolean[clusters];
        for(int i = 0; i < clusters; i++) {
            localLocks[i] = new TTASLock();
            localCount[i] = new AtomicInteger(0);
            globalLockHeld[i] = new AtomicBoolean(false);
        }
    }

    @Override
    public void lock() {
        int cluster = ThreadCluster.getCluster() % clusters;
        int count = localCount[cluster].getAndIncrement();

        if (count == 0) {
            globalLock.lock();
            globalLockHeld[cluster].set(true);
        }

        localLocks[cluster].lock();
    }

    @Override
    public void unlock() {
        int cluster = ThreadCluster.getCluster() % clusters;
        localLocks[cluster].unlock();

        int remainingCount = localCount[cluster].decrementAndGet();

        if (remainingCount == 0 || remainingCount % BATCH_COUNT == 0) {
            if (globalLockHeld[cluster].compareAndSet(true, false)) {
                globalLock.unlock();
            }
        }
    }
}