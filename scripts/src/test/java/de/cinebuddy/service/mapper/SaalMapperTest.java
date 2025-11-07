package de.cinebuddy.service.mapper;

import static de.cinebuddy.domain.SaalAsserts.*;
import static de.cinebuddy.domain.SaalTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SaalMapperTest {

    private SaalMapper saalMapper;

    @BeforeEach
    void setUp() {
        saalMapper = new SaalMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSaalSample1();
        var actual = saalMapper.toEntity(saalMapper.toDto(expected));
        assertSaalAllPropertiesEquals(expected, actual);
    }
}
