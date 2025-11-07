package de.cinebuddy.web.rest;

import de.cinebuddy.repository.ReservierungRepository;
import de.cinebuddy.service.ReservierungService;
import de.cinebuddy.service.dto.ReservierungDTO;
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
 * REST controller for managing {@link de.cinebuddy.domain.Reservierung}.
 */
@RestController
@RequestMapping("/api/reservierungs")
public class ReservierungResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReservierungResource.class);

    private static final String ENTITY_NAME = "reservierung";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservierungService reservierungService;

    private final ReservierungRepository reservierungRepository;

    public ReservierungResource(ReservierungService reservierungService, ReservierungRepository reservierungRepository) {
        this.reservierungService = reservierungService;
        this.reservierungRepository = reservierungRepository;
    }

    /**
     * {@code POST  /reservierungs} : Create a new reservierung.
     *
     * @param reservierungDTO the reservierungDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservierungDTO, or with status {@code 400 (Bad Request)} if the reservierung has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReservierungDTO> createReservierung(@Valid @RequestBody ReservierungDTO reservierungDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Reservierung : {}", reservierungDTO);
        if (reservierungDTO.getId() != null) {
            throw new BadRequestAlertException("A new reservierung cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reservierungDTO = reservierungService.save(reservierungDTO);
        return ResponseEntity.created(new URI("/api/reservierungs/" + reservierungDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reservierungDTO.getId().toString()))
            .body(reservierungDTO);
    }

    /**
     * {@code PUT  /reservierungs/:id} : Updates an existing reservierung.
     *
     * @param id the id of the reservierungDTO to save.
     * @param reservierungDTO the reservierungDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservierungDTO,
     * or with status {@code 400 (Bad Request)} if the reservierungDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservierungDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservierungDTO> updateReservierung(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservierungDTO reservierungDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Reservierung : {}, {}", id, reservierungDTO);
        if (reservierungDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservierungDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservierungRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reservierungDTO = reservierungService.update(reservierungDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservierungDTO.getId().toString()))
            .body(reservierungDTO);
    }

    /**
     * {@code PATCH  /reservierungs/:id} : Partial updates given fields of an existing reservierung, field will ignore if it is null
     *
     * @param id the id of the reservierungDTO to save.
     * @param reservierungDTO the reservierungDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservierungDTO,
     * or with status {@code 400 (Bad Request)} if the reservierungDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reservierungDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservierungDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservierungDTO> partialUpdateReservierung(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservierungDTO reservierungDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Reservierung partially : {}, {}", id, reservierungDTO);
        if (reservierungDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservierungDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservierungRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservierungDTO> result = reservierungService.partialUpdate(reservierungDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservierungDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reservierungs} : get all the reservierungs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservierungs in body.
     */
    @GetMapping("")
    public List<ReservierungDTO> getAllReservierungs(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Reservierungs");
        return reservierungService.findAll();
    }

    /**
     * {@code GET  /reservierungs/:id} : get the "id" reservierung.
     *
     * @param id the id of the reservierungDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservierungDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservierungDTO> getReservierung(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Reservierung : {}", id);
        Optional<ReservierungDTO> reservierungDTO = reservierungService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservierungDTO);
    }

    /**
     * {@code DELETE  /reservierungs/:id} : delete the "id" reservierung.
     *
     * @param id the id of the reservierungDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservierung(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Reservierung : {}", id);
        reservierungService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
