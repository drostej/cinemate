package de.cinebuddy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReservierungTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reservierung getReservierungSample1() {
        return new Reservierung().id(1L).kundeName("kundeName1");
    }

    public static Reservierung getReservierungSample2() {
        return new Reservierung().id(2L).kundeName("kundeName2");
    }

    public static Reservierung getReservierungRandomSampleGenerator() {
        return new Reservierung().id(longCount.incrementAndGet()).kundeName(UUID.randomUUID().toString());
    }
}
