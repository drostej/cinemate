package de.cinebuddy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservierungDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservierungDTO.class);
        ReservierungDTO reservierungDTO1 = new ReservierungDTO();
        reservierungDTO1.setId(1L);
        ReservierungDTO reservierungDTO2 = new ReservierungDTO();
        assertThat(reservierungDTO1).isNotEqualTo(reservierungDTO2);
        reservierungDTO2.setId(reservierungDTO1.getId());
        assertThat(reservierungDTO1).isEqualTo(reservierungDTO2);
        reservierungDTO2.setId(2L);
        assertThat(reservierungDTO1).isNotEqualTo(reservierungDTO2);
        reservierungDTO1.setId(null);
        assertThat(reservierungDTO1).isNotEqualTo(reservierungDTO2);
    }
}
