/*
 * MCSLock.java
 *
 * Created on January 20, 2006, 11:41 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements Lock {
    AtomicReference<QNode> queue;
    ThreadLocal<QNode> myNode;

    public MCSLock() {
        queue = new AtomicReference<>(null);
        // initialize thread-local variable
        myNode = ThreadLocal.withInitial(() -> new QNode());
    }

    @Override
    public void lock() {
        QNode qnode = myNode.get();
        qnode.next = null; // Reset the next pointer to avoid stale references
        QNode pred = queue.getAndSet(qnode);

        if (pred != null) {
            qnode.locked = true;
            pred.next = qnode;
            while (qnode.locked) {
                // spin until predecessor signals
            }
        }
    }

    @Override
    public void unlock() {
        QNode qnode = myNode.get();

        if (qnode.next == null) { // Check if there is a successor
            if (queue.compareAndSet(qnode, null)) {
                return; // No successor, safely release lock
            }
            while (qnode.next == null) {
                // spin-wait until a successor appears
            }
        }

        qnode.next.locked = false; // Release the lock for the successor
        qnode.next = null; // Avoid memory leak by helping GC
        qnode.locked = false; // Reset locked status for reuse
    }

    static class QNode {     // Queue node inner class
        volatile boolean locked = false;
        volatile QNode next = null;
    }
}
