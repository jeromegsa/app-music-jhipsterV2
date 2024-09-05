package bj.highfiveuniversity.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlbumTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Album getAlbumSample1() {
        return new Album()
            .id(1L)
            .name("name1")
            .tags("tags1")
            .description("description1")
            .nbr_music(1)
            .author("author1")
            .image_url("image_url1");
    }

    public static Album getAlbumSample2() {
        return new Album()
            .id(2L)
            .name("name2")
            .tags("tags2")
            .description("description2")
            .nbr_music(2)
            .author("author2")
            .image_url("image_url2");
    }

    public static Album getAlbumRandomSampleGenerator() {
        return new Album()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .tags(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .nbr_music(intCount.incrementAndGet())
            .author(UUID.randomUUID().toString())
            .image_url(UUID.randomUUID().toString());
    }
}
