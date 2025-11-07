package de.cinebuddy.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.cinebuddy.domain.enumeration.ReservierungsStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Reservierung.
 */
@Entity
@Table(name = "reservierung")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reservierung implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "kunde_name", length = 100, nullable = false)
    private String kundeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservierungsStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_reservierung__plaetze",
        joinColumns = @JoinColumn(name = "reservierung_id"),
        inverseJoinColumns = @JoinColumn(name = "plaetze_id")
    )
    @JsonIgnoreProperties(value = { "saal", "reservierungens" }, allowSetters = true)
    private Set<Sitzplatz> plaetzes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reservierungens", "saal" }, allowSetters = true)
    private Vorfuehrung vorfuehrung;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservierung id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKundeName() {
        return this.kundeName;
    }

    public Reservierung kundeName(String kundeName) {
        this.setKundeName(kundeName);
        return this;
    }

    public void setKundeName(String kundeName) {
        this.kundeName = kundeName;
    }

    public ReservierungsStatus getStatus() {
        return this.status;
    }

    public Reservierung status(ReservierungsStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReservierungsStatus status) {
        this.status = status;
    }

    public Set<Sitzplatz> getPlaetzes() {
        return this.plaetzes;
    }

    public void setPlaetzes(Set<Sitzplatz> sitzplatzs) {
        this.plaetzes = sitzplatzs;
    }

    public Reservierung plaetzes(Set<Sitzplatz> sitzplatzs) {
        this.setPlaetzes(sitzplatzs);
        return this;
    }

    public Reservierung addPlaetze(Sitzplatz sitzplatz) {
        this.plaetzes.add(sitzplatz);
        return this;
    }

    public Reservierung removePlaetze(Sitzplatz sitzplatz) {
        this.plaetzes.remove(sitzplatz);
        return this;
    }

    public Vorfuehrung getVorfuehrung() {
        return this.vorfuehrung;
    }

    public void setVorfuehrung(Vorfuehrung vorfuehrung) {
        this.vorfuehrung = vorfuehrung;
    }

    public Reservierung vorfuehrung(Vorfuehrung vorfuehrung) {
        this.setVorfuehrung(vorfuehrung);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservierung)) {
            return false;
        }
        return getId() != null && getId().equals(((Reservierung) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservierung{" +
            "id=" + getId() +
            ", kundeName='" + getKundeName() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
