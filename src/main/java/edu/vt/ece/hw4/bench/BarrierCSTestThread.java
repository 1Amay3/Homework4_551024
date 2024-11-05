package edu.vt.ece.hw4.bench;

import edu.vt.ece.hw4.barriers.Barrier;
import edu.vt.ece.hw4.barriers.TTASBarrier;
import edu.vt.ece.hw4.locks.Lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierCSTestThread extends Thread implements ThreadId{
    private static volatile AtomicInteger ID_GEN = new AtomicInteger(0);
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
        this.elapsed = 0;
    }
    public static void reset() {
        ID_GEN.set(0);
    }

    @Override
    public void run() {

        for (int i = 0; i < iter; i++) {

            foo();
            long start = System.currentTimeMillis();
            barrier.enter();
            elapsed += System.currentTimeMillis() - start;
            bar();

        }


    }

    private void foo(){
        lock.lock();
        try {
            counter.getAndIncrement();
        } finally {
            lock.unlock();
        }
    }

    private void bar(){
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getThreadId() {
        return id;
    }

    public long getElapsed() {
        return elapsed;
    }
}
