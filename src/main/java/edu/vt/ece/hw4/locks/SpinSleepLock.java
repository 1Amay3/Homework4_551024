package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private AtomicBoolean locked = new AtomicBoolean(false);
    private AtomicInteger SpinningThreads = new AtomicInteger(0);
    private AtomicInteger maxSpin = new AtomicInteger(0);

    public SpinSleepLock(int maxSpin) {
        this.maxSpin.set(maxSpin);
    }

    @Override
    public void lock() {
        if (SpinningThreads.get() < maxSpin.get()) {
            while (true) {
                if (!locked.getAndSet(true)) {
                    SpinningThreads.decrementAndGet();
                }
                if (SpinningThreads.get() > maxSpin.get()) {
                    SpinningThreads.decrementAndGet();
                    break;
                }
            }

            while (true) {
                synchronized (this) {
                    if (!locked.get()) {
                        locked.set(true);
                        return;
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }


    }

    @Override
    public void unlock() {
        locked.set(false);
        synchronized (this) {
            notify();
        }
    }
}