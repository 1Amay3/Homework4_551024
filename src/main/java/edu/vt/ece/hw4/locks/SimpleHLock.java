

package edu.vt.ece.hw4.locks;

import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleHLock implements Lock {

    private final Lock globalLock;
    private final Lock localLocks[];
    private final int clusters;
    private final int BATCH_COUNT;
    private final AtomicInteger localCount[]; //number of threads waiting per cluster

    public SimpleHLock(int clusters,int batchCount) {
        this.globalLock = new TTASLock();
        this.localLocks = new Lock[clusters];
        for(int i = 0; i < clusters; i++) {
            localLocks[i] = new TTASLock();
        }
        this.clusters = clusters;
        this.BATCH_COUNT = batchCount;
        this.localCount = new AtomicInteger[clusters];
    }

    @Override
    public void lock() {
        int cluster = ThreadCluster.getCluster() % clusters;
        localCount[cluster].incrementAndGet();
        localLocks[cluster].lock();
        localCount[cluster].decrementAndGet();

        if(localCount[cluster].get() == 0){
            globalLock.lock();
        }

    }

    @Override
    public void unlock() {
        int cluster = ThreadCluster.getCluster();
        if(localCount[cluster].get() ==0 || localCount[cluster].get() % BATCH_COUNT == 0){
            globalLock.unlock();
        }
        localLocks[cluster].unlock();
    }
}