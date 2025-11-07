package de.cinebuddy.domain;

import static de.cinebuddy.domain.SaalTestSamples.*;
import static de.cinebuddy.domain.SitzplatzTestSamples.*;
import static de.cinebuddy.domain.VorfuehrungTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SaalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Saal.class);
        Saal saal1 = getSaalSample1();
        Saal saal2 = new Saal();
        assertThat(saal1).isNotEqualTo(saal2);

        saal2.setId(saal1.getId());
        assertThat(saal1).isEqualTo(saal2);

        saal2 = getSaalSample2();
        assertThat(saal1).isNotEqualTo(saal2);
    }

    @Test
    void sitzeTest() {
        Saal saal = getSaalRandomSampleGenerator();
        Sitzplatz sitzplatzBack = getSitzplatzRandomSampleGenerator();

        saal.addSitze(sitzplatzBack);
        assertThat(saal.getSitzes()).containsOnly(sitzplatzBack);
        assertThat(sitzplatzBack.getSaal()).isEqualTo(saal);

        saal.removeSitze(sitzplatzBack);
        assertThat(saal.getSitzes()).doesNotContain(sitzplatzBack);
        assertThat(sitzplatzBack.getSaal()).isNull();

        saal.sitzes(new HashSet<>(Set.of(sitzplatzBack)));
        assertThat(saal.getSitzes()).containsOnly(sitzplatzBack);
        assertThat(sitzplatzBack.getSaal()).isEqualTo(saal);

        saal.setSitzes(new HashSet<>());
        assertThat(saal.getSitzes()).doesNotContain(sitzplatzBack);
        assertThat(sitzplatzBack.getSaal()).isNull();
    }

    @Test
    void vorfuehrungenTest() {
        Saal saal = getSaalRandomSampleGenerator();
        Vorfuehrung vorfuehrungBack = getVorfuehrungRandomSampleGenerator();

        saal.addVorfuehrungen(vorfuehrungBack);
        assertThat(saal.getVorfuehrungens()).containsOnly(vorfuehrungBack);
        assertThat(vorfuehrungBack.getSaal()).isEqualTo(saal);

        saal.removeVorfuehrungen(vorfuehrungBack);
        assertThat(saal.getVorfuehrungens()).doesNotContain(vorfuehrungBack);
        assertThat(vorfuehrungBack.getSaal()).isNull();

        saal.vorfuehrungens(new HashSet<>(Set.of(vorfuehrungBack)));
        assertThat(saal.getVorfuehrungens()).containsOnly(vorfuehrungBack);
        assertThat(vorfuehrungBack.getSaal()).isEqualTo(saal);

        saal.setVorfuehrungens(new HashSet<>());
        assertThat(saal.getVorfuehrungens()).doesNotContain(vorfuehrungBack);
        assertThat(vorfuehrungBack.getSaal()).isNull();
    }
}
