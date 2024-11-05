package edu.vt.ece.hw4.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCluster {
    private static final AtomicInteger nextID = new AtomicInteger(0);
    private static final ThreadLocal<Integer> threadID = ThreadLocal.withInitial(nextID::getAndIncrement);
    private static int clusters = 2; // Default number of clusters

    public static int get() {
        return threadID.get();
    }

    public static void reset() {
        nextID.set(0);
        threadID.remove();
    }

    public static void set(int value) {
        threadID.set(value);
    }

    public static int getCluster() {
        return threadID.get() % clusters;
    }

    public static void setNumClusters(int numClusters) {
        if (clusters > 0) {
            clusters = clusters;
        }
    }

    public static int getNumClusters() {
        return clusters;
    }
}