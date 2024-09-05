package bj.highfiveuniversity.service;

import bj.highfiveuniversity.domain.*; // for static metamodels
import bj.highfiveuniversity.domain.Album;
import bj.highfiveuniversity.repository.AlbumRepository;
import bj.highfiveuniversity.service.criteria.AlbumCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Album} entities in the database.
 * The main input is a {@link AlbumCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Album} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlbumQueryService extends QueryService<Album> {

    private static final Logger LOG = LoggerFactory.getLogger(AlbumQueryService.class);

    private final AlbumRepository albumRepository;

    public AlbumQueryService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    /**
     * Return a {@link Page} of {@link Album} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Album> findByCriteria(AlbumCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Album> specification = createSpecification(criteria);
        return albumRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlbumCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Album> specification = createSpecification(criteria);
        return albumRepository.count(specification);
    }

    /**
     * Function to convert {@link AlbumCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Album> createSpecification(AlbumCriteria criteria) {
        Specification<Album> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Album_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Album_.name));
            }
            if (criteria.getTags() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTags(), Album_.tags));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Album_.description));
            }
            if (criteria.getNbr_music() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNbr_music(), Album_.nbr_music));
            }
            if (criteria.getAuthor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAuthor(), Album_.author));
            }
            if (criteria.getImage_url() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImage_url(), Album_.image_url));
            }
            if (criteria.getAlbum_categoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlbum_categoryId(), root ->
                        root.join(Album_.album_category, JoinType.LEFT).get(Category_.id)
                    )
                );
            }
            if (criteria.getAlbum_musicId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAlbum_musicId(), root -> root.join(Album_.album_musics, JoinType.LEFT).get(Music_.id))
                );
            }
        }
        return specification;
    }
}
