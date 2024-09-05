package bj.highfiveuniversity.web.rest;

import bj.highfiveuniversity.domain.Music;
import bj.highfiveuniversity.repository.MusicRepository;
import bj.highfiveuniversity.service.MusicService;
import bj.highfiveuniversity.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bj.highfiveuniversity.domain.Music}.
 */
@RestController
@RequestMapping("/api/music")
public class MusicResource {

    private static final Logger LOG = LoggerFactory.getLogger(MusicResource.class);

    private static final String ENTITY_NAME = "music";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MusicService musicService;

    private final MusicRepository musicRepository;

    public MusicResource(MusicService musicService, MusicRepository musicRepository) {
        this.musicService = musicService;
        this.musicRepository = musicRepository;
    }

    /**
     * {@code POST  /music} : Create a new music.
     *
     * @param music the music to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new music, or with status {@code 400 (Bad Request)} if the music has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Music> createMusic(@RequestBody Music music) throws URISyntaxException {
        LOG.debug("REST request to save Music : {}", music);
        if (music.getId() != null) {
            throw new BadRequestAlertException("A new music cannot already have an ID", ENTITY_NAME, "idexists");
        }
        music = musicService.save(music);
        return ResponseEntity.created(new URI("/api/music/" + music.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, music.getId().toString()))
            .body(music);
    }

    /**
     * {@code PUT  /music/:id} : Updates an existing music.
     *
     * @param id the id of the music to save.
     * @param music the music to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated music,
     * or with status {@code 400 (Bad Request)} if the music is not valid,
     * or with status {@code 500 (Internal Server Error)} if the music couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Music> updateMusic(@PathVariable(value = "id", required = false) final Long id, @RequestBody Music music)
        throws URISyntaxException {
        LOG.debug("REST request to update Music : {}, {}", id, music);
        if (music.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, music.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        music = musicService.update(music);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, music.getId().toString()))
            .body(music);
    }

    /**
     * {@code PATCH  /music/:id} : Partial updates given fields of an existing music, field will ignore if it is null
     *
     * @param id the id of the music to save.
     * @param music the music to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated music,
     * or with status {@code 400 (Bad Request)} if the music is not valid,
     * or with status {@code 404 (Not Found)} if the music is not found,
     * or with status {@code 500 (Internal Server Error)} if the music couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Music> partialUpdateMusic(@PathVariable(value = "id", required = false) final Long id, @RequestBody Music music)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Music partially : {}, {}", id, music);
        if (music.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, music.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!musicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Music> result = musicService.partialUpdate(music);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, music.getId().toString())
        );
    }

    /**
     * {@code GET  /music} : get all the music.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of music in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Music>> getAllMusic(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Music");
        Page<Music> page = musicService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /music/:id} : get the "id" music.
     *
     * @param id the id of the music to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the music, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Music> getMusic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Music : {}", id);
        Optional<Music> music = musicService.findOne(id);
        return ResponseUtil.wrapOrNotFound(music);
    }

    /**
     * {@code DELETE  /music/:id} : delete the "id" music.
     *
     * @param id the id of the music to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Music : {}", id);
        musicService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
