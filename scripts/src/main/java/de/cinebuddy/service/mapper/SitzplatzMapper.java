package de.cinebuddy.service.mapper;

import de.cinebuddy.domain.Reservierung;
import de.cinebuddy.domain.Saal;
import de.cinebuddy.domain.Sitzplatz;
import de.cinebuddy.service.dto.ReservierungDTO;
import de.cinebuddy.service.dto.SaalDTO;
import de.cinebuddy.service.dto.SitzplatzDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sitzplatz} and its DTO {@link SitzplatzDTO}.
 */
@Mapper(componentModel = "spring")
public interface SitzplatzMapper extends EntityMapper<SitzplatzDTO, Sitzplatz> {
    @Mapping(target = "saal", source = "saal", qualifiedByName = "saalName")
    @Mapping(target = "reservierungens", source = "reservierungens", qualifiedByName = "reservierungIdSet")
    SitzplatzDTO toDto(Sitzplatz s);

    @Mapping(target = "reservierungens", ignore = true)
    @Mapping(target = "removeReservierungen", ignore = true)
    Sitzplatz toEntity(SitzplatzDTO sitzplatzDTO);

    @Named("saalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SaalDTO toDtoSaalName(Saal saal);

    @Named("reservierungId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReservierungDTO toDtoReservierungId(Reservierung reservierung);

    @Named("reservierungIdSet")
    default Set<ReservierungDTO> toDtoReservierungIdSet(Set<Reservierung> reservierung) {
        return reservierung.stream().map(this::toDtoReservierungId).collect(Collectors.toSet());
    }
}
