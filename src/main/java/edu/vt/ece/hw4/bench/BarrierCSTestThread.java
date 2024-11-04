package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;

public class BarrierCSTestThread extends Thread implements ThreadId{
    private static int ID_GEN = 0;
    private Lock lock;
    private int id;
    private long elapsed;
    private int iter;
    private Counter counter;
    private TTASBarrier barrier;

    public BarrierCSTestThread(Lock lock,Counter counter, int iter, TTASBarrier barrier) {
        id = ID_GEN++;
        this.lock =lock;
        this.iter = iter;
        this.barrier = barrier;
        this.counter = counter;
    }
    public static void reset() {
        ID_GEN = 0;
    }

    public void run() {
        barrier.enter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {

            lock.lock();
            try {
                counter.getAndIncrement();
            }finally {
                lock.unlock();
            }

        }
        elapsed = System.currentTimeMillis() - start;
    }

    @Override
    public int getThreadId() {
        return id;
    }

    public long getElapsed() {
        return elapsed;
    }
}
