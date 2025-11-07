package de.cinebuddy.web.rest;

import static de.cinebuddy.domain.SaalAsserts.*;
import static de.cinebuddy.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.cinebuddy.IntegrationTest;
import de.cinebuddy.domain.Saal;
import de.cinebuddy.repository.SaalRepository;
import de.cinebuddy.service.dto.SaalDTO;
import de.cinebuddy.service.mapper.SaalMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SaalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_KAPAZITAET = 1;
    private static final Integer UPDATED_KAPAZITAET = 2;

    private static final String ENTITY_API_URL = "/api/saals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaalRepository saalRepository;

    @Autowired
    private SaalMapper saalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaalMockMvc;

    private Saal saal;

    private Saal insertedSaal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saal createEntity() {
        return new Saal().name(DEFAULT_NAME).kapazitaet(DEFAULT_KAPAZITAET);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saal createUpdatedEntity() {
        return new Saal().name(UPDATED_NAME).kapazitaet(UPDATED_KAPAZITAET);
    }

    @BeforeEach
    void initTest() {
        saal = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSaal != null) {
            saalRepository.delete(insertedSaal);
            insertedSaal = null;
        }
    }

    @Test
    @Transactional
    void createSaal() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);
        var returnedSaalDTO = om.readValue(
            restSaalMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaalDTO.class
        );

        // Validate the Saal in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSaal = saalMapper.toEntity(returnedSaalDTO);
        assertSaalUpdatableFieldsEquals(returnedSaal, getPersistedSaal(returnedSaal));

        insertedSaal = returnedSaal;
    }

    @Test
    @Transactional
    void createSaalWithExistingId() throws Exception {
        // Create the Saal with an existing ID
        saal.setId(1L);
        SaalDTO saalDTO = saalMapper.toDto(saal);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saal.setName(null);

        // Create the Saal, which fails.
        SaalDTO saalDTO = saalMapper.toDto(saal);

        restSaalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKapazitaetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saal.setKapazitaet(null);

        // Create the Saal, which fails.
        SaalDTO saalDTO = saalMapper.toDto(saal);

        restSaalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaals() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        // Get all the saalList
        restSaalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].kapazitaet").value(hasItem(DEFAULT_KAPAZITAET)));
    }

    @Test
    @Transactional
    void getSaal() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        // Get the saal
        restSaalMockMvc
            .perform(get(ENTITY_API_URL_ID, saal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.kapazitaet").value(DEFAULT_KAPAZITAET));
    }

    @Test
    @Transactional
    void getNonExistingSaal() throws Exception {
        // Get the saal
        restSaalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaal() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saal
        Saal updatedSaal = saalRepository.findById(saal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaal are not directly saved in db
        em.detach(updatedSaal);
        updatedSaal.name(UPDATED_NAME).kapazitaet(UPDATED_KAPAZITAET);
        SaalDTO saalDTO = saalMapper.toDto(updatedSaal);

        restSaalMockMvc
            .perform(put(ENTITY_API_URL_ID, saalDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isOk());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaalToMatchAllProperties(updatedSaal);
    }

    @Test
    @Transactional
    void putNonExistingSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(put(ENTITY_API_URL_ID, saalDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaalWithPatch() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saal using partial update
        Saal partialUpdatedSaal = new Saal();
        partialUpdatedSaal.setId(saal.getId());

        restSaalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaal))
            )
            .andExpect(status().isOk());

        // Validate the Saal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaalUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSaal, saal), getPersistedSaal(saal));
    }

    @Test
    @Transactional
    void fullUpdateSaalWithPatch() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saal using partial update
        Saal partialUpdatedSaal = new Saal();
        partialUpdatedSaal.setId(saal.getId());

        partialUpdatedSaal.name(UPDATED_NAME).kapazitaet(UPDATED_KAPAZITAET);

        restSaalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaal.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaal))
            )
            .andExpect(status().isOk());

        // Validate the Saal in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaalUpdatableFieldsEquals(partialUpdatedSaal, getPersistedSaal(partialUpdatedSaal));
    }

    @Test
    @Transactional
    void patchNonExistingSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saalDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaal() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saal.setId(longCount.incrementAndGet());

        // Create the Saal
        SaalDTO saalDTO = saalMapper.toDto(saal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saal in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaal() throws Exception {
        // Initialize the database
        insertedSaal = saalRepository.saveAndFlush(saal);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saal
        restSaalMockMvc
            .perform(delete(ENTITY_API_URL_ID, saal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saalRepository.count();
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

    protected Saal getPersistedSaal(Saal saal) {
        return saalRepository.findById(saal.getId()).orElseThrow();
    }

    protected void assertPersistedSaalToMatchAllProperties(Saal expectedSaal) {
        assertSaalAllPropertiesEquals(expectedSaal, getPersistedSaal(expectedSaal));
    }

    protected void assertPersistedSaalToMatchUpdatableProperties(Saal expectedSaal) {
        assertSaalAllUpdatablePropertiesEquals(expectedSaal, getPersistedSaal(expectedSaal));
    }
}
