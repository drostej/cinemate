package de.cinebuddy.domain;

import static de.cinebuddy.domain.ReservierungTestSamples.*;
import static de.cinebuddy.domain.SaalTestSamples.*;
import static de.cinebuddy.domain.VorfuehrungTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VorfuehrungTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vorfuehrung.class);
        Vorfuehrung vorfuehrung1 = getVorfuehrungSample1();
        Vorfuehrung vorfuehrung2 = new Vorfuehrung();
        assertThat(vorfuehrung1).isNotEqualTo(vorfuehrung2);

        vorfuehrung2.setId(vorfuehrung1.getId());
        assertThat(vorfuehrung1).isEqualTo(vorfuehrung2);

        vorfuehrung2 = getVorfuehrungSample2();
        assertThat(vorfuehrung1).isNotEqualTo(vorfuehrung2);
    }

    @Test
    void reservierungenTest() {
        Vorfuehrung vorfuehrung = getVorfuehrungRandomSampleGenerator();
        Reservierung reservierungBack = getReservierungRandomSampleGenerator();

        vorfuehrung.addReservierungen(reservierungBack);
        assertThat(vorfuehrung.getReservierungens()).containsOnly(reservierungBack);
        assertThat(reservierungBack.getVorfuehrung()).isEqualTo(vorfuehrung);

        vorfuehrung.removeReservierungen(reservierungBack);
        assertThat(vorfuehrung.getReservierungens()).doesNotContain(reservierungBack);
        assertThat(reservierungBack.getVorfuehrung()).isNull();

        vorfuehrung.reservierungens(new HashSet<>(Set.of(reservierungBack)));
        assertThat(vorfuehrung.getReservierungens()).containsOnly(reservierungBack);
        assertThat(reservierungBack.getVorfuehrung()).isEqualTo(vorfuehrung);

        vorfuehrung.setReservierungens(new HashSet<>());
        assertThat(vorfuehrung.getReservierungens()).doesNotContain(reservierungBack);
        assertThat(reservierungBack.getVorfuehrung()).isNull();
    }

    @Test
    void saalTest() {
        Vorfuehrung vorfuehrung = getVorfuehrungRandomSampleGenerator();
        Saal saalBack = getSaalRandomSampleGenerator();

        vorfuehrung.setSaal(saalBack);
        assertThat(vorfuehrung.getSaal()).isEqualTo(saalBack);

        vorfuehrung.saal(null);
        assertThat(vorfuehrung.getSaal()).isNull();
    }
}
