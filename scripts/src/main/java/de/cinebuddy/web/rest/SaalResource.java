package de.cinebuddy.web.rest;

import de.cinebuddy.repository.SaalRepository;
import de.cinebuddy.service.SaalService;
import de.cinebuddy.service.dto.SaalDTO;
import de.cinebuddy.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.cinebuddy.domain.Saal}.
 */
@RestController
@RequestMapping("/api/saals")
public class SaalResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaalResource.class);

    private static final String ENTITY_NAME = "saal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaalService saalService;

    private final SaalRepository saalRepository;

    public SaalResource(SaalService saalService, SaalRepository saalRepository) {
        this.saalService = saalService;
        this.saalRepository = saalRepository;
    }

    /**
     * {@code POST  /saals} : Create a new saal.
     *
     * @param saalDTO the saalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saalDTO, or with status {@code 400 (Bad Request)} if the saal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaalDTO> createSaal(@Valid @RequestBody SaalDTO saalDTO) throws URISyntaxException {
        LOG.debug("REST request to save Saal : {}", saalDTO);
        if (saalDTO.getId() != null) {
            throw new BadRequestAlertException("A new saal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saalDTO = saalService.save(saalDTO);
        return ResponseEntity.created(new URI("/api/saals/" + saalDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saalDTO.getId().toString()))
            .body(saalDTO);
    }

    /**
     * {@code PUT  /saals/:id} : Updates an existing saal.
     *
     * @param id the id of the saalDTO to save.
     * @param saalDTO the saalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saalDTO,
     * or with status {@code 400 (Bad Request)} if the saalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaalDTO> updateSaal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaalDTO saalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Saal : {}, {}", id, saalDTO);
        if (saalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saalDTO = saalService.update(saalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saalDTO.getId().toString()))
            .body(saalDTO);
    }

    /**
     * {@code PATCH  /saals/:id} : Partial updates given fields of an existing saal, field will ignore if it is null
     *
     * @param id the id of the saalDTO to save.
     * @param saalDTO the saalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saalDTO,
     * or with status {@code 400 (Bad Request)} if the saalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaalDTO> partialUpdateSaal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaalDTO saalDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Saal partially : {}, {}", id, saalDTO);
        if (saalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaalDTO> result = saalService.partialUpdate(saalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saals} : get all the saals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saals in body.
     */
    @GetMapping("")
    public List<SaalDTO> getAllSaals() {
        LOG.debug("REST request to get all Saals");
        return saalService.findAll();
    }

    /**
     * {@code GET  /saals/:id} : get the "id" saal.
     *
     * @param id the id of the saalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaalDTO> getSaal(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Saal : {}", id);
        Optional<SaalDTO> saalDTO = saalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saalDTO);
    }

    /**
     * {@code DELETE  /saals/:id} : delete the "id" saal.
     *
     * @param id the id of the saalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaal(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Saal : {}", id);
        saalService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
