package de.cinebuddy.repository;

import de.cinebuddy.domain.Reservierung;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Reservierung entity.
 *
 * When extending this class, extend ReservierungRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ReservierungRepository extends ReservierungRepositoryWithBagRelationships, JpaRepository<Reservierung, Long> {
    default Optional<Reservierung> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Reservierung> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Reservierung> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select reservierung from Reservierung reservierung left join fetch reservierung.vorfuehrung",
        countQuery = "select count(reservierung) from Reservierung reservierung"
    )
    Page<Reservierung> findAllWithToOneRelationships(Pageable pageable);

    @Query("select reservierung from Reservierung reservierung left join fetch reservierung.vorfuehrung")
    List<Reservierung> findAllWithToOneRelationships();

    @Query("select reservierung from Reservierung reservierung left join fetch reservierung.vorfuehrung where reservierung.id =:id")
    Optional<Reservierung> findOneWithToOneRelationships(@Param("id") Long id);
}
