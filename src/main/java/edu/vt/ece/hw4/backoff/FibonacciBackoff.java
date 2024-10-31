package edu.vt.ece.hw4.backoff;

public class FibonacciBackoff implements Backoff {
    private int max = 1000;
    private int prev =0;
    private int curr =1;
    @Override
    public void backoff() throws InterruptedException {
        int backOffTime= Math.min(max,curr);
        Thread.sleep(backOffTime);
        int next = curr+prev;
        prev = curr;
        curr=next;
    }
}
