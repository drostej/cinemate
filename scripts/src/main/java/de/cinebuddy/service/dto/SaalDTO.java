package de.cinebuddy.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link de.cinebuddy.domain.Saal} entity.
 */
@Schema(description = "🧱 DOMAIN ENTITIES")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaalDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(value = 1)
    private Integer kapazitaet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKapazitaet() {
        return kapazitaet;
    }

    public void setKapazitaet(Integer kapazitaet) {
        this.kapazitaet = kapazitaet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaalDTO)) {
            return false;
        }

        SaalDTO saalDTO = (SaalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaalDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", kapazitaet=" + getKapazitaet() +
            "}";
    }
}
