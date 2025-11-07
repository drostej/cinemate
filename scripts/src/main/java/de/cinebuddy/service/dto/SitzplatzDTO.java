package de.cinebuddy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link de.cinebuddy.domain.Sitzplatz} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SitzplatzDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 1)
    private String reihe;

    @NotNull
    private Integer nummer;

    private SaalDTO saal;

    private Set<ReservierungDTO> reservierungens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReihe() {
        return reihe;
    }

    public void setReihe(String reihe) {
        this.reihe = reihe;
    }

    public Integer getNummer() {
        return nummer;
    }

    public void setNummer(Integer nummer) {
        this.nummer = nummer;
    }

    public SaalDTO getSaal() {
        return saal;
    }

    public void setSaal(SaalDTO saal) {
        this.saal = saal;
    }

    public Set<ReservierungDTO> getReservierungens() {
        return reservierungens;
    }

    public void setReservierungens(Set<ReservierungDTO> reservierungens) {
        this.reservierungens = reservierungens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SitzplatzDTO)) {
            return false;
        }

        SitzplatzDTO sitzplatzDTO = (SitzplatzDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sitzplatzDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SitzplatzDTO{" +
            "id=" + getId() +
            ", reihe='" + getReihe() + "'" +
            ", nummer=" + getNummer() +
            ", saal=" + getSaal() +
            ", reservierungens=" + getReservierungens() +
            "}";
    }
}
