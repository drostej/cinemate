package de.cinebuddy.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VorfuehrungTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vorfuehrung getVorfuehrungSample1() {
        return new Vorfuehrung().id(1L).filmTitel("filmTitel1");
    }

    public static Vorfuehrung getVorfuehrungSample2() {
        return new Vorfuehrung().id(2L).filmTitel("filmTitel2");
    }

    public static Vorfuehrung getVorfuehrungRandomSampleGenerator() {
        return new Vorfuehrung().id(longCount.incrementAndGet()).filmTitel(UUID.randomUUID().toString());
    }
}
