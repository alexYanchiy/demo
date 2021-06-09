package ru.nc.demo.parser.utils;

public class ThreadUtils {

    public static long calculateTimeToSleep(long lastRequestTime, IntervalGenerator intervalGenerator) {
        return calculateTimeToSleep(lastRequestTime, intervalGenerator.generateRandom());
    }

    public static long calculateTimeToSleep(long lastRequestTime, long maxTimeToSleep) {
        return lastRequestTime + maxTimeToSleep - System.currentTimeMillis();
    }

    public static void calculateTimeAndSleep(long lastRequestTime, IntervalGenerator intervalGenerator) {
        sleep(calculateTimeToSleep(lastRequestTime, intervalGenerator.generateRandom()));
    }

    public static void calculateTimeAndSleep(long lastRequestTime, long maxTimeToSleep) {
        sleep(lastRequestTime + maxTimeToSleep - System.currentTimeMillis());
    }

    public static void sleep(long sleepTime) {
        if (sleepTime <= 0) {
            return;
        }

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException iex) {
            /* NOP */
        }
    }

}
