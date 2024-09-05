package bj.highfiveuniversity.service;

import bj.highfiveuniversity.domain.Album;
import bj.highfiveuniversity.repository.AlbumRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link bj.highfiveuniversity.domain.Album}.
 */
@Service
@Transactional
public class AlbumService {

    private static final Logger LOG = LoggerFactory.getLogger(AlbumService.class);

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * Save a album.
     *
     * @param album the entity to save.
     * @return the persisted entity.
     */
    public Album save(Album album) {
        LOG.debug("Request to save Album : {}", album);
        return albumRepository.save(album);
    }

    /**
     * Update a album.
     *
     * @param album the entity to save.
     * @return the persisted entity.
     */
    public Album update(Album album) {
        LOG.debug("Request to update Album : {}", album);
        return albumRepository.save(album);
    }

    /**
     * Partially update a album.
     *
     * @param album the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Album> partialUpdate(Album album) {
        LOG.debug("Request to partially update Album : {}", album);

        return albumRepository
            .findById(album.getId())
            .map(existingAlbum -> {
                if (album.getName() != null) {
                    existingAlbum.setName(album.getName());
                }
                if (album.getTags() != null) {
                    existingAlbum.setTags(album.getTags());
                }
                if (album.getDescription() != null) {
                    existingAlbum.setDescription(album.getDescription());
                }
                if (album.getNbr_music() != null) {
                    existingAlbum.setNbr_music(album.getNbr_music());
                }
                if (album.getAuthor() != null) {
                    existingAlbum.setAuthor(album.getAuthor());
                }
                if (album.getImage_url() != null) {
                    existingAlbum.setImage_url(album.getImage_url());
                }

                return existingAlbum;
            })
            .map(albumRepository::save);
    }

    /**
     * Get all the albums with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Album> findAllWithEagerRelationships(Pageable pageable) {
        return albumRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one album by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Album> findOne(Long id) {
        LOG.debug("Request to get Album : {}", id);
        return albumRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the album by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Album : {}", id);
        albumRepository.deleteById(id);
    }
}
