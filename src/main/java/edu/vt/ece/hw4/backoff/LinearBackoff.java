package edu.vt.ece.hw4.backoff;

public class LinearBackoff implements Backoff {
    private int baseDelay=1000;
    private int attempts=0;
    private final int m=3;
    @Override

    public void backoff() throws InterruptedException {
        attempts++;
        Thread.sleep(m*attempts + baseDelay);
    }
}
