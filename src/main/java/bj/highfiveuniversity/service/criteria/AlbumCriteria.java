package bj.highfiveuniversity.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link bj.highfiveuniversity.domain.Album} entity. This class is used
 * in {@link bj.highfiveuniversity.web.rest.AlbumResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /albums?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AlbumCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter tags;

    private StringFilter description;

    private IntegerFilter nbr_music;

    private StringFilter author;

    private StringFilter image_url;

    private LongFilter album_categoryId;

    private LongFilter album_musicId;

    private Boolean distinct;

    public AlbumCriteria() {}

    public AlbumCriteria(AlbumCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.tags = other.optionalTags().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.nbr_music = other.optionalNbr_music().map(IntegerFilter::copy).orElse(null);
        this.author = other.optionalAuthor().map(StringFilter::copy).orElse(null);
        this.image_url = other.optionalImage_url().map(StringFilter::copy).orElse(null);
        this.album_categoryId = other.optionalAlbum_categoryId().map(LongFilter::copy).orElse(null);
        this.album_musicId = other.optionalAlbum_musicId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AlbumCriteria copy() {
        return new AlbumCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getTags() {
        return tags;
    }

    public Optional<StringFilter> optionalTags() {
        return Optional.ofNullable(tags);
    }

    public StringFilter tags() {
        if (tags == null) {
            setTags(new StringFilter());
        }
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getNbr_music() {
        return nbr_music;
    }

    public Optional<IntegerFilter> optionalNbr_music() {
        return Optional.ofNullable(nbr_music);
    }

    public IntegerFilter nbr_music() {
        if (nbr_music == null) {
            setNbr_music(new IntegerFilter());
        }
        return nbr_music;
    }

    public void setNbr_music(IntegerFilter nbr_music) {
        this.nbr_music = nbr_music;
    }

    public StringFilter getAuthor() {
        return author;
    }

    public Optional<StringFilter> optionalAuthor() {
        return Optional.ofNullable(author);
    }

    public StringFilter author() {
        if (author == null) {
            setAuthor(new StringFilter());
        }
        return author;
    }

    public void setAuthor(StringFilter author) {
        this.author = author;
    }

    public StringFilter getImage_url() {
        return image_url;
    }

    public Optional<StringFilter> optionalImage_url() {
        return Optional.ofNullable(image_url);
    }

    public StringFilter image_url() {
        if (image_url == null) {
            setImage_url(new StringFilter());
        }
        return image_url;
    }

    public void setImage_url(StringFilter image_url) {
        this.image_url = image_url;
    }

    public LongFilter getAlbum_categoryId() {
        return album_categoryId;
    }

    public Optional<LongFilter> optionalAlbum_categoryId() {
        return Optional.ofNullable(album_categoryId);
    }

    public LongFilter album_categoryId() {
        if (album_categoryId == null) {
            setAlbum_categoryId(new LongFilter());
        }
        return album_categoryId;
    }

    public void setAlbum_categoryId(LongFilter album_categoryId) {
        this.album_categoryId = album_categoryId;
    }

    public LongFilter getAlbum_musicId() {
        return album_musicId;
    }

    public Optional<LongFilter> optionalAlbum_musicId() {
        return Optional.ofNullable(album_musicId);
    }

    public LongFilter album_musicId() {
        if (album_musicId == null) {
            setAlbum_musicId(new LongFilter());
        }
        return album_musicId;
    }

    public void setAlbum_musicId(LongFilter album_musicId) {
        this.album_musicId = album_musicId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlbumCriteria that = (AlbumCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(description, that.description) &&
            Objects.equals(nbr_music, that.nbr_music) &&
            Objects.equals(author, that.author) &&
            Objects.equals(image_url, that.image_url) &&
            Objects.equals(album_categoryId, that.album_categoryId) &&
            Objects.equals(album_musicId, that.album_musicId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags, description, nbr_music, author, image_url, album_categoryId, album_musicId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AlbumCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalTags().map(f -> "tags=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalNbr_music().map(f -> "nbr_music=" + f + ", ").orElse("") +
            optionalAuthor().map(f -> "author=" + f + ", ").orElse("") +
            optionalImage_url().map(f -> "image_url=" + f + ", ").orElse("") +
            optionalAlbum_categoryId().map(f -> "album_categoryId=" + f + ", ").orElse("") +
            optionalAlbum_musicId().map(f -> "album_musicId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
