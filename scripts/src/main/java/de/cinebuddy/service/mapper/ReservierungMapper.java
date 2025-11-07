package de.cinebuddy.service.mapper;

import de.cinebuddy.domain.Reservierung;
import de.cinebuddy.domain.Sitzplatz;
import de.cinebuddy.domain.Vorfuehrung;
import de.cinebuddy.service.dto.ReservierungDTO;
import de.cinebuddy.service.dto.SitzplatzDTO;
import de.cinebuddy.service.dto.VorfuehrungDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservierung} and its DTO {@link ReservierungDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReservierungMapper extends EntityMapper<ReservierungDTO, Reservierung> {
    @Mapping(target = "plaetzes", source = "plaetzes", qualifiedByName = "sitzplatzIdSet")
    @Mapping(target = "vorfuehrung", source = "vorfuehrung", qualifiedByName = "vorfuehrungFilmTitel")
    ReservierungDTO toDto(Reservierung s);

    @Mapping(target = "removePlaetze", ignore = true)
    Reservierung toEntity(ReservierungDTO reservierungDTO);

    @Named("sitzplatzId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SitzplatzDTO toDtoSitzplatzId(Sitzplatz sitzplatz);

    @Named("sitzplatzIdSet")
    default Set<SitzplatzDTO> toDtoSitzplatzIdSet(Set<Sitzplatz> sitzplatz) {
        return sitzplatz.stream().map(this::toDtoSitzplatzId).collect(Collectors.toSet());
    }

    @Named("vorfuehrungFilmTitel")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "filmTitel", source = "filmTitel")
    VorfuehrungDTO toDtoVorfuehrungFilmTitel(Vorfuehrung vorfuehrung);
}
