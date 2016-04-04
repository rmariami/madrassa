package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.Inscription;
import fr.rmariami.madrassa.repository.InscriptionRepository;
import fr.rmariami.madrassa.service.InscriptionService;
import fr.rmariami.madrassa.repository.search.InscriptionSearchRepository;
import fr.rmariami.madrassa.web.rest.dto.InscriptionDTO;
import fr.rmariami.madrassa.web.rest.mapper.InscriptionMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InscriptionResource REST controller.
 *
 * @see InscriptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class InscriptionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final String DEFAULT_STATUT = "AAAAA";
    private static final String UPDATED_STATUT = "BBBBB";

    @Inject
    private InscriptionRepository inscriptionRepository;

    @Inject
    private InscriptionMapper inscriptionMapper;

    @Inject
    private InscriptionService inscriptionService;

    @Inject
    private InscriptionSearchRepository inscriptionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InscriptionResource inscriptionResource = new InscriptionResource();
        ReflectionTestUtils.setField(inscriptionResource, "inscriptionService", inscriptionService);
        ReflectionTestUtils.setField(inscriptionResource, "inscriptionMapper", inscriptionMapper);
        this.restInscriptionMockMvc = MockMvcBuilders.standaloneSetup(inscriptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inscriptionSearchRepository.deleteAll();
        inscription = new Inscription();
        inscription.setDate(DEFAULT_DATE);
        inscription.setPrice(DEFAULT_PRICE);
        inscription.setStatut(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    public void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(inscription);

        restInscriptionMockMvc.perform(post("/api/inscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
                .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptions.get(inscriptions.size() - 1);
        assertThat(testInscription.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testInscription.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testInscription.getStatut()).isEqualTo(DEFAULT_STATUT);

        // Validate the Inscription in ElasticSearch
        Inscription inscriptionEs = inscriptionSearchRepository.findOne(testInscription.getId());
        assertThat(inscriptionEs).isEqualToComparingFieldByField(testInscription);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setDate(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(inscription);

        restInscriptionMockMvc.perform(post("/api/inscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
                .andExpect(status().isBadRequest());

        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setPrice(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(inscription);

        restInscriptionMockMvc.perform(post("/api/inscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
                .andExpect(status().isBadRequest());

        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setStatut(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(inscription);

        restInscriptionMockMvc.perform(post("/api/inscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
                .andExpect(status().isBadRequest());

        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptions
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    public void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        inscriptionSearchRepository.save(inscription);
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = new Inscription();
        updatedInscription.setId(inscription.getId());
        updatedInscription.setDate(UPDATED_DATE);
        updatedInscription.setPrice(UPDATED_PRICE);
        updatedInscription.setStatut(UPDATED_STATUT);
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(updatedInscription);

        restInscriptionMockMvc.perform(put("/api/inscriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
                .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptions.get(inscriptions.size() - 1);
        assertThat(testInscription.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testInscription.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInscription.getStatut()).isEqualTo(UPDATED_STATUT);

        // Validate the Inscription in ElasticSearch
        Inscription inscriptionEs = inscriptionSearchRepository.findOne(testInscription.getId());
        assertThat(inscriptionEs).isEqualToComparingFieldByField(testInscription);
    }

    @Test
    @Transactional
    public void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        inscriptionSearchRepository.save(inscription);
        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Get the inscription
        restInscriptionMockMvc.perform(delete("/api/inscriptions/{id}", inscription.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean inscriptionExistsInEs = inscriptionSearchRepository.exists(inscription.getId());
        assertThat(inscriptionExistsInEs).isFalse();

        // Validate the database is empty
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        assertThat(inscriptions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);
        inscriptionSearchRepository.save(inscription);

        // Search the inscription
        restInscriptionMockMvc.perform(get("/api/_search/inscriptions?query=id:" + inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }
}
