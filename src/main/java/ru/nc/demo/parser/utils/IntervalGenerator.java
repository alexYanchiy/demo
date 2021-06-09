package ru.nc.demo.parser.utils;

public class IntervalGenerator {
    private final long minTime;
    private final long maxTime;

    private IntervalGenerator(long minTime, long maxTime) {
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public long generateRandom() {
        return (long) ((Math.random() * (maxTime - minTime)) + minTime);
    }

    @Override
    public String toString() {
        return "IntervalGenerator{" +
                "minTime=" + minTime +
                ", maxTime=" + maxTime +
                '}';
    }

    public static long generateRandom(long minTime, long maxTime) {
        return (long) ((Math.random() * (maxTime - minTime)) + minTime);
    }

    public static IntervalGenerator of(long minTime, long maxTime) {
        return new IntervalGenerator(minTime, maxTime);
    }

}
