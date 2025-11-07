package de.cinebuddy.service;

import de.cinebuddy.domain.Vorfuehrung;
import de.cinebuddy.repository.VorfuehrungRepository;
import de.cinebuddy.service.dto.VorfuehrungDTO;
import de.cinebuddy.service.mapper.VorfuehrungMapper;
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
 * Service Implementation for managing {@link de.cinebuddy.domain.Vorfuehrung}.
 */
@Service
@Transactional
public class VorfuehrungService {

    private static final Logger LOG = LoggerFactory.getLogger(VorfuehrungService.class);

    private final VorfuehrungRepository vorfuehrungRepository;

    private final VorfuehrungMapper vorfuehrungMapper;

    public VorfuehrungService(VorfuehrungRepository vorfuehrungRepository, VorfuehrungMapper vorfuehrungMapper) {
        this.vorfuehrungRepository = vorfuehrungRepository;
        this.vorfuehrungMapper = vorfuehrungMapper;
    }

    /**
     * Save a vorfuehrung.
     *
     * @param vorfuehrungDTO the entity to save.
     * @return the persisted entity.
     */
    public VorfuehrungDTO save(VorfuehrungDTO vorfuehrungDTO) {
        LOG.debug("Request to save Vorfuehrung : {}", vorfuehrungDTO);
        Vorfuehrung vorfuehrung = vorfuehrungMapper.toEntity(vorfuehrungDTO);
        vorfuehrung = vorfuehrungRepository.save(vorfuehrung);
        return vorfuehrungMapper.toDto(vorfuehrung);
    }

    /**
     * Update a vorfuehrung.
     *
     * @param vorfuehrungDTO the entity to save.
     * @return the persisted entity.
     */
    public VorfuehrungDTO update(VorfuehrungDTO vorfuehrungDTO) {
        LOG.debug("Request to update Vorfuehrung : {}", vorfuehrungDTO);
        Vorfuehrung vorfuehrung = vorfuehrungMapper.toEntity(vorfuehrungDTO);
        vorfuehrung = vorfuehrungRepository.save(vorfuehrung);
        return vorfuehrungMapper.toDto(vorfuehrung);
    }

    /**
     * Partially update a vorfuehrung.
     *
     * @param vorfuehrungDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VorfuehrungDTO> partialUpdate(VorfuehrungDTO vorfuehrungDTO) {
        LOG.debug("Request to partially update Vorfuehrung : {}", vorfuehrungDTO);

        return vorfuehrungRepository
            .findById(vorfuehrungDTO.getId())
            .map(existingVorfuehrung -> {
                vorfuehrungMapper.partialUpdate(existingVorfuehrung, vorfuehrungDTO);

                return existingVorfuehrung;
            })
            .map(vorfuehrungRepository::save)
            .map(vorfuehrungMapper::toDto);
    }

    /**
     * Get all the vorfuehrungs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VorfuehrungDTO> findAll() {
        LOG.debug("Request to get all Vorfuehrungs");
        return vorfuehrungRepository.findAll().stream().map(vorfuehrungMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the vorfuehrungs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<VorfuehrungDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vorfuehrungRepository.findAllWithEagerRelationships(pageable).map(vorfuehrungMapper::toDto);
    }

    /**
     * Get one vorfuehrung by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VorfuehrungDTO> findOne(Long id) {
        LOG.debug("Request to get Vorfuehrung : {}", id);
        return vorfuehrungRepository.findOneWithEagerRelationships(id).map(vorfuehrungMapper::toDto);
    }

    /**
     * Delete the vorfuehrung by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vorfuehrung : {}", id);
        vorfuehrungRepository.deleteById(id);
    }
}
