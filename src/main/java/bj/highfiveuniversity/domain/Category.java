package bj.highfiveuniversity.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category_name")
    private String category_name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album_category")
    @JsonIgnoreProperties(value = { "album_category", "album_musics" }, allowSetters = true)
    private Set<Album> albums = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory_name() {
        return this.category_name;
    }

    public Category category_name(String category_name) {
        this.setCategory_name(category_name);
        return this;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Set<Album> getAlbums() {
        return this.albums;
    }

    public void setAlbums(Set<Album> albums) {
        if (this.albums != null) {
            this.albums.forEach(i -> i.setAlbum_category(null));
        }
        if (albums != null) {
            albums.forEach(i -> i.setAlbum_category(this));
        }
        this.albums = albums;
    }

    public Category albums(Set<Album> albums) {
        this.setAlbums(albums);
        return this;
    }

    public Category addAlbum(Album album) {
        this.albums.add(album);
        album.setAlbum_category(this);
        return this;
    }

    public Category removeAlbum(Album album) {
        this.albums.remove(album);
        album.setAlbum_category(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", category_name='" + getCategory_name() + "'" +
            "}";
    }
}
