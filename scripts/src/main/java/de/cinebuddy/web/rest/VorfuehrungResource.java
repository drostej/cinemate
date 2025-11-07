package de.cinebuddy.web.rest;

import de.cinebuddy.repository.VorfuehrungRepository;
import de.cinebuddy.service.VorfuehrungService;
import de.cinebuddy.service.dto.VorfuehrungDTO;
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
 * REST controller for managing {@link de.cinebuddy.domain.Vorfuehrung}.
 */
@RestController
@RequestMapping("/api/vorfuehrungs")
public class VorfuehrungResource {

    private static final Logger LOG = LoggerFactory.getLogger(VorfuehrungResource.class);

    private static final String ENTITY_NAME = "vorfuehrung";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VorfuehrungService vorfuehrungService;

    private final VorfuehrungRepository vorfuehrungRepository;

    public VorfuehrungResource(VorfuehrungService vorfuehrungService, VorfuehrungRepository vorfuehrungRepository) {
        this.vorfuehrungService = vorfuehrungService;
        this.vorfuehrungRepository = vorfuehrungRepository;
    }

    /**
     * {@code POST  /vorfuehrungs} : Create a new vorfuehrung.
     *
     * @param vorfuehrungDTO the vorfuehrungDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vorfuehrungDTO, or with status {@code 400 (Bad Request)} if the vorfuehrung has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VorfuehrungDTO> createVorfuehrung(@Valid @RequestBody VorfuehrungDTO vorfuehrungDTO) throws URISyntaxException {
        LOG.debug("REST request to save Vorfuehrung : {}", vorfuehrungDTO);
        if (vorfuehrungDTO.getId() != null) {
            throw new BadRequestAlertException("A new vorfuehrung cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vorfuehrungDTO = vorfuehrungService.save(vorfuehrungDTO);
        return ResponseEntity.created(new URI("/api/vorfuehrungs/" + vorfuehrungDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vorfuehrungDTO.getId().toString()))
            .body(vorfuehrungDTO);
    }

    /**
     * {@code PUT  /vorfuehrungs/:id} : Updates an existing vorfuehrung.
     *
     * @param id the id of the vorfuehrungDTO to save.
     * @param vorfuehrungDTO the vorfuehrungDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vorfuehrungDTO,
     * or with status {@code 400 (Bad Request)} if the vorfuehrungDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vorfuehrungDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VorfuehrungDTO> updateVorfuehrung(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VorfuehrungDTO vorfuehrungDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Vorfuehrung : {}, {}", id, vorfuehrungDTO);
        if (vorfuehrungDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vorfuehrungDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vorfuehrungRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vorfuehrungDTO = vorfuehrungService.update(vorfuehrungDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vorfuehrungDTO.getId().toString()))
            .body(vorfuehrungDTO);
    }

    /**
     * {@code PATCH  /vorfuehrungs/:id} : Partial updates given fields of an existing vorfuehrung, field will ignore if it is null
     *
     * @param id the id of the vorfuehrungDTO to save.
     * @param vorfuehrungDTO the vorfuehrungDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vorfuehrungDTO,
     * or with status {@code 400 (Bad Request)} if the vorfuehrungDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vorfuehrungDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vorfuehrungDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VorfuehrungDTO> partialUpdateVorfuehrung(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VorfuehrungDTO vorfuehrungDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Vorfuehrung partially : {}, {}", id, vorfuehrungDTO);
        if (vorfuehrungDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vorfuehrungDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vorfuehrungRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VorfuehrungDTO> result = vorfuehrungService.partialUpdate(vorfuehrungDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vorfuehrungDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vorfuehrungs} : get all the vorfuehrungs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vorfuehrungs in body.
     */
    @GetMapping("")
    public List<VorfuehrungDTO> getAllVorfuehrungs(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Vorfuehrungs");
        return vorfuehrungService.findAll();
    }

    /**
     * {@code GET  /vorfuehrungs/:id} : get the "id" vorfuehrung.
     *
     * @param id the id of the vorfuehrungDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vorfuehrungDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VorfuehrungDTO> getVorfuehrung(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vorfuehrung : {}", id);
        Optional<VorfuehrungDTO> vorfuehrungDTO = vorfuehrungService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vorfuehrungDTO);
    }

    /**
     * {@code DELETE  /vorfuehrungs/:id} : delete the "id" vorfuehrung.
     *
     * @param id the id of the vorfuehrungDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVorfuehrung(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vorfuehrung : {}", id);
        vorfuehrungService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
