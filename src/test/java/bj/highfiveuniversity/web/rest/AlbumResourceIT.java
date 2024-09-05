package bj.highfiveuniversity.web.rest;

import static bj.highfiveuniversity.domain.AlbumAsserts.*;
import static bj.highfiveuniversity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bj.highfiveuniversity.IntegrationTest;
import bj.highfiveuniversity.domain.Album;
import bj.highfiveuniversity.domain.Category;
import bj.highfiveuniversity.repository.AlbumRepository;
import bj.highfiveuniversity.service.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlbumResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlbumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBR_MUSIC = 1;
    private static final Integer UPDATED_NBR_MUSIC = 2;
    private static final Integer SMALLER_NBR_MUSIC = 1 - 1;

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/albums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlbumRepository albumRepository;

    @Mock
    private AlbumRepository albumRepositoryMock;

    @Mock
    private AlbumService albumServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlbumMockMvc;

    private Album album;

    private Album insertedAlbum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createEntity() {
        return new Album()
            .name(DEFAULT_NAME)
            .tags(DEFAULT_TAGS)
            .description(DEFAULT_DESCRIPTION)
            .nbr_music(DEFAULT_NBR_MUSIC)
            .author(DEFAULT_AUTHOR)
            .image_url(DEFAULT_IMAGE_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Album createUpdatedEntity() {
        return new Album()
            .name(UPDATED_NAME)
            .tags(UPDATED_TAGS)
            .description(UPDATED_DESCRIPTION)
            .nbr_music(UPDATED_NBR_MUSIC)
            .author(UPDATED_AUTHOR)
            .image_url(UPDATED_IMAGE_URL);
    }

    @BeforeEach
    public void initTest() {
        album = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlbum != null) {
            albumRepository.delete(insertedAlbum);
            insertedAlbum = null;
        }
    }

    @Test
    @Transactional
    void createAlbum() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Album
        var returnedAlbum = om.readValue(
            restAlbumMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(album)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Album.class
        );

        // Validate the Album in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlbumUpdatableFieldsEquals(returnedAlbum, getPersistedAlbum(returnedAlbum));

        insertedAlbum = returnedAlbum;
    }

    @Test
    @Transactional
    void createAlbumWithExistingId() throws Exception {
        // Create the Album with an existing ID
        album.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlbumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(album)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlbums() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nbr_music").value(hasItem(DEFAULT_NBR_MUSIC)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlbumsWithEagerRelationshipsIsEnabled() throws Exception {
        when(albumServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlbumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(albumServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlbumsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(albumServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlbumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(albumRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlbum() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get the album
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL_ID, album.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(album.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.nbr_music").value(DEFAULT_NBR_MUSIC))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.image_url").value(DEFAULT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getAlbumsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        Long id = album.getId();

        defaultAlbumFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlbumFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlbumFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlbumsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where name equals to
        defaultAlbumFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlbumsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where name in
        defaultAlbumFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlbumsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where name is not null
        defaultAlbumFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where name contains
        defaultAlbumFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlbumsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where name does not contain
        defaultAlbumFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlbumsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where tags equals to
        defaultAlbumFiltering("tags.equals=" + DEFAULT_TAGS, "tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllAlbumsByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where tags in
        defaultAlbumFiltering("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS, "tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllAlbumsByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where tags is not null
        defaultAlbumFiltering("tags.specified=true", "tags.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByTagsContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where tags contains
        defaultAlbumFiltering("tags.contains=" + DEFAULT_TAGS, "tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllAlbumsByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where tags does not contain
        defaultAlbumFiltering("tags.doesNotContain=" + UPDATED_TAGS, "tags.doesNotContain=" + DEFAULT_TAGS);
    }

    @Test
    @Transactional
    void getAllAlbumsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where description equals to
        defaultAlbumFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlbumsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where description in
        defaultAlbumFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlbumsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where description is not null
        defaultAlbumFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where description contains
        defaultAlbumFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlbumsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where description does not contain
        defaultAlbumFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music equals to
        defaultAlbumFiltering("nbr_music.equals=" + DEFAULT_NBR_MUSIC, "nbr_music.equals=" + UPDATED_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music in
        defaultAlbumFiltering("nbr_music.in=" + DEFAULT_NBR_MUSIC + "," + UPDATED_NBR_MUSIC, "nbr_music.in=" + UPDATED_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music is not null
        defaultAlbumFiltering("nbr_music.specified=true", "nbr_music.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music is greater than or equal to
        defaultAlbumFiltering("nbr_music.greaterThanOrEqual=" + DEFAULT_NBR_MUSIC, "nbr_music.greaterThanOrEqual=" + UPDATED_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music is less than or equal to
        defaultAlbumFiltering("nbr_music.lessThanOrEqual=" + DEFAULT_NBR_MUSIC, "nbr_music.lessThanOrEqual=" + SMALLER_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music is less than
        defaultAlbumFiltering("nbr_music.lessThan=" + UPDATED_NBR_MUSIC, "nbr_music.lessThan=" + DEFAULT_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByNbr_musicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where nbr_music is greater than
        defaultAlbumFiltering("nbr_music.greaterThan=" + SMALLER_NBR_MUSIC, "nbr_music.greaterThan=" + DEFAULT_NBR_MUSIC);
    }

    @Test
    @Transactional
    void getAllAlbumsByAuthorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where author equals to
        defaultAlbumFiltering("author.equals=" + DEFAULT_AUTHOR, "author.equals=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllAlbumsByAuthorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where author in
        defaultAlbumFiltering("author.in=" + DEFAULT_AUTHOR + "," + UPDATED_AUTHOR, "author.in=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllAlbumsByAuthorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where author is not null
        defaultAlbumFiltering("author.specified=true", "author.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByAuthorContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where author contains
        defaultAlbumFiltering("author.contains=" + DEFAULT_AUTHOR, "author.contains=" + UPDATED_AUTHOR);
    }

    @Test
    @Transactional
    void getAllAlbumsByAuthorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where author does not contain
        defaultAlbumFiltering("author.doesNotContain=" + UPDATED_AUTHOR, "author.doesNotContain=" + DEFAULT_AUTHOR);
    }

    @Test
    @Transactional
    void getAllAlbumsByImage_urlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where image_url equals to
        defaultAlbumFiltering("image_url.equals=" + DEFAULT_IMAGE_URL, "image_url.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllAlbumsByImage_urlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where image_url in
        defaultAlbumFiltering("image_url.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "image_url.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllAlbumsByImage_urlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where image_url is not null
        defaultAlbumFiltering("image_url.specified=true", "image_url.specified=false");
    }

    @Test
    @Transactional
    void getAllAlbumsByImage_urlContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where image_url contains
        defaultAlbumFiltering("image_url.contains=" + DEFAULT_IMAGE_URL, "image_url.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllAlbumsByImage_urlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        // Get all the albumList where image_url does not contain
        defaultAlbumFiltering("image_url.doesNotContain=" + UPDATED_IMAGE_URL, "image_url.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllAlbumsByAlbum_categoryIsEqualToSomething() throws Exception {
        Category album_category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            albumRepository.saveAndFlush(album);
            album_category = CategoryResourceIT.createEntity();
        } else {
            album_category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(album_category);
        em.flush();
        album.setAlbum_category(album_category);
        albumRepository.saveAndFlush(album);
        Long album_categoryId = album_category.getId();
        // Get all the albumList where album_category equals to album_categoryId
        defaultAlbumShouldBeFound("album_categoryId.equals=" + album_categoryId);

        // Get all the albumList where album_category equals to (album_categoryId + 1)
        defaultAlbumShouldNotBeFound("album_categoryId.equals=" + (album_categoryId + 1));
    }

    private void defaultAlbumFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlbumShouldBeFound(shouldBeFound);
        defaultAlbumShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlbumShouldBeFound(String filter) throws Exception {
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(album.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].nbr_music").value(hasItem(DEFAULT_NBR_MUSIC)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].image_url").value(hasItem(DEFAULT_IMAGE_URL)));

        // Check, that the count call also returns 1
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlbumShouldNotBeFound(String filter) throws Exception {
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlbumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlbum() throws Exception {
        // Get the album
        restAlbumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlbum() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the album
        Album updatedAlbum = albumRepository.findById(album.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlbum are not directly saved in db
        em.detach(updatedAlbum);
        updatedAlbum
            .name(UPDATED_NAME)
            .tags(UPDATED_TAGS)
            .description(UPDATED_DESCRIPTION)
            .nbr_music(UPDATED_NBR_MUSIC)
            .author(UPDATED_AUTHOR)
            .image_url(UPDATED_IMAGE_URL);

        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlbum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlbumToMatchAllProperties(updatedAlbum);
    }

    @Test
    @Transactional
    void putNonExistingAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(put(ENTITY_API_URL_ID, album.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(album)))
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(album)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        partialUpdatedAlbum.name(UPDATED_NAME).author(UPDATED_AUTHOR);

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlbumUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlbum, album), getPersistedAlbum(album));
    }

    @Test
    @Transactional
    void fullUpdateAlbumWithPatch() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the album using partial update
        Album partialUpdatedAlbum = new Album();
        partialUpdatedAlbum.setId(album.getId());

        partialUpdatedAlbum
            .name(UPDATED_NAME)
            .tags(UPDATED_TAGS)
            .description(UPDATED_DESCRIPTION)
            .nbr_music(UPDATED_NBR_MUSIC)
            .author(UPDATED_AUTHOR)
            .image_url(UPDATED_IMAGE_URL);

        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlbum.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlbum))
            )
            .andExpect(status().isOk());

        // Validate the Album in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlbumUpdatableFieldsEquals(partialUpdatedAlbum, getPersistedAlbum(partialUpdatedAlbum));
    }

    @Test
    @Transactional
    void patchNonExistingAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, album.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(album))
            )
            .andExpect(status().isBadRequest());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlbum() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        album.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlbumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(album)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Album in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlbum() throws Exception {
        // Initialize the database
        insertedAlbum = albumRepository.saveAndFlush(album);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the album
        restAlbumMockMvc
            .perform(delete(ENTITY_API_URL_ID, album.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return albumRepository.count();
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

    protected Album getPersistedAlbum(Album album) {
        return albumRepository.findById(album.getId()).orElseThrow();
    }

    protected void assertPersistedAlbumToMatchAllProperties(Album expectedAlbum) {
        assertAlbumAllPropertiesEquals(expectedAlbum, getPersistedAlbum(expectedAlbum));
    }

    protected void assertPersistedAlbumToMatchUpdatableProperties(Album expectedAlbum) {
        assertAlbumAllUpdatablePropertiesEquals(expectedAlbum, getPersistedAlbum(expectedAlbum));
    }
}
