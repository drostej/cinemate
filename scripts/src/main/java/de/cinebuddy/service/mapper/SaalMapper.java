package de.cinebuddy.service.mapper;

import de.cinebuddy.domain.Saal;
import de.cinebuddy.service.dto.SaalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Saal} and its DTO {@link SaalDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaalMapper extends EntityMapper<SaalDTO, Saal> {}
