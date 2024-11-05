package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.locks.Lock;
import edu.vt.ece.hw4.utils.ThreadCluster;

import java.util.concurrent.atomic.AtomicInteger;

public class ClusterCSTestThread extends Thread implements ThreadId{

    private static AtomicInteger ID_GEN = new AtomicInteger();
    private Lock lock;
    private int id;
    private long elapsedTime;
    private int iter;
    private Counter counter;
    private int clusters;

    public ClusterCSTestThread(Lock lock, int iter, Counter counter, int clusters) {
        this.lock = lock;
        this.id = ID_GEN.getAndIncrement();
        this.iter = iter;
        this.counter = counter;
        this.clusters = clusters;
        ThreadCluster.set(id);
    }

    public static void reset(){
        ID_GEN.set(0);
    }

    public void run(){
        long start = System.currentTimeMillis();
        for(int i=0; i<iter; i++){
            lock.lock();
            try{
                counter.getAndIncrement();
            }finally{
                lock.unlock();
            }
        }
        elapsedTime = System.currentTimeMillis() - start;
    }


    @Override
    public int getThreadId() {
        return id;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
