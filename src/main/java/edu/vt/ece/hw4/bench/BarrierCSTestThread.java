package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.locks.Lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierCSTestThread extends Thread implements ThreadId{
    private static AtomicInteger ID_GEN = new AtomicInteger(0);
    private Lock lock;
    private int id;
    private long elapsed;
    private int iter;
    private Counter counter;
    private Barrier barrier;

    public BarrierCSTestThread(Lock lock,Counter counter, int iter, Barrier barrier) {
        id = ID_GEN.getAndIncrement();
        this.lock =lock;
        this.iter = iter;
        this.barrier = barrier;
        this.counter = counter;
    }
    public static void reset() {
        ID_GEN.set(0);
    }

    public void run() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < iter; i++) {

            lock.lock();
            try {
                counter.getAndIncrement();
            }finally {
                lock.unlock();
            }
            barrier.enter();
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
