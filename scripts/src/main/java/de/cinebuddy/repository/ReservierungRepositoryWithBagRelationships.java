package de.cinebuddy.repository;

import de.cinebuddy.domain.Reservierung;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ReservierungRepositoryWithBagRelationships {
    Optional<Reservierung> fetchBagRelationships(Optional<Reservierung> reservierung);

    List<Reservierung> fetchBagRelationships(List<Reservierung> reservierungs);

    Page<Reservierung> fetchBagRelationships(Page<Reservierung> reservierungs);
}
