package de.cinebuddy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Vorfuehrung.
 */
@Entity
@Table(name = "vorfuehrung")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vorfuehrung implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "film_titel", length = 200, nullable = false)
    private String filmTitel;

    @NotNull
    @Column(name = "datum_zeit", nullable = false)
    private Instant datumZeit;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vorfuehrung")
    @JsonIgnoreProperties(value = { "plaetzes", "vorfuehrung" }, allowSetters = true)
    private Set<Reservierung> reservierungens = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitzes", "vorfuehrungens" }, allowSetters = true)
    private Saal saal;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vorfuehrung id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilmTitel() {
        return this.filmTitel;
    }

    public Vorfuehrung filmTitel(String filmTitel) {
        this.setFilmTitel(filmTitel);
        return this;
    }

    public void setFilmTitel(String filmTitel) {
        this.filmTitel = filmTitel;
    }

    public Instant getDatumZeit() {
        return this.datumZeit;
    }

    public Vorfuehrung datumZeit(Instant datumZeit) {
        this.setDatumZeit(datumZeit);
        return this;
    }

    public void setDatumZeit(Instant datumZeit) {
        this.datumZeit = datumZeit;
    }

    public Set<Reservierung> getReservierungens() {
        return this.reservierungens;
    }

    public void setReservierungens(Set<Reservierung> reservierungs) {
        if (this.reservierungens != null) {
            this.reservierungens.forEach(i -> i.setVorfuehrung(null));
        }
        if (reservierungs != null) {
            reservierungs.forEach(i -> i.setVorfuehrung(this));
        }
        this.reservierungens = reservierungs;
    }

    public Vorfuehrung reservierungens(Set<Reservierung> reservierungs) {
        this.setReservierungens(reservierungs);
        return this;
    }

    public Vorfuehrung addReservierungen(Reservierung reservierung) {
        this.reservierungens.add(reservierung);
        reservierung.setVorfuehrung(this);
        return this;
    }

    public Vorfuehrung removeReservierungen(Reservierung reservierung) {
        this.reservierungens.remove(reservierung);
        reservierung.setVorfuehrung(null);
        return this;
    }

    public Saal getSaal() {
        return this.saal;
    }

    public void setSaal(Saal saal) {
        this.saal = saal;
    }

    public Vorfuehrung saal(Saal saal) {
        this.setSaal(saal);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vorfuehrung)) {
            return false;
        }
        return getId() != null && getId().equals(((Vorfuehrung) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vorfuehrung{" +
            "id=" + getId() +
            ", filmTitel='" + getFilmTitel() + "'" +
            ", datumZeit='" + getDatumZeit() + "'" +
            "}";
    }
}
