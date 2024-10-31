package edu.vt.ece.hw4.backoff;

public class FixedBackoff implements Backoff {
    @Override
    public void backoff() throws InterruptedException {
        Thread.sleep(1000);
    }
}
