package bj.highfiveuniversity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Album.
 */
@Entity
@Table(name = "album")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tags")
    private String tags;

    @Column(name = "description")
    private String description;

    @Column(name = "nbr_music")
    private Integer nbr_music;

    @Column(name = "author")
    private String author;

    @Column(name = "image_url")
    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "albums" }, allowSetters = true)
    private Category album_category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
    @JsonIgnoreProperties(value = { "album" }, allowSetters = true)
    private Set<Music> album_musics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Album id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Album name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTags() {
        return this.tags;
    }

    public Album tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return this.description;
    }

    public Album description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNbr_music() {
        return this.nbr_music;
    }

    public Album nbr_music(Integer nbr_music) {
        this.setNbr_music(nbr_music);
        return this;
    }

    public void setNbr_music(Integer nbr_music) {
        this.nbr_music = nbr_music;
    }

    public String getAuthor() {
        return this.author;
    }

    public Album author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public Album image_url(String image_url) {
        this.setImage_url(image_url);
        return this;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Category getAlbum_category() {
        return this.album_category;
    }

    public void setAlbum_category(Category category) {
        this.album_category = category;
    }

    public Album album_category(Category category) {
        this.setAlbum_category(category);
        return this;
    }

    public Set<Music> getAlbum_musics() {
        return this.album_musics;
    }

    public void setAlbum_musics(Set<Music> music) {
        if (this.album_musics != null) {
            this.album_musics.forEach(i -> i.setAlbum(null));
        }
        if (music != null) {
            music.forEach(i -> i.setAlbum(this));
        }
        this.album_musics = music;
    }

    public Album album_musics(Set<Music> music) {
        this.setAlbum_musics(music);
        return this;
    }

    public Album addAlbum_music(Music music) {
        this.album_musics.add(music);
        music.setAlbum(this);
        return this;
    }

    public Album removeAlbum_music(Music music) {
        this.album_musics.remove(music);
        music.setAlbum(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Album)) {
            return false;
        }
        return getId() != null && getId().equals(((Album) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Album{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", tags='" + getTags() + "'" +
            ", description='" + getDescription() + "'" +
            ", nbr_music=" + getNbr_music() +
            ", author='" + getAuthor() + "'" +
            ", image_url='" + getImage_url() + "'" +
            "}";
    }
}
