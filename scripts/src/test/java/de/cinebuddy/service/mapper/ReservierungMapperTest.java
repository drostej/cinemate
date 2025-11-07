package de.cinebuddy.service.mapper;

import static de.cinebuddy.domain.ReservierungAsserts.*;
import static de.cinebuddy.domain.ReservierungTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservierungMapperTest {

    private ReservierungMapper reservierungMapper;

    @BeforeEach
    void setUp() {
        reservierungMapper = new ReservierungMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReservierungSample1();
        var actual = reservierungMapper.toEntity(reservierungMapper.toDto(expected));
        assertReservierungAllPropertiesEquals(expected, actual);
    }
}
