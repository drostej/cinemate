package de.cinebuddy.service.mapper;

import static de.cinebuddy.domain.VorfuehrungAsserts.*;
import static de.cinebuddy.domain.VorfuehrungTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VorfuehrungMapperTest {

    private VorfuehrungMapper vorfuehrungMapper;

    @BeforeEach
    void setUp() {
        vorfuehrungMapper = new VorfuehrungMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVorfuehrungSample1();
        var actual = vorfuehrungMapper.toEntity(vorfuehrungMapper.toDto(expected));
        assertVorfuehrungAllPropertiesEquals(expected, actual);
    }
}
