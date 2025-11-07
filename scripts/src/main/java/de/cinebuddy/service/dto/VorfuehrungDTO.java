package de.cinebuddy.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link de.cinebuddy.domain.Vorfuehrung} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VorfuehrungDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String filmTitel;

    @NotNull
    private Instant datumZeit;

    private SaalDTO saal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilmTitel() {
        return filmTitel;
    }

    public void setFilmTitel(String filmTitel) {
        this.filmTitel = filmTitel;
    }

    public Instant getDatumZeit() {
        return datumZeit;
    }

    public void setDatumZeit(Instant datumZeit) {
        this.datumZeit = datumZeit;
    }

    public SaalDTO getSaal() {
        return saal;
    }

    public void setSaal(SaalDTO saal) {
        this.saal = saal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VorfuehrungDTO)) {
            return false;
        }

        VorfuehrungDTO vorfuehrungDTO = (VorfuehrungDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vorfuehrungDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VorfuehrungDTO{" +
            "id=" + getId() +
            ", filmTitel='" + getFilmTitel() + "'" +
            ", datumZeit='" + getDatumZeit() + "'" +
            ", saal=" + getSaal() +
            "}";
    }
}
