package de.cinebuddy.repository;

import de.cinebuddy.domain.Vorfuehrung;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vorfuehrung entity.
 */
@Repository
public interface VorfuehrungRepository extends JpaRepository<Vorfuehrung, Long> {
    default Optional<Vorfuehrung> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vorfuehrung> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vorfuehrung> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vorfuehrung from Vorfuehrung vorfuehrung left join fetch vorfuehrung.saal",
        countQuery = "select count(vorfuehrung) from Vorfuehrung vorfuehrung"
    )
    Page<Vorfuehrung> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vorfuehrung from Vorfuehrung vorfuehrung left join fetch vorfuehrung.saal")
    List<Vorfuehrung> findAllWithToOneRelationships();

    @Query("select vorfuehrung from Vorfuehrung vorfuehrung left join fetch vorfuehrung.saal where vorfuehrung.id =:id")
    Optional<Vorfuehrung> findOneWithToOneRelationships(@Param("id") Long id);
}
