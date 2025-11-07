package de.cinebuddy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SaalDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaalDTO.class);
        SaalDTO saalDTO1 = new SaalDTO();
        saalDTO1.setId(1L);
        SaalDTO saalDTO2 = new SaalDTO();
        assertThat(saalDTO1).isNotEqualTo(saalDTO2);
        saalDTO2.setId(saalDTO1.getId());
        assertThat(saalDTO1).isEqualTo(saalDTO2);
        saalDTO2.setId(2L);
        assertThat(saalDTO1).isNotEqualTo(saalDTO2);
        saalDTO1.setId(null);
        assertThat(saalDTO1).isNotEqualTo(saalDTO2);
    }
}
