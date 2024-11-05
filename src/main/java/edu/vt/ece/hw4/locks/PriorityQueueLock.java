package edu.vt.ece.hw4.locks;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class PriorityQueueLock {
    private final PriorityBlockingQueue<PriorityNode> queue;
    private final AtomicBoolean locked = new AtomicBoolean(false);

    public PriorityQueueLock() {

        queue = new PriorityBlockingQueue<>();
    }

    private static class PriorityNode implements Comparable<PriorityNode> {
        final Thread thread;
        final int priority;
        final long enqueueTime;

        public PriorityNode(Thread thread, int priority) {
            this.thread = thread;
            this.priority = priority;
            this.enqueueTime = System.nanoTime();
        }

        @Override
        public int compareTo(PriorityNode other) {
            int priorityCompare = Integer.compare(this.priority, other.priority);
            if (priorityCompare == 0) {
                return Long.compare(this.enqueueTime, other.enqueueTime); // Tie-break by enqueue time
            }
            return priorityCompare;
        }
    }


    public void lock() {
        try {
            lock(5); // Default priority if none is specified
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void lock(int priority) throws InterruptedException {
        PriorityNode node = new PriorityNode(Thread.currentThread(), priority);
        queue.put(node);


        while (true) {
            PriorityNode head = queue.peek();
            if (head == node && locked.compareAndSet(false, true)) {
                queue.remove(node);
                return;
            } else {
                Thread.onSpinWait();
            }
        }
    }

    public void unlock() {
        locked.set(false);
        synchronized (this) {
            notifyAll();
        }
    }
}
