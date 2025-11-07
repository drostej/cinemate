package de.cinebuddy.domain;

import static de.cinebuddy.domain.ReservierungTestSamples.*;
import static de.cinebuddy.domain.SaalTestSamples.*;
import static de.cinebuddy.domain.SitzplatzTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SitzplatzTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sitzplatz.class);
        Sitzplatz sitzplatz1 = getSitzplatzSample1();
        Sitzplatz sitzplatz2 = new Sitzplatz();
        assertThat(sitzplatz1).isNotEqualTo(sitzplatz2);

        sitzplatz2.setId(sitzplatz1.getId());
        assertThat(sitzplatz1).isEqualTo(sitzplatz2);

        sitzplatz2 = getSitzplatzSample2();
        assertThat(sitzplatz1).isNotEqualTo(sitzplatz2);
    }

    @Test
    void saalTest() {
        Sitzplatz sitzplatz = getSitzplatzRandomSampleGenerator();
        Saal saalBack = getSaalRandomSampleGenerator();

        sitzplatz.setSaal(saalBack);
        assertThat(sitzplatz.getSaal()).isEqualTo(saalBack);

        sitzplatz.saal(null);
        assertThat(sitzplatz.getSaal()).isNull();
    }

    @Test
    void reservierungenTest() {
        Sitzplatz sitzplatz = getSitzplatzRandomSampleGenerator();
        Reservierung reservierungBack = getReservierungRandomSampleGenerator();

        sitzplatz.addReservierungen(reservierungBack);
        assertThat(sitzplatz.getReservierungens()).containsOnly(reservierungBack);
        assertThat(reservierungBack.getPlaetzes()).containsOnly(sitzplatz);

        sitzplatz.removeReservierungen(reservierungBack);
        assertThat(sitzplatz.getReservierungens()).doesNotContain(reservierungBack);
        assertThat(reservierungBack.getPlaetzes()).doesNotContain(sitzplatz);

        sitzplatz.reservierungens(new HashSet<>(Set.of(reservierungBack)));
        assertThat(sitzplatz.getReservierungens()).containsOnly(reservierungBack);
        assertThat(reservierungBack.getPlaetzes()).containsOnly(sitzplatz);

        sitzplatz.setReservierungens(new HashSet<>());
        assertThat(sitzplatz.getReservierungens()).doesNotContain(reservierungBack);
        assertThat(reservierungBack.getPlaetzes()).doesNotContain(sitzplatz);
    }
}
