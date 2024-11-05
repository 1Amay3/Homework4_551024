package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleHLock implements Lock {

    private final Lock globalLock;
    private final Lock[] localLocks;
    private final int clusters;
    private final int BATCH_COUNT;
    private final AtomicInteger[] localCount;

    public SimpleHLock(int clusters, int batchCount) {
        this.globalLock = new TTASLock();
        this.localLocks = new Lock[clusters];
        this.clusters = clusters;
        this.BATCH_COUNT = batchCount;
        this.localCount = new AtomicInteger[clusters];
        for(int i = 0; i < clusters; i++) {
            localLocks[i] = new TTASLock();
            localCount[i] = new AtomicInteger(0);
        }
    }

    @Override
    public void lock() {
        int cluster = ThreadCluster.getCluster() % clusters;
        boolean acquiredGlobal = false;

        localCount[cluster].incrementAndGet();

        if (localCount[cluster].get() == 1) {
            globalLock.lock();
            acquiredGlobal = true;
        }

        localLocks[cluster].lock();

        if (acquiredGlobal) {
            globalLock.unlock();
        }
    }

    @Override
    public void unlock() {
        int cluster = ThreadCluster.getCluster() % clusters;

        localLocks[cluster].unlock();

        int remaining = localCount[cluster].decrementAndGet();

        if (remaining == 0 || remaining % BATCH_COUNT == 0) {
            globalLock.lock();
            try {

                if (localCount[cluster].get() == 0 || localCount[cluster].get() % BATCH_COUNT == 0) {

                }
            } finally {
                globalLock.unlock();
            }
        }
    }
}