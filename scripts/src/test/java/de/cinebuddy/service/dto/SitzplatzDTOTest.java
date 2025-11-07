package de.cinebuddy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SitzplatzDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SitzplatzDTO.class);
        SitzplatzDTO sitzplatzDTO1 = new SitzplatzDTO();
        sitzplatzDTO1.setId(1L);
        SitzplatzDTO sitzplatzDTO2 = new SitzplatzDTO();
        assertThat(sitzplatzDTO1).isNotEqualTo(sitzplatzDTO2);
        sitzplatzDTO2.setId(sitzplatzDTO1.getId());
        assertThat(sitzplatzDTO1).isEqualTo(sitzplatzDTO2);
        sitzplatzDTO2.setId(2L);
        assertThat(sitzplatzDTO1).isNotEqualTo(sitzplatzDTO2);
        sitzplatzDTO1.setId(null);
        assertThat(sitzplatzDTO1).isNotEqualTo(sitzplatzDTO2);
    }
}
