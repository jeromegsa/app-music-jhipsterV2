package bj.highfiveuniversity.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AlbumCriteriaTest {

    @Test
    void newAlbumCriteriaHasAllFiltersNullTest() {
        var albumCriteria = new AlbumCriteria();
        assertThat(albumCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void albumCriteriaFluentMethodsCreatesFiltersTest() {
        var albumCriteria = new AlbumCriteria();

        setAllFilters(albumCriteria);

        assertThat(albumCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void albumCriteriaCopyCreatesNullFilterTest() {
        var albumCriteria = new AlbumCriteria();
        var copy = albumCriteria.copy();

        assertThat(albumCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(albumCriteria)
        );
    }

    @Test
    void albumCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var albumCriteria = new AlbumCriteria();
        setAllFilters(albumCriteria);

        var copy = albumCriteria.copy();

        assertThat(albumCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(albumCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var albumCriteria = new AlbumCriteria();

        assertThat(albumCriteria).hasToString("AlbumCriteria{}");
    }

    private static void setAllFilters(AlbumCriteria albumCriteria) {
        albumCriteria.id();
        albumCriteria.name();
        albumCriteria.tags();
        albumCriteria.description();
        albumCriteria.nbr_music();
        albumCriteria.author();
        albumCriteria.image_url();
        albumCriteria.album_categoryId();
        albumCriteria.album_musicId();
        albumCriteria.distinct();
    }

    private static Condition<AlbumCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getTags()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getNbr_music()) &&
                condition.apply(criteria.getAuthor()) &&
                condition.apply(criteria.getImage_url()) &&
                condition.apply(criteria.getAlbum_categoryId()) &&
                condition.apply(criteria.getAlbum_musicId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AlbumCriteria> copyFiltersAre(AlbumCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getTags(), copy.getTags()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getNbr_music(), copy.getNbr_music()) &&
                condition.apply(criteria.getAuthor(), copy.getAuthor()) &&
                condition.apply(criteria.getImage_url(), copy.getImage_url()) &&
                condition.apply(criteria.getAlbum_categoryId(), copy.getAlbum_categoryId()) &&
                condition.apply(criteria.getAlbum_musicId(), copy.getAlbum_musicId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
