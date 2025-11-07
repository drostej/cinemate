package de.cinebuddy.web.rest;

import static de.cinebuddy.domain.VorfuehrungAsserts.*;
import static de.cinebuddy.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cinebuddy.IntegrationTest;
import de.cinebuddy.domain.Vorfuehrung;
import de.cinebuddy.repository.VorfuehrungRepository;
import de.cinebuddy.service.VorfuehrungService;
import de.cinebuddy.service.dto.VorfuehrungDTO;
import de.cinebuddy.service.mapper.VorfuehrungMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VorfuehrungResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VorfuehrungResourceIT {

    private static final String DEFAULT_FILM_TITEL = "AAAAAAAAAA";
    private static final String UPDATED_FILM_TITEL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATUM_ZEIT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATUM_ZEIT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vorfuehrungs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VorfuehrungRepository vorfuehrungRepository;

    @Mock
    private VorfuehrungRepository vorfuehrungRepositoryMock;

    @Autowired
    private VorfuehrungMapper vorfuehrungMapper;

    @Mock
    private VorfuehrungService vorfuehrungServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVorfuehrungMockMvc;

    private Vorfuehrung vorfuehrung;

    private Vorfuehrung insertedVorfuehrung;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vorfuehrung createEntity() {
        return new Vorfuehrung().filmTitel(DEFAULT_FILM_TITEL).datumZeit(DEFAULT_DATUM_ZEIT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vorfuehrung createUpdatedEntity() {
        return new Vorfuehrung().filmTitel(UPDATED_FILM_TITEL).datumZeit(UPDATED_DATUM_ZEIT);
    }

    @BeforeEach
    void initTest() {
        vorfuehrung = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVorfuehrung != null) {
            vorfuehrungRepository.delete(insertedVorfuehrung);
            insertedVorfuehrung = null;
        }
    }

    @Test
    @Transactional
    void createVorfuehrung() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);
        var returnedVorfuehrungDTO = om.readValue(
            restVorfuehrungMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vorfuehrungDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VorfuehrungDTO.class
        );

        // Validate the Vorfuehrung in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVorfuehrung = vorfuehrungMapper.toEntity(returnedVorfuehrungDTO);
        assertVorfuehrungUpdatableFieldsEquals(returnedVorfuehrung, getPersistedVorfuehrung(returnedVorfuehrung));

        insertedVorfuehrung = returnedVorfuehrung;
    }

    @Test
    @Transactional
    void createVorfuehrungWithExistingId() throws Exception {
        // Create the Vorfuehrung with an existing ID
        vorfuehrung.setId(1L);
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVorfuehrungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vorfuehrungDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFilmTitelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vorfuehrung.setFilmTitel(null);

        // Create the Vorfuehrung, which fails.
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        restVorfuehrungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vorfuehrungDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatumZeitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vorfuehrung.setDatumZeit(null);

        // Create the Vorfuehrung, which fails.
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        restVorfuehrungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vorfuehrungDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVorfuehrungs() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        // Get all the vorfuehrungList
        restVorfuehrungMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vorfuehrung.getId().intValue())))
            .andExpect(jsonPath("$.[*].filmTitel").value(hasItem(DEFAULT_FILM_TITEL)))
            .andExpect(jsonPath("$.[*].datumZeit").value(hasItem(DEFAULT_DATUM_ZEIT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVorfuehrungsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vorfuehrungServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVorfuehrungMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vorfuehrungServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVorfuehrungsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vorfuehrungServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVorfuehrungMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vorfuehrungRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVorfuehrung() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        // Get the vorfuehrung
        restVorfuehrungMockMvc
            .perform(get(ENTITY_API_URL_ID, vorfuehrung.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vorfuehrung.getId().intValue()))
            .andExpect(jsonPath("$.filmTitel").value(DEFAULT_FILM_TITEL))
            .andExpect(jsonPath("$.datumZeit").value(DEFAULT_DATUM_ZEIT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVorfuehrung() throws Exception {
        // Get the vorfuehrung
        restVorfuehrungMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVorfuehrung() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vorfuehrung
        Vorfuehrung updatedVorfuehrung = vorfuehrungRepository.findById(vorfuehrung.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVorfuehrung are not directly saved in db
        em.detach(updatedVorfuehrung);
        updatedVorfuehrung.filmTitel(UPDATED_FILM_TITEL).datumZeit(UPDATED_DATUM_ZEIT);
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(updatedVorfuehrung);

        restVorfuehrungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vorfuehrungDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vorfuehrungDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVorfuehrungToMatchAllProperties(updatedVorfuehrung);
    }

    @Test
    @Transactional
    void putNonExistingVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vorfuehrungDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vorfuehrungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vorfuehrungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vorfuehrungDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVorfuehrungWithPatch() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vorfuehrung using partial update
        Vorfuehrung partialUpdatedVorfuehrung = new Vorfuehrung();
        partialUpdatedVorfuehrung.setId(vorfuehrung.getId());

        restVorfuehrungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVorfuehrung.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVorfuehrung))
            )
            .andExpect(status().isOk());

        // Validate the Vorfuehrung in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVorfuehrungUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVorfuehrung, vorfuehrung),
            getPersistedVorfuehrung(vorfuehrung)
        );
    }

    @Test
    @Transactional
    void fullUpdateVorfuehrungWithPatch() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vorfuehrung using partial update
        Vorfuehrung partialUpdatedVorfuehrung = new Vorfuehrung();
        partialUpdatedVorfuehrung.setId(vorfuehrung.getId());

        partialUpdatedVorfuehrung.filmTitel(UPDATED_FILM_TITEL).datumZeit(UPDATED_DATUM_ZEIT);

        restVorfuehrungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVorfuehrung.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVorfuehrung))
            )
            .andExpect(status().isOk());

        // Validate the Vorfuehrung in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVorfuehrungUpdatableFieldsEquals(partialUpdatedVorfuehrung, getPersistedVorfuehrung(partialUpdatedVorfuehrung));
    }

    @Test
    @Transactional
    void patchNonExistingVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vorfuehrungDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vorfuehrungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vorfuehrungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVorfuehrung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vorfuehrung.setId(longCount.incrementAndGet());

        // Create the Vorfuehrung
        VorfuehrungDTO vorfuehrungDTO = vorfuehrungMapper.toDto(vorfuehrung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVorfuehrungMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vorfuehrungDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vorfuehrung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVorfuehrung() throws Exception {
        // Initialize the database
        insertedVorfuehrung = vorfuehrungRepository.saveAndFlush(vorfuehrung);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vorfuehrung
        restVorfuehrungMockMvc
            .perform(delete(ENTITY_API_URL_ID, vorfuehrung.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vorfuehrungRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Vorfuehrung getPersistedVorfuehrung(Vorfuehrung vorfuehrung) {
        return vorfuehrungRepository.findById(vorfuehrung.getId()).orElseThrow();
    }

    protected void assertPersistedVorfuehrungToMatchAllProperties(Vorfuehrung expectedVorfuehrung) {
        assertVorfuehrungAllPropertiesEquals(expectedVorfuehrung, getPersistedVorfuehrung(expectedVorfuehrung));
    }

    protected void assertPersistedVorfuehrungToMatchUpdatableProperties(Vorfuehrung expectedVorfuehrung) {
        assertVorfuehrungAllUpdatablePropertiesEquals(expectedVorfuehrung, getPersistedVorfuehrung(expectedVorfuehrung));
    }
}
