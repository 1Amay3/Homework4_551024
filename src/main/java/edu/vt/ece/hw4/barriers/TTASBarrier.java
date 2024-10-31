package edu.vt.ece.hw4.barriers;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TTASBarrier implements Barrier {
    private AtomicBoolean state = new AtomicBoolean(false);
    private int n = 0;
    private AtomicInteger count = new AtomicInteger(0);

    public TTASBarrier(int n) {
        this.n = n;
    }
    public void enter() {
        while(true){
            while(state.get()){
                if(!state.getAndSet(true)){
                    count.getAndIncrement();
                    if(count.get() ==n){
                        count.set(0);
                        state.set(false);
                        return;
                    }
                    state.set(false);
                    while(count.get()<n){}
                    return;
                }
            }
        }
    }
}
