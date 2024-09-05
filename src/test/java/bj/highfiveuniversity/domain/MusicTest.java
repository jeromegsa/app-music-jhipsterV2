package bj.highfiveuniversity.domain;

import static bj.highfiveuniversity.domain.AlbumTestSamples.*;
import static bj.highfiveuniversity.domain.MusicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import bj.highfiveuniversity.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MusicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Music.class);
        Music music1 = getMusicSample1();
        Music music2 = new Music();
        assertThat(music1).isNotEqualTo(music2);

        music2.setId(music1.getId());
        assertThat(music1).isEqualTo(music2);

        music2 = getMusicSample2();
        assertThat(music1).isNotEqualTo(music2);
    }

    @Test
    void albumTest() {
        Music music = getMusicRandomSampleGenerator();
        Album albumBack = getAlbumRandomSampleGenerator();

        music.setAlbum(albumBack);
        assertThat(music.getAlbum()).isEqualTo(albumBack);

        music.album(null);
        assertThat(music.getAlbum()).isNull();
    }
}
