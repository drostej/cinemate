package de.cinebuddy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SaalTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Saal getSaalSample1() {
        return new Saal().id(1L).name("name1").kapazitaet(1);
    }

    public static Saal getSaalSample2() {
        return new Saal().id(2L).name("name2").kapazitaet(2);
    }

    public static Saal getSaalRandomSampleGenerator() {
        return new Saal().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).kapazitaet(intCount.incrementAndGet());
    }
}
