package de.cinebuddy.service;

import de.cinebuddy.domain.Reservierung;
import de.cinebuddy.repository.ReservierungRepository;
import de.cinebuddy.service.dto.ReservierungDTO;
import de.cinebuddy.service.mapper.ReservierungMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.cinebuddy.domain.Reservierung}.
 */
@Service
@Transactional
public class ReservierungService {

    private static final Logger LOG = LoggerFactory.getLogger(ReservierungService.class);

    private final ReservierungRepository reservierungRepository;

    private final ReservierungMapper reservierungMapper;

    public ReservierungService(ReservierungRepository reservierungRepository, ReservierungMapper reservierungMapper) {
        this.reservierungRepository = reservierungRepository;
        this.reservierungMapper = reservierungMapper;
    }

    /**
     * Save a reservierung.
     *
     * @param reservierungDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservierungDTO save(ReservierungDTO reservierungDTO) {
        LOG.debug("Request to save Reservierung : {}", reservierungDTO);
        Reservierung reservierung = reservierungMapper.toEntity(reservierungDTO);
        reservierung = reservierungRepository.save(reservierung);
        return reservierungMapper.toDto(reservierung);
    }

    /**
     * Update a reservierung.
     *
     * @param reservierungDTO the entity to save.
     * @return the persisted entity.
     */
    public ReservierungDTO update(ReservierungDTO reservierungDTO) {
        LOG.debug("Request to update Reservierung : {}", reservierungDTO);
        Reservierung reservierung = reservierungMapper.toEntity(reservierungDTO);
        reservierung = reservierungRepository.save(reservierung);
        return reservierungMapper.toDto(reservierung);
    }

    /**
     * Partially update a reservierung.
     *
     * @param reservierungDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReservierungDTO> partialUpdate(ReservierungDTO reservierungDTO) {
        LOG.debug("Request to partially update Reservierung : {}", reservierungDTO);

        return reservierungRepository
            .findById(reservierungDTO.getId())
            .map(existingReservierung -> {
                reservierungMapper.partialUpdate(existingReservierung, reservierungDTO);

                return existingReservierung;
            })
            .map(reservierungRepository::save)
            .map(reservierungMapper::toDto);
    }

    /**
     * Get all the reservierungs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReservierungDTO> findAll() {
        LOG.debug("Request to get all Reservierungs");
        return reservierungRepository.findAll().stream().map(reservierungMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the reservierungs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReservierungDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reservierungRepository.findAllWithEagerRelationships(pageable).map(reservierungMapper::toDto);
    }

    /**
     * Get one reservierung by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReservierungDTO> findOne(Long id) {
        LOG.debug("Request to get Reservierung : {}", id);
        return reservierungRepository.findOneWithEagerRelationships(id).map(reservierungMapper::toDto);
    }

    /**
     * Delete the reservierung by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reservierung : {}", id);
        reservierungRepository.deleteById(id);
    }
}
