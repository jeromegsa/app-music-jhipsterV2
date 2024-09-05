package bj.highfiveuniversity.domain;

import static bj.highfiveuniversity.domain.AlbumTestSamples.*;
import static bj.highfiveuniversity.domain.CategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bj.highfiveuniversity.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void albumTest() {
        Category category = getCategoryRandomSampleGenerator();
        Album albumBack = getAlbumRandomSampleGenerator();

        category.addAlbum(albumBack);
        assertThat(category.getAlbums()).containsOnly(albumBack);
        assertThat(albumBack.getAlbum_category()).isEqualTo(category);

        category.removeAlbum(albumBack);
        assertThat(category.getAlbums()).doesNotContain(albumBack);
        assertThat(albumBack.getAlbum_category()).isNull();

        category.albums(new HashSet<>(Set.of(albumBack)));
        assertThat(category.getAlbums()).containsOnly(albumBack);
        assertThat(albumBack.getAlbum_category()).isEqualTo(category);

        category.setAlbums(new HashSet<>());
        assertThat(category.getAlbums()).doesNotContain(albumBack);
        assertThat(albumBack.getAlbum_category()).isNull();
    }
}
