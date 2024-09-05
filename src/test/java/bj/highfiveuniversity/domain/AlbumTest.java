package bj.highfiveuniversity.domain;

import static bj.highfiveuniversity.domain.AlbumTestSamples.*;
import static bj.highfiveuniversity.domain.CategoryTestSamples.*;
import static bj.highfiveuniversity.domain.MusicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bj.highfiveuniversity.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlbumTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Album.class);
        Album album1 = getAlbumSample1();
        Album album2 = new Album();
        assertThat(album1).isNotEqualTo(album2);

        album2.setId(album1.getId());
        assertThat(album1).isEqualTo(album2);

        album2 = getAlbumSample2();
        assertThat(album1).isNotEqualTo(album2);
    }

    @Test
    void album_categoryTest() {
        Album album = getAlbumRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        album.setAlbum_category(categoryBack);
        assertThat(album.getAlbum_category()).isEqualTo(categoryBack);

        album.album_category(null);
        assertThat(album.getAlbum_category()).isNull();
    }

    @Test
    void album_musicTest() {
        Album album = getAlbumRandomSampleGenerator();
        Music musicBack = getMusicRandomSampleGenerator();

        album.addAlbum_music(musicBack);
        assertThat(album.getAlbum_musics()).containsOnly(musicBack);
        assertThat(musicBack.getAlbum()).isEqualTo(album);

        album.removeAlbum_music(musicBack);
        assertThat(album.getAlbum_musics()).doesNotContain(musicBack);
        assertThat(musicBack.getAlbum()).isNull();

        album.album_musics(new HashSet<>(Set.of(musicBack)));
        assertThat(album.getAlbum_musics()).containsOnly(musicBack);
        assertThat(musicBack.getAlbum()).isEqualTo(album);

        album.setAlbum_musics(new HashSet<>());
        assertThat(album.getAlbum_musics()).doesNotContain(musicBack);
        assertThat(musicBack.getAlbum()).isNull();
    }
}
