package bj.highfiveuniversity.service;

import bj.highfiveuniversity.domain.Music;
import bj.highfiveuniversity.repository.MusicRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bj.highfiveuniversity.domain.Music}.
 */
@Service
@Transactional
public class MusicService {

    private static final Logger LOG = LoggerFactory.getLogger(MusicService.class);

    private final MusicRepository musicRepository;

    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    /**
     * Save a music.
     *
     * @param music the entity to save.
     * @return the persisted entity.
     */
    public Music save(Music music) {
        LOG.debug("Request to save Music : {}", music);
        return musicRepository.save(music);
    }

    /**
     * Update a music.
     *
     * @param music the entity to save.
     * @return the persisted entity.
     */
    public Music update(Music music) {
        LOG.debug("Request to update Music : {}", music);
        return musicRepository.save(music);
    }

    /**
     * Partially update a music.
     *
     * @param music the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Music> partialUpdate(Music music) {
        LOG.debug("Request to partially update Music : {}", music);

        return musicRepository
            .findById(music.getId())
            .map(existingMusic -> {
                if (music.getTitle() != null) {
                    existingMusic.setTitle(music.getTitle());
                }
                if (music.getDuration() != null) {
                    existingMusic.setDuration(music.getDuration());
                }
                if (music.getParoles() != null) {
                    existingMusic.setParoles(music.getParoles());
                }
                if (music.getCreated_At() != null) {
                    existingMusic.setCreated_At(music.getCreated_At());
                }
                if (music.getUpdated_At() != null) {
                    existingMusic.setUpdated_At(music.getUpdated_At());
                }

                return existingMusic;
            })
            .map(musicRepository::save);
    }

    /**
     * Get all the music.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Music> findAll(Pageable pageable) {
        LOG.debug("Request to get all Music");
        return musicRepository.findAll(pageable);
    }

    /**
     * Get one music by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Music> findOne(Long id) {
        LOG.debug("Request to get Music : {}", id);
        return musicRepository.findById(id);
    }

    /**
     * Delete the music by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Music : {}", id);
        musicRepository.deleteById(id);
    }
}
