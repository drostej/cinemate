package de.cinebuddy.service.mapper;

import static de.cinebuddy.domain.SitzplatzAsserts.*;
import static de.cinebuddy.domain.SitzplatzTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SitzplatzMapperTest {

    private SitzplatzMapper sitzplatzMapper;

    @BeforeEach
    void setUp() {
        sitzplatzMapper = new SitzplatzMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSitzplatzSample1();
        var actual = sitzplatzMapper.toEntity(sitzplatzMapper.toDto(expected));
        assertSitzplatzAllPropertiesEquals(expected, actual);
    }
}
