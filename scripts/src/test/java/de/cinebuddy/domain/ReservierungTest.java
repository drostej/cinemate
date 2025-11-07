package de.cinebuddy.domain;

import static de.cinebuddy.domain.ReservierungTestSamples.*;
import static de.cinebuddy.domain.SitzplatzTestSamples.*;
import static de.cinebuddy.domain.VorfuehrungTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import de.cinebuddy.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReservierungTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservierung.class);
        Reservierung reservierung1 = getReservierungSample1();
        Reservierung reservierung2 = new Reservierung();
        assertThat(reservierung1).isNotEqualTo(reservierung2);

        reservierung2.setId(reservierung1.getId());
        assertThat(reservierung1).isEqualTo(reservierung2);

        reservierung2 = getReservierungSample2();
        assertThat(reservierung1).isNotEqualTo(reservierung2);
    }

    @Test
    void plaetzeTest() {
        Reservierung reservierung = getReservierungRandomSampleGenerator();
        Sitzplatz sitzplatzBack = getSitzplatzRandomSampleGenerator();

        reservierung.addPlaetze(sitzplatzBack);
        assertThat(reservierung.getPlaetzes()).containsOnly(sitzplatzBack);

        reservierung.removePlaetze(sitzplatzBack);
        assertThat(reservierung.getPlaetzes()).doesNotContain(sitzplatzBack);

        reservierung.plaetzes(new HashSet<>(Set.of(sitzplatzBack)));
        assertThat(reservierung.getPlaetzes()).containsOnly(sitzplatzBack);

        reservierung.setPlaetzes(new HashSet<>());
        assertThat(reservierung.getPlaetzes()).doesNotContain(sitzplatzBack);
    }

    @Test
    void vorfuehrungTest() {
        Reservierung reservierung = getReservierungRandomSampleGenerator();
        Vorfuehrung vorfuehrungBack = getVorfuehrungRandomSampleGenerator();

        reservierung.setVorfuehrung(vorfuehrungBack);
        assertThat(reservierung.getVorfuehrung()).isEqualTo(vorfuehrungBack);

        reservierung.vorfuehrung(null);
        assertThat(reservierung.getVorfuehrung()).isNull();
    }
}
