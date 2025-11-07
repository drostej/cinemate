package de.cinebuddy.service;

import de.cinebuddy.domain.Saal;
import de.cinebuddy.repository.SaalRepository;
import de.cinebuddy.service.dto.SaalDTO;
import de.cinebuddy.service.mapper.SaalMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link de.cinebuddy.domain.Saal}.
 */
@Service
@Transactional
public class SaalService {

    private static final Logger LOG = LoggerFactory.getLogger(SaalService.class);

    private final SaalRepository saalRepository;

    private final SaalMapper saalMapper;

    public SaalService(SaalRepository saalRepository, SaalMapper saalMapper) {
        this.saalRepository = saalRepository;
        this.saalMapper = saalMapper;
    }

    /**
     * Save a saal.
     *
     * @param saalDTO the entity to save.
     * @return the persisted entity.
     */
    public SaalDTO save(SaalDTO saalDTO) {
        LOG.debug("Request to save Saal : {}", saalDTO);
        Saal saal = saalMapper.toEntity(saalDTO);
        saal = saalRepository.save(saal);
        return saalMapper.toDto(saal);
    }

    /**
     * Update a saal.
     *
     * @param saalDTO the entity to save.
     * @return the persisted entity.
     */
    public SaalDTO update(SaalDTO saalDTO) {
        LOG.debug("Request to update Saal : {}", saalDTO);
        Saal saal = saalMapper.toEntity(saalDTO);
        saal = saalRepository.save(saal);
        return saalMapper.toDto(saal);
    }

    /**
     * Partially update a saal.
     *
     * @param saalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaalDTO> partialUpdate(SaalDTO saalDTO) {
        LOG.debug("Request to partially update Saal : {}", saalDTO);

        return saalRepository
            .findById(saalDTO.getId())
            .map(existingSaal -> {
                saalMapper.partialUpdate(existingSaal, saalDTO);

                return existingSaal;
            })
            .map(saalRepository::save)
            .map(saalMapper::toDto);
    }

    /**
     * Get all the saals.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SaalDTO> findAll() {
        LOG.debug("Request to get all Saals");
        return saalRepository.findAll().stream().map(saalMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one saal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaalDTO> findOne(Long id) {
        LOG.debug("Request to get Saal : {}", id);
        return saalRepository.findById(id).map(saalMapper::toDto);
    }

    /**
     * Delete the saal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Saal : {}", id);
        saalRepository.deleteById(id);
    }
}
