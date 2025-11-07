package de.cinebuddy.repository;

import de.cinebuddy.domain.Sitzplatz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sitzplatz entity.
 */
@Repository
public interface SitzplatzRepository extends JpaRepository<Sitzplatz, Long> {
    default Optional<Sitzplatz> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sitzplatz> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sitzplatz> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select sitzplatz from Sitzplatz sitzplatz left join fetch sitzplatz.saal",
        countQuery = "select count(sitzplatz) from Sitzplatz sitzplatz"
    )
    Page<Sitzplatz> findAllWithToOneRelationships(Pageable pageable);

    @Query("select sitzplatz from Sitzplatz sitzplatz left join fetch sitzplatz.saal")
    List<Sitzplatz> findAllWithToOneRelationships();

    @Query("select sitzplatz from Sitzplatz sitzplatz left join fetch sitzplatz.saal where sitzplatz.id =:id")
    Optional<Sitzplatz> findOneWithToOneRelationships(@Param("id") Long id);
}
