package edu.vt.ece.hw4.backoff;

public class PolynomialBackoff implements Backoff {

    private int attempts=0;
    private int n=2;

    @Override
    public void backoff() throws InterruptedException {
        attempts++;
        int delay= attempts^3;

        Thread.sleep(delay);
    }
}
