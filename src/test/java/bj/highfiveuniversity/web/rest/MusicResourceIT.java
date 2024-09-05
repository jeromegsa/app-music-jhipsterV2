package bj.highfiveuniversity.web.rest;

import static bj.highfiveuniversity.domain.MusicAsserts.*;
import static bj.highfiveuniversity.web.rest.TestUtil.createUpdateProxyForBean;
import static bj.highfiveuniversity.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bj.highfiveuniversity.IntegrationTest;
import bj.highfiveuniversity.domain.Music;
import bj.highfiveuniversity.repository.MusicRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MusicResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MusicResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_PAROLES = "AAAAAAAAAA";
    private static final String UPDATED_PAROLES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/music";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMusicMockMvc;

    private Music music;

    private Music insertedMusic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createEntity() {
        return new Music()
            .title(DEFAULT_TITLE)
            .duration(DEFAULT_DURATION)
            .paroles(DEFAULT_PAROLES)
            .created_At(DEFAULT_CREATED_AT)
            .updated_At(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Music createUpdatedEntity() {
        return new Music()
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .paroles(UPDATED_PAROLES)
            .created_At(UPDATED_CREATED_AT)
            .updated_At(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    public void initTest() {
        music = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedMusic != null) {
            musicRepository.delete(insertedMusic);
            insertedMusic = null;
        }
    }

    @Test
    @Transactional
    void createMusic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Music
        var returnedMusic = om.readValue(
            restMusicMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(music)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Music.class
        );

        // Validate the Music in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMusicUpdatableFieldsEquals(returnedMusic, getPersistedMusic(returnedMusic));

        insertedMusic = returnedMusic;
    }

    @Test
    @Transactional
    void createMusicWithExistingId() throws Exception {
        // Create the Music with an existing ID
        music.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMusicMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(music)))
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMusic() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        // Get all the musicList
        restMusicMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(music.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].paroles").value(hasItem(DEFAULT_PAROLES)))
            .andExpect(jsonPath("$.[*].created_At").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updated_At").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getMusic() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        // Get the music
        restMusicMockMvc
            .perform(get(ENTITY_API_URL_ID, music.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(music.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.paroles").value(DEFAULT_PAROLES))
            .andExpect(jsonPath("$.created_At").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updated_At").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMusic() throws Exception {
        // Get the music
        restMusicMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMusic() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the music
        Music updatedMusic = musicRepository.findById(music.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMusic are not directly saved in db
        em.detach(updatedMusic);
        updatedMusic
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .paroles(UPDATED_PAROLES)
            .created_At(UPDATED_CREATED_AT)
            .updated_At(UPDATED_UPDATED_AT);

        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMusic.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMusicToMatchAllProperties(updatedMusic);
    }

    @Test
    @Transactional
    void putNonExistingMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(put(ENTITY_API_URL_ID, music.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(music)))
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(music)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic.paroles(UPDATED_PAROLES).created_At(UPDATED_CREATED_AT).updated_At(UPDATED_UPDATED_AT);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMusicUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMusic, music), getPersistedMusic(music));
    }

    @Test
    @Transactional
    void fullUpdateMusicWithPatch() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the music using partial update
        Music partialUpdatedMusic = new Music();
        partialUpdatedMusic.setId(music.getId());

        partialUpdatedMusic
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .paroles(UPDATED_PAROLES)
            .created_At(UPDATED_CREATED_AT)
            .updated_At(UPDATED_UPDATED_AT);

        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMusic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMusic))
            )
            .andExpect(status().isOk());

        // Validate the Music in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMusicUpdatableFieldsEquals(partialUpdatedMusic, getPersistedMusic(partialUpdatedMusic));
    }

    @Test
    @Transactional
    void patchNonExistingMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, music.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(music))
            )
            .andExpect(status().isBadRequest());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMusic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        music.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMusicMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(music)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Music in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMusic() throws Exception {
        // Initialize the database
        insertedMusic = musicRepository.saveAndFlush(music);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the music
        restMusicMockMvc
            .perform(delete(ENTITY_API_URL_ID, music.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return musicRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Music getPersistedMusic(Music music) {
        return musicRepository.findById(music.getId()).orElseThrow();
    }

    protected void assertPersistedMusicToMatchAllProperties(Music expectedMusic) {
        assertMusicAllPropertiesEquals(expectedMusic, getPersistedMusic(expectedMusic));
    }

    protected void assertPersistedMusicToMatchUpdatableProperties(Music expectedMusic) {
        assertMusicAllUpdatablePropertiesEquals(expectedMusic, getPersistedMusic(expectedMusic));
    }
}
