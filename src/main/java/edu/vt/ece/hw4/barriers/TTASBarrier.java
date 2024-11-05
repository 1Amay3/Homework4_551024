package edu.vt.ece.hw4.barriers;

import edu.vt.ece.hw4.locks.Lock;
import edu.vt.ece.hw4.locks.TTASLock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TTASBarrier implements Barrier {
    private Lock lock;
    private int n = 0;
    private AtomicInteger count;
    public TTASBarrier(int n) {
        this.n = n;
        this.lock = new TTASLock();
        this.count = new AtomicInteger(0);
    }

    @Override
    public void enter() {
        System.out.println("Entering " + count +" / " + n + "Thread :" + Thread.currentThread().getId());
        lock.lock();
        count.incrementAndGet();
        lock.unlock();

        while(count.get() <n){
            Thread.yield();
        }
    }
}
