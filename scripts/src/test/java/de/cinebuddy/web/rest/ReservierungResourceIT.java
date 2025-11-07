package de.cinebuddy.web.rest;

import static de.cinebuddy.domain.ReservierungAsserts.*;
import static de.cinebuddy.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cinebuddy.IntegrationTest;
import de.cinebuddy.domain.Reservierung;
import de.cinebuddy.domain.enumeration.ReservierungsStatus;
import de.cinebuddy.repository.ReservierungRepository;
import de.cinebuddy.service.ReservierungService;
import de.cinebuddy.service.dto.ReservierungDTO;
import de.cinebuddy.service.mapper.ReservierungMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ReservierungResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReservierungResourceIT {

    private static final String DEFAULT_KUNDE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_KUNDE_NAME = "BBBBBBBBBB";

    private static final ReservierungsStatus DEFAULT_STATUS = ReservierungsStatus.OFFEN;
    private static final ReservierungsStatus UPDATED_STATUS = ReservierungsStatus.BEZAHLT;

    private static final String ENTITY_API_URL = "/api/reservierungs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReservierungRepository reservierungRepository;

    @Mock
    private ReservierungRepository reservierungRepositoryMock;

    @Autowired
    private ReservierungMapper reservierungMapper;

    @Mock
    private ReservierungService reservierungServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservierungMockMvc;

    private Reservierung reservierung;

    private Reservierung insertedReservierung;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservierung createEntity() {
        return new Reservierung().kundeName(DEFAULT_KUNDE_NAME).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservierung createUpdatedEntity() {
        return new Reservierung().kundeName(UPDATED_KUNDE_NAME).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        reservierung = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReservierung != null) {
            reservierungRepository.delete(insertedReservierung);
            insertedReservierung = null;
        }
    }

    @Test
    @Transactional
    void createReservierung() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);
        var returnedReservierungDTO = om.readValue(
            restReservierungMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservierungDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReservierungDTO.class
        );

        // Validate the Reservierung in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReservierung = reservierungMapper.toEntity(returnedReservierungDTO);
        assertReservierungUpdatableFieldsEquals(returnedReservierung, getPersistedReservierung(returnedReservierung));

        insertedReservierung = returnedReservierung;
    }

    @Test
    @Transactional
    void createReservierungWithExistingId() throws Exception {
        // Create the Reservierung with an existing ID
        reservierung.setId(1L);
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservierungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservierungDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKundeNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservierung.setKundeName(null);

        // Create the Reservierung, which fails.
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        restReservierungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservierungDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reservierung.setStatus(null);

        // Create the Reservierung, which fails.
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        restReservierungMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservierungDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservierungs() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        // Get all the reservierungList
        restReservierungMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservierung.getId().intValue())))
            .andExpect(jsonPath("$.[*].kundeName").value(hasItem(DEFAULT_KUNDE_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservierungsWithEagerRelationshipsIsEnabled() throws Exception {
        when(reservierungServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservierungMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(reservierungServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReservierungsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(reservierungServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReservierungMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(reservierungRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getReservierung() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        // Get the reservierung
        restReservierungMockMvc
            .perform(get(ENTITY_API_URL_ID, reservierung.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservierung.getId().intValue()))
            .andExpect(jsonPath("$.kundeName").value(DEFAULT_KUNDE_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReservierung() throws Exception {
        // Get the reservierung
        restReservierungMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservierung() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservierung
        Reservierung updatedReservierung = reservierungRepository.findById(reservierung.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReservierung are not directly saved in db
        em.detach(updatedReservierung);
        updatedReservierung.kundeName(UPDATED_KUNDE_NAME).status(UPDATED_STATUS);
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(updatedReservierung);

        restReservierungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservierungDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservierungDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReservierungToMatchAllProperties(updatedReservierung);
    }

    @Test
    @Transactional
    void putNonExistingReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservierungDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservierungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reservierungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reservierungDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservierungWithPatch() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservierung using partial update
        Reservierung partialUpdatedReservierung = new Reservierung();
        partialUpdatedReservierung.setId(reservierung.getId());

        partialUpdatedReservierung.status(UPDATED_STATUS);

        restReservierungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservierung.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservierung))
            )
            .andExpect(status().isOk());

        // Validate the Reservierung in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservierungUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReservierung, reservierung),
            getPersistedReservierung(reservierung)
        );
    }

    @Test
    @Transactional
    void fullUpdateReservierungWithPatch() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reservierung using partial update
        Reservierung partialUpdatedReservierung = new Reservierung();
        partialUpdatedReservierung.setId(reservierung.getId());

        partialUpdatedReservierung.kundeName(UPDATED_KUNDE_NAME).status(UPDATED_STATUS);

        restReservierungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservierung.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReservierung))
            )
            .andExpect(status().isOk());

        // Validate the Reservierung in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReservierungUpdatableFieldsEquals(partialUpdatedReservierung, getPersistedReservierung(partialUpdatedReservierung));
    }

    @Test
    @Transactional
    void patchNonExistingReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservierungDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservierungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reservierungDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservierung() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reservierung.setId(longCount.incrementAndGet());

        // Create the Reservierung
        ReservierungDTO reservierungDTO = reservierungMapper.toDto(reservierung);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservierungMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reservierungDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reservierung in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservierung() throws Exception {
        // Initialize the database
        insertedReservierung = reservierungRepository.saveAndFlush(reservierung);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reservierung
        restReservierungMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservierung.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reservierungRepository.count();
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

    protected Reservierung getPersistedReservierung(Reservierung reservierung) {
        return reservierungRepository.findById(reservierung.getId()).orElseThrow();
    }

    protected void assertPersistedReservierungToMatchAllProperties(Reservierung expectedReservierung) {
        assertReservierungAllPropertiesEquals(expectedReservierung, getPersistedReservierung(expectedReservierung));
    }

    protected void assertPersistedReservierungToMatchUpdatableProperties(Reservierung expectedReservierung) {
        assertReservierungAllUpdatablePropertiesEquals(expectedReservierung, getPersistedReservierung(expectedReservierung));
    }
}
