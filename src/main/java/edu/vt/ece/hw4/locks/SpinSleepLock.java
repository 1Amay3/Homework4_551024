package edu.vt.ece.hw4.locks;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinSleepLock implements Lock {
    private AtomicBoolean locked = new AtomicBoolean(false);
    private AtomicInteger Spinning = new AtomicInteger(0);
    private AtomicInteger threadLimit = new AtomicInteger(0);

    public SpinSleepLock(int maxSpin) {
        this.threadLimit.set(maxSpin);
    }

    @Override
    public void lock() {
        if(Spinning.incrementAndGet()<= threadLimit.get()) {
            while(true){

                if(locked.compareAndSet(false,true)){
                    Spinning.decrementAndGet();
                    return;}}
        } else{
            Spinning.decrementAndGet();
            synchronized(this){
                while(true){
                    if(locked.compareAndSet(false,true)){
                        return;
                    } try{
                        wait();
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }}
            }
        }
    }

    @Override
    public void unlock() {
        locked.set(false);
        synchronized(this){
            notifyAll();
        }
    }
}