package edu.vt.ece.hw4.backoff;

public class BackoffFactory {
    public static Backoff getBackoff(String name) {
        switch(name) {
            case "Exponential":
                return new ExponentialBackoff();
            case "Fibonacci":
                return new FibonacciBackoff();
            case "Linear" :
                return new LinearBackoff();
            case "Polynomial":
                return new PolynomialBackoff();
            case "Fixed":
                return new FixedBackoff();
        }
        return null;
    }
}
