package de.cinebuddy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SitzplatzTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Sitzplatz getSitzplatzSample1() {
        return new Sitzplatz().id(1L).reihe("reihe1").nummer(1);
    }

    public static Sitzplatz getSitzplatzSample2() {
        return new Sitzplatz().id(2L).reihe("reihe2").nummer(2);
    }

    public static Sitzplatz getSitzplatzRandomSampleGenerator() {
        return new Sitzplatz().id(longCount.incrementAndGet()).reihe(UUID.randomUUID().toString()).nummer(intCount.incrementAndGet());
    }
}
