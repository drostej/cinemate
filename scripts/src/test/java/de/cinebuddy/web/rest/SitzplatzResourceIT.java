package de.cinebuddy.web.rest;

import static de.cinebuddy.domain.SitzplatzAsserts.*;
import static de.cinebuddy.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cinebuddy.IntegrationTest;
import de.cinebuddy.domain.Sitzplatz;
import de.cinebuddy.repository.SitzplatzRepository;
import de.cinebuddy.service.SitzplatzService;
import de.cinebuddy.service.dto.SitzplatzDTO;
import de.cinebuddy.service.mapper.SitzplatzMapper;
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
 * Integration tests for the {@link SitzplatzResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SitzplatzResourceIT {

    private static final String DEFAULT_REIHE = "A";
    private static final String UPDATED_REIHE = "B";

    private static final Integer DEFAULT_NUMMER = 1;
    private static final Integer UPDATED_NUMMER = 2;

    private static final String ENTITY_API_URL = "/api/sitzplatzs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SitzplatzRepository sitzplatzRepository;

    @Mock
    private SitzplatzRepository sitzplatzRepositoryMock;

    @Autowired
    private SitzplatzMapper sitzplatzMapper;

    @Mock
    private SitzplatzService sitzplatzServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSitzplatzMockMvc;

    private Sitzplatz sitzplatz;

    private Sitzplatz insertedSitzplatz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sitzplatz createEntity() {
        return new Sitzplatz().reihe(DEFAULT_REIHE).nummer(DEFAULT_NUMMER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sitzplatz createUpdatedEntity() {
        return new Sitzplatz().reihe(UPDATED_REIHE).nummer(UPDATED_NUMMER);
    }

    @BeforeEach
    void initTest() {
        sitzplatz = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSitzplatz != null) {
            sitzplatzRepository.delete(insertedSitzplatz);
            insertedSitzplatz = null;
        }
    }

    @Test
    @Transactional
    void createSitzplatz() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);
        var returnedSitzplatzDTO = om.readValue(
            restSitzplatzMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sitzplatzDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SitzplatzDTO.class
        );

        // Validate the Sitzplatz in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSitzplatz = sitzplatzMapper.toEntity(returnedSitzplatzDTO);
        assertSitzplatzUpdatableFieldsEquals(returnedSitzplatz, getPersistedSitzplatz(returnedSitzplatz));

        insertedSitzplatz = returnedSitzplatz;
    }

    @Test
    @Transactional
    void createSitzplatzWithExistingId() throws Exception {
        // Create the Sitzplatz with an existing ID
        sitzplatz.setId(1L);
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSitzplatzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sitzplatzDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReiheIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sitzplatz.setReihe(null);

        // Create the Sitzplatz, which fails.
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        restSitzplatzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sitzplatzDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNummerIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        sitzplatz.setNummer(null);

        // Create the Sitzplatz, which fails.
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        restSitzplatzMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sitzplatzDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSitzplatzs() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        // Get all the sitzplatzList
        restSitzplatzMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sitzplatz.getId().intValue())))
            .andExpect(jsonPath("$.[*].reihe").value(hasItem(DEFAULT_REIHE)))
            .andExpect(jsonPath("$.[*].nummer").value(hasItem(DEFAULT_NUMMER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSitzplatzsWithEagerRelationshipsIsEnabled() throws Exception {
        when(sitzplatzServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSitzplatzMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(sitzplatzServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSitzplatzsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(sitzplatzServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSitzplatzMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(sitzplatzRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSitzplatz() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        // Get the sitzplatz
        restSitzplatzMockMvc
            .perform(get(ENTITY_API_URL_ID, sitzplatz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sitzplatz.getId().intValue()))
            .andExpect(jsonPath("$.reihe").value(DEFAULT_REIHE))
            .andExpect(jsonPath("$.nummer").value(DEFAULT_NUMMER));
    }

    @Test
    @Transactional
    void getNonExistingSitzplatz() throws Exception {
        // Get the sitzplatz
        restSitzplatzMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSitzplatz() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sitzplatz
        Sitzplatz updatedSitzplatz = sitzplatzRepository.findById(sitzplatz.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSitzplatz are not directly saved in db
        em.detach(updatedSitzplatz);
        updatedSitzplatz.reihe(UPDATED_REIHE).nummer(UPDATED_NUMMER);
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(updatedSitzplatz);

        restSitzplatzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sitzplatzDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sitzplatzDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSitzplatzToMatchAllProperties(updatedSitzplatz);
    }

    @Test
    @Transactional
    void putNonExistingSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sitzplatzDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sitzplatzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sitzplatzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sitzplatzDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSitzplatzWithPatch() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sitzplatz using partial update
        Sitzplatz partialUpdatedSitzplatz = new Sitzplatz();
        partialUpdatedSitzplatz.setId(sitzplatz.getId());

        restSitzplatzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSitzplatz.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSitzplatz))
            )
            .andExpect(status().isOk());

        // Validate the Sitzplatz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSitzplatzUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSitzplatz, sitzplatz),
            getPersistedSitzplatz(sitzplatz)
        );
    }

    @Test
    @Transactional
    void fullUpdateSitzplatzWithPatch() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sitzplatz using partial update
        Sitzplatz partialUpdatedSitzplatz = new Sitzplatz();
        partialUpdatedSitzplatz.setId(sitzplatz.getId());

        partialUpdatedSitzplatz.reihe(UPDATED_REIHE).nummer(UPDATED_NUMMER);

        restSitzplatzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSitzplatz.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSitzplatz))
            )
            .andExpect(status().isOk());

        // Validate the Sitzplatz in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSitzplatzUpdatableFieldsEquals(partialUpdatedSitzplatz, getPersistedSitzplatz(partialUpdatedSitzplatz));
    }

    @Test
    @Transactional
    void patchNonExistingSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sitzplatzDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sitzplatzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sitzplatzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSitzplatz() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sitzplatz.setId(longCount.incrementAndGet());

        // Create the Sitzplatz
        SitzplatzDTO sitzplatzDTO = sitzplatzMapper.toDto(sitzplatz);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSitzplatzMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sitzplatzDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sitzplatz in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSitzplatz() throws Exception {
        // Initialize the database
        insertedSitzplatz = sitzplatzRepository.saveAndFlush(sitzplatz);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sitzplatz
        restSitzplatzMockMvc
            .perform(delete(ENTITY_API_URL_ID, sitzplatz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sitzplatzRepository.count();
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

    protected Sitzplatz getPersistedSitzplatz(Sitzplatz sitzplatz) {
        return sitzplatzRepository.findById(sitzplatz.getId()).orElseThrow();
    }

    protected void assertPersistedSitzplatzToMatchAllProperties(Sitzplatz expectedSitzplatz) {
        assertSitzplatzAllPropertiesEquals(expectedSitzplatz, getPersistedSitzplatz(expectedSitzplatz));
    }

    protected void assertPersistedSitzplatzToMatchUpdatableProperties(Sitzplatz expectedSitzplatz) {
        assertSitzplatzAllUpdatablePropertiesEquals(expectedSitzplatz, getPersistedSitzplatz(expectedSitzplatz));
    }
}
