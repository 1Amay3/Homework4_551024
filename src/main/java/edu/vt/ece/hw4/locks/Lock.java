package edu.vt.ece.hw4.locks;

public interface Lock {
    void lock();
    void unlock();

    boolean trylock(int priority, long timeoutMillis) throws InterruptedException;
}
