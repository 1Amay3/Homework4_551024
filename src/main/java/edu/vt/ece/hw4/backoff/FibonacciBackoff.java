package edu.vt.ece.hw4.backoff;

public class FibonacciBackoff implements Backoff {
    private int max = 1000;
    private int x =0;
    private int y =1;
    @Override
    public void backoff() throws InterruptedException {
        int backOffTime= Math.min(max, y);
        Thread.sleep(backOffTime);
        int z = y + x;
        x = y;
        y = z;
    }
}
