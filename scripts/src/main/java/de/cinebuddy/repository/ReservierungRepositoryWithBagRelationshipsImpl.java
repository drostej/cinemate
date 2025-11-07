package de.cinebuddy.repository;

import de.cinebuddy.domain.Reservierung;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ReservierungRepositoryWithBagRelationshipsImpl implements ReservierungRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String RESERVIERUNGS_PARAMETER = "reservierungs";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reservierung> fetchBagRelationships(Optional<Reservierung> reservierung) {
        return reservierung.map(this::fetchPlaetzes);
    }

    @Override
    public Page<Reservierung> fetchBagRelationships(Page<Reservierung> reservierungs) {
        return new PageImpl<>(
            fetchBagRelationships(reservierungs.getContent()),
            reservierungs.getPageable(),
            reservierungs.getTotalElements()
        );
    }

    @Override
    public List<Reservierung> fetchBagRelationships(List<Reservierung> reservierungs) {
        return Optional.of(reservierungs).map(this::fetchPlaetzes).orElse(Collections.emptyList());
    }

    Reservierung fetchPlaetzes(Reservierung result) {
        return entityManager
            .createQuery(
                "select reservierung from Reservierung reservierung left join fetch reservierung.plaetzes where reservierung.id = :id",
                Reservierung.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Reservierung> fetchPlaetzes(List<Reservierung> reservierungs) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, reservierungs.size()).forEach(index -> order.put(reservierungs.get(index).getId(), index));
        List<Reservierung> result = entityManager
            .createQuery(
                "select reservierung from Reservierung reservierung left join fetch reservierung.plaetzes where reservierung in :reservierungs",
                Reservierung.class
            )
            .setParameter(RESERVIERUNGS_PARAMETER, reservierungs)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
