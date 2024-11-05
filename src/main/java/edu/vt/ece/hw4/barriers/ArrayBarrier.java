package edu.vt.ece.hw4.barriers;

import edu.vt.ece.spin.ThreadID;

import java.util.concurrent.atomic.AtomicInteger;

public class ArrayBarrier implements Barrier {
    private final AtomicInteger[] b;
    private final int n;

    public ArrayBarrier(int n) {
        this.b = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            this.b[i] = new AtomicInteger(0);
        }
        this.n = n;
    }

    @Override
    public void enter() {
        int threadId = ThreadID.get() % n;

        if (threadId == 0) {
            b[0].set(1);
        } else {
            while (b[(threadId - 1 + n) % n].get() != 1) {
                Thread.yield();
            }
            b[threadId].set(1);
        }

        if (threadId == n - 1) {
            b[n - 1].set(2);
        } else {
            while (b[n - 1].get() != 2) {
                Thread.yield();
            }
        }
    }
}