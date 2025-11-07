package de.cinebuddy.web.rest;

import de.cinebuddy.repository.SitzplatzRepository;
import de.cinebuddy.service.SitzplatzService;
import de.cinebuddy.service.dto.SitzplatzDTO;
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
 * REST controller for managing {@link de.cinebuddy.domain.Sitzplatz}.
 */
@RestController
@RequestMapping("/api/sitzplatzs")
public class SitzplatzResource {

    private static final Logger LOG = LoggerFactory.getLogger(SitzplatzResource.class);

    private static final String ENTITY_NAME = "sitzplatz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SitzplatzService sitzplatzService;

    private final SitzplatzRepository sitzplatzRepository;

    public SitzplatzResource(SitzplatzService sitzplatzService, SitzplatzRepository sitzplatzRepository) {
        this.sitzplatzService = sitzplatzService;
        this.sitzplatzRepository = sitzplatzRepository;
    }

    /**
     * {@code POST  /sitzplatzs} : Create a new sitzplatz.
     *
     * @param sitzplatzDTO the sitzplatzDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sitzplatzDTO, or with status {@code 400 (Bad Request)} if the sitzplatz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SitzplatzDTO> createSitzplatz(@Valid @RequestBody SitzplatzDTO sitzplatzDTO) throws URISyntaxException {
        LOG.debug("REST request to save Sitzplatz : {}", sitzplatzDTO);
        if (sitzplatzDTO.getId() != null) {
            throw new BadRequestAlertException("A new sitzplatz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sitzplatzDTO = sitzplatzService.save(sitzplatzDTO);
        return ResponseEntity.created(new URI("/api/sitzplatzs/" + sitzplatzDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sitzplatzDTO.getId().toString()))
            .body(sitzplatzDTO);
    }

    /**
     * {@code PUT  /sitzplatzs/:id} : Updates an existing sitzplatz.
     *
     * @param id the id of the sitzplatzDTO to save.
     * @param sitzplatzDTO the sitzplatzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sitzplatzDTO,
     * or with status {@code 400 (Bad Request)} if the sitzplatzDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sitzplatzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SitzplatzDTO> updateSitzplatz(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SitzplatzDTO sitzplatzDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Sitzplatz : {}, {}", id, sitzplatzDTO);
        if (sitzplatzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sitzplatzDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sitzplatzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sitzplatzDTO = sitzplatzService.update(sitzplatzDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sitzplatzDTO.getId().toString()))
            .body(sitzplatzDTO);
    }

    /**
     * {@code PATCH  /sitzplatzs/:id} : Partial updates given fields of an existing sitzplatz, field will ignore if it is null
     *
     * @param id the id of the sitzplatzDTO to save.
     * @param sitzplatzDTO the sitzplatzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sitzplatzDTO,
     * or with status {@code 400 (Bad Request)} if the sitzplatzDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sitzplatzDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sitzplatzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SitzplatzDTO> partialUpdateSitzplatz(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SitzplatzDTO sitzplatzDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Sitzplatz partially : {}, {}", id, sitzplatzDTO);
        if (sitzplatzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sitzplatzDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sitzplatzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SitzplatzDTO> result = sitzplatzService.partialUpdate(sitzplatzDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sitzplatzDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sitzplatzs} : get all the sitzplatzs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sitzplatzs in body.
     */
    @GetMapping("")
    public List<SitzplatzDTO> getAllSitzplatzs(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Sitzplatzs");
        return sitzplatzService.findAll();
    }

    /**
     * {@code GET  /sitzplatzs/:id} : get the "id" sitzplatz.
     *
     * @param id the id of the sitzplatzDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sitzplatzDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SitzplatzDTO> getSitzplatz(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Sitzplatz : {}", id);
        Optional<SitzplatzDTO> sitzplatzDTO = sitzplatzService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sitzplatzDTO);
    }

    /**
     * {@code DELETE  /sitzplatzs/:id} : delete the "id" sitzplatz.
     *
     * @param id the id of the sitzplatzDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSitzplatz(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Sitzplatz : {}", id);
        sitzplatzService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
