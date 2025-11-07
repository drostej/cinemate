package de.cinebuddy.service;

import de.cinebuddy.domain.Sitzplatz;
import de.cinebuddy.repository.SitzplatzRepository;
import de.cinebuddy.service.dto.SitzplatzDTO;
import de.cinebuddy.service.mapper.SitzplatzMapper;
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
 * Service Implementation for managing {@link de.cinebuddy.domain.Sitzplatz}.
 */
@Service
@Transactional
public class SitzplatzService {

    private static final Logger LOG = LoggerFactory.getLogger(SitzplatzService.class);

    private final SitzplatzRepository sitzplatzRepository;

    private final SitzplatzMapper sitzplatzMapper;

    public SitzplatzService(SitzplatzRepository sitzplatzRepository, SitzplatzMapper sitzplatzMapper) {
        this.sitzplatzRepository = sitzplatzRepository;
        this.sitzplatzMapper = sitzplatzMapper;
    }

    /**
     * Save a sitzplatz.
     *
     * @param sitzplatzDTO the entity to save.
     * @return the persisted entity.
     */
    public SitzplatzDTO save(SitzplatzDTO sitzplatzDTO) {
        LOG.debug("Request to save Sitzplatz : {}", sitzplatzDTO);
        Sitzplatz sitzplatz = sitzplatzMapper.toEntity(sitzplatzDTO);
        sitzplatz = sitzplatzRepository.save(sitzplatz);
        return sitzplatzMapper.toDto(sitzplatz);
    }

    /**
     * Update a sitzplatz.
     *
     * @param sitzplatzDTO the entity to save.
     * @return the persisted entity.
     */
    public SitzplatzDTO update(SitzplatzDTO sitzplatzDTO) {
        LOG.debug("Request to update Sitzplatz : {}", sitzplatzDTO);
        Sitzplatz sitzplatz = sitzplatzMapper.toEntity(sitzplatzDTO);
        sitzplatz = sitzplatzRepository.save(sitzplatz);
        return sitzplatzMapper.toDto(sitzplatz);
    }

    /**
     * Partially update a sitzplatz.
     *
     * @param sitzplatzDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SitzplatzDTO> partialUpdate(SitzplatzDTO sitzplatzDTO) {
        LOG.debug("Request to partially update Sitzplatz : {}", sitzplatzDTO);

        return sitzplatzRepository
            .findById(sitzplatzDTO.getId())
            .map(existingSitzplatz -> {
                sitzplatzMapper.partialUpdate(existingSitzplatz, sitzplatzDTO);

                return existingSitzplatz;
            })
            .map(sitzplatzRepository::save)
            .map(sitzplatzMapper::toDto);
    }

    /**
     * Get all the sitzplatzs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SitzplatzDTO> findAll() {
        LOG.debug("Request to get all Sitzplatzs");
        return sitzplatzRepository.findAll().stream().map(sitzplatzMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the sitzplatzs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SitzplatzDTO> findAllWithEagerRelationships(Pageable pageable) {
        return sitzplatzRepository.findAllWithEagerRelationships(pageable).map(sitzplatzMapper::toDto);
    }

    /**
     * Get one sitzplatz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SitzplatzDTO> findOne(Long id) {
        LOG.debug("Request to get Sitzplatz : {}", id);
        return sitzplatzRepository.findOneWithEagerRelationships(id).map(sitzplatzMapper::toDto);
    }

    /**
     * Delete the sitzplatz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sitzplatz : {}", id);
        sitzplatzRepository.deleteById(id);
    }
}
