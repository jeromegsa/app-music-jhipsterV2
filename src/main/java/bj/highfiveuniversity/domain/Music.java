package bj.highfiveuniversity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * A Music.
 */
@Entity
@Table(name = "music")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Music implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "paroles")
    private String paroles;

    @Column(name = "created_at")
    private ZonedDateTime created_At;

    @Column(name = "updated_at")
    private Instant updated_At;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "album_category", "album_musics" }, allowSetters = true)
    private Album album;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Music id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Music title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Music duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getParoles() {
        return this.paroles;
    }

    public Music paroles(String paroles) {
        this.setParoles(paroles);
        return this;
    }

    public void setParoles(String paroles) {
        this.paroles = paroles;
    }

    public ZonedDateTime getCreated_At() {
        return this.created_At;
    }

    public Music created_At(ZonedDateTime created_At) {
        this.setCreated_At(created_At);
        return this;
    }

    public void setCreated_At(ZonedDateTime created_At) {
        this.created_At = created_At;
    }

    public Instant getUpdated_At() {
        return this.updated_At;
    }

    public Music updated_At(Instant updated_At) {
        this.setUpdated_At(updated_At);
        return this;
    }

    public void setUpdated_At(Instant updated_At) {
        this.updated_At = updated_At;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Music album(Album album) {
        this.setAlbum(album);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Music)) {
            return false;
        }
        return getId() != null && getId().equals(((Music) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Music{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", duration=" + getDuration() +
            ", paroles='" + getParoles() + "'" +
            ", created_At='" + getCreated_At() + "'" +
            ", updated_At='" + getUpdated_At() + "'" +
            "}";
    }
}
