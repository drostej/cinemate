package de.cinebuddy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 🧱 DOMAIN ENTITIES
 */
@Entity
@Table(name = "saal")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Saal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Min(value = 1)
    @Column(name = "kapazitaet", nullable = false)
    private Integer kapazitaet;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "saal")
    @JsonIgnoreProperties(value = { "saal", "reservierungens" }, allowSetters = true)
    private Set<Sitzplatz> sitzes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "saal")
    @JsonIgnoreProperties(value = { "reservierungens", "saal" }, allowSetters = true)
    private Set<Vorfuehrung> vorfuehrungens = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Saal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Saal name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKapazitaet() {
        return this.kapazitaet;
    }

    public Saal kapazitaet(Integer kapazitaet) {
        this.setKapazitaet(kapazitaet);
        return this;
    }

    public void setKapazitaet(Integer kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    public Set<Sitzplatz> getSitzes() {
        return this.sitzes;
    }

    public void setSitzes(Set<Sitzplatz> sitzplatzs) {
        if (this.sitzes != null) {
            this.sitzes.forEach(i -> i.setSaal(null));
        }
        if (sitzplatzs != null) {
            sitzplatzs.forEach(i -> i.setSaal(this));
        }
        this.sitzes = sitzplatzs;
    }

    public Saal sitzes(Set<Sitzplatz> sitzplatzs) {
        this.setSitzes(sitzplatzs);
        return this;
    }

    public Saal addSitze(Sitzplatz sitzplatz) {
        this.sitzes.add(sitzplatz);
        sitzplatz.setSaal(this);
        return this;
    }

    public Saal removeSitze(Sitzplatz sitzplatz) {
        this.sitzes.remove(sitzplatz);
        sitzplatz.setSaal(null);
        return this;
    }

    public Set<Vorfuehrung> getVorfuehrungens() {
        return this.vorfuehrungens;
    }

    public void setVorfuehrungens(Set<Vorfuehrung> vorfuehrungs) {
        if (this.vorfuehrungens != null) {
            this.vorfuehrungens.forEach(i -> i.setSaal(null));
        }
        if (vorfuehrungs != null) {
            vorfuehrungs.forEach(i -> i.setSaal(this));
        }
        this.vorfuehrungens = vorfuehrungs;
    }

    public Saal vorfuehrungens(Set<Vorfuehrung> vorfuehrungs) {
        this.setVorfuehrungens(vorfuehrungs);
        return this;
    }

    public Saal addVorfuehrungen(Vorfuehrung vorfuehrung) {
        this.vorfuehrungens.add(vorfuehrung);
        vorfuehrung.setSaal(this);
        return this;
    }

    public Saal removeVorfuehrungen(Vorfuehrung vorfuehrung) {
        this.vorfuehrungens.remove(vorfuehrung);
        vorfuehrung.setSaal(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Saal)) {
            return false;
        }
        return getId() != null && getId().equals(((Saal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Saal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", kapazitaet=" + getKapazitaet() +
            "}";
    }
}
