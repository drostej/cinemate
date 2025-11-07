package de.cinebuddy.repository;

import de.cinebuddy.domain.Saal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Saal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SaalRepository extends JpaRepository<Saal, Long> {}
