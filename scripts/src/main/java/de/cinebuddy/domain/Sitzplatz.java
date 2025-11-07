package de.cinebuddy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sitzplatz.
 */
@Entity
@Table(name = "sitzplatz")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sitzplatz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "reihe", length = 1, nullable = false)
    private String reihe;

    @NotNull
    @Column(name = "nummer", nullable = false)
    private Integer nummer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sitzes", "vorfuehrungens" }, allowSetters = true)
    private Saal saal;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "plaetzes")
    @JsonIgnoreProperties(value = { "plaetzes", "vorfuehrung" }, allowSetters = true)
    private Set<Reservierung> reservierungens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sitzplatz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReihe() {
        return this.reihe;
    }

    public Sitzplatz reihe(String reihe) {
        this.setReihe(reihe);
        return this;
    }

    public void setReihe(String reihe) {
        this.reihe = reihe;
    }

    public Integer getNummer() {
        return this.nummer;
    }

    public Sitzplatz nummer(Integer nummer) {
        this.setNummer(nummer);
        return this;
    }

    public void setNummer(Integer nummer) {
        this.nummer = nummer;
    }

    public Saal getSaal() {
        return this.saal;
    }

    public void setSaal(Saal saal) {
        this.saal = saal;
    }

    public Sitzplatz saal(Saal saal) {
        this.setSaal(saal);
        return this;
    }

    public Set<Reservierung> getReservierungens() {
        return this.reservierungens;
    }

    public void setReservierungens(Set<Reservierung> reservierungs) {
        if (this.reservierungens != null) {
            this.reservierungens.forEach(i -> i.removePlaetze(this));
        }
        if (reservierungs != null) {
            reservierungs.forEach(i -> i.addPlaetze(this));
        }
        this.reservierungens = reservierungs;
    }

    public Sitzplatz reservierungens(Set<Reservierung> reservierungs) {
        this.setReservierungens(reservierungs);
        return this;
    }

    public Sitzplatz addReservierungen(Reservierung reservierung) {
        this.reservierungens.add(reservierung);
        reservierung.getPlaetzes().add(this);
        return this;
    }

    public Sitzplatz removeReservierungen(Reservierung reservierung) {
        this.reservierungens.remove(reservierung);
        reservierung.getPlaetzes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sitzplatz)) {
            return false;
        }
        return getId() != null && getId().equals(((Sitzplatz) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sitzplatz{" +
            "id=" + getId() +
            ", reihe='" + getReihe() + "'" +
            ", nummer=" + getNummer() +
            "}";
    }
}
