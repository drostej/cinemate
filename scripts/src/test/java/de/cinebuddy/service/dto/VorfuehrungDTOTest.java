package de.cinebuddy.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VorfuehrungDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VorfuehrungDTO.class);
        VorfuehrungDTO vorfuehrungDTO1 = new VorfuehrungDTO();
        vorfuehrungDTO1.setId(1L);
        VorfuehrungDTO vorfuehrungDTO2 = new VorfuehrungDTO();
        assertThat(vorfuehrungDTO1).isNotEqualTo(vorfuehrungDTO2);
        vorfuehrungDTO2.setId(vorfuehrungDTO1.getId());
        assertThat(vorfuehrungDTO1).isEqualTo(vorfuehrungDTO2);
        vorfuehrungDTO2.setId(2L);
        assertThat(vorfuehrungDTO1).isNotEqualTo(vorfuehrungDTO2);
        vorfuehrungDTO1.setId(null);
        assertThat(vorfuehrungDTO1).isNotEqualTo(vorfuehrungDTO2);
    }
}
