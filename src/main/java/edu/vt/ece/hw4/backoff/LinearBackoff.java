package edu.vt.ece.hw4.backoff;

public class LinearBackoff implements Backoff {
    private int attempts=0;
    @Override

    public void backoff() throws InterruptedException {
        Thread.sleep(attempts);
        attempts++;
    }
}
