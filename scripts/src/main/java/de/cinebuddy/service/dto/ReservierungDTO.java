package de.cinebuddy.service.dto;

import de.cinebuddy.domain.enumeration.ReservierungsStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link de.cinebuddy.domain.Reservierung} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservierungDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String kundeName;

    @NotNull
    private ReservierungsStatus status;

    private Set<SitzplatzDTO> plaetzes = new HashSet<>();

    private VorfuehrungDTO vorfuehrung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKundeName() {
        return kundeName;
    }

    public void setKundeName(String kundeName) {
        this.kundeName = kundeName;
    }

    public ReservierungsStatus getStatus() {
        return status;
    }

    public void setStatus(ReservierungsStatus status) {
        this.status = status;
    }

    public Set<SitzplatzDTO> getPlaetzes() {
        return plaetzes;
    }

    public void setPlaetzes(Set<SitzplatzDTO> plaetzes) {
        this.plaetzes = plaetzes;
    }

    public VorfuehrungDTO getVorfuehrung() {
        return vorfuehrung;
    }

    public void setVorfuehrung(VorfuehrungDTO vorfuehrung) {
        this.vorfuehrung = vorfuehrung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservierungDTO)) {
            return false;
        }

        ReservierungDTO reservierungDTO = (ReservierungDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reservierungDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservierungDTO{" +
            "id=" + getId() +
            ", kundeName='" + getKundeName() + "'" +
            ", status='" + getStatus() + "'" +
            ", plaetzes=" + getPlaetzes() +
            ", vorfuehrung=" + getVorfuehrung() +
            "}";
    }
}
