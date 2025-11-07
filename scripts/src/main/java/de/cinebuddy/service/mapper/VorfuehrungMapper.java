package de.cinebuddy.service.mapper;

import de.cinebuddy.domain.Saal;
import de.cinebuddy.domain.Vorfuehrung;
import de.cinebuddy.service.dto.SaalDTO;
import de.cinebuddy.service.dto.VorfuehrungDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vorfuehrung} and its DTO {@link VorfuehrungDTO}.
 */
@Mapper(componentModel = "spring")
public interface VorfuehrungMapper extends EntityMapper<VorfuehrungDTO, Vorfuehrung> {
    @Mapping(target = "saal", source = "saal", qualifiedByName = "saalName")
    VorfuehrungDTO toDto(Vorfuehrung s);

    @Named("saalName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SaalDTO toDtoSaalName(Saal saal);
}
