package bj.highfiveuniversity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MusicTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Music getMusicSample1() {
        return new Music().id(1L).title("title1").duration(1).paroles("paroles1");
    }

    public static Music getMusicSample2() {
        return new Music().id(2L).title("title2").duration(2).paroles("paroles2");
    }

    public static Music getMusicRandomSampleGenerator() {
        return new Music()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .duration(intCount.incrementAndGet())
            .paroles(UUID.randomUUID().toString());
    }
}
