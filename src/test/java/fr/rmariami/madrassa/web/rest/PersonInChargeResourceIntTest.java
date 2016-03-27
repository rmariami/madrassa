package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.PersonInCharge;
import fr.rmariami.madrassa.repository.PersonInChargeRepository;
import fr.rmariami.madrassa.repository.search.PersonInChargeSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PersonInChargeResource REST controller.
 *
 * @see PersonInChargeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class PersonInChargeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_ADRESS = "AAAAA";
    private static final String UPDATED_ADRESS = "BBBBB";
    private static final String DEFAULT_WORK = "AAAAA";
    private static final String UPDATED_WORK = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_MOBILE_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_MOBILE_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private PersonInChargeRepository personInChargeRepository;

    @Inject
    private PersonInChargeSearchRepository personInChargeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPersonInChargeMockMvc;

    private PersonInCharge personInCharge;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonInChargeResource personInChargeResource = new PersonInChargeResource();
        ReflectionTestUtils.setField(personInChargeResource, "personInChargeSearchRepository", personInChargeSearchRepository);
        ReflectionTestUtils.setField(personInChargeResource, "personInChargeRepository", personInChargeRepository);
        this.restPersonInChargeMockMvc = MockMvcBuilders.standaloneSetup(personInChargeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        personInChargeSearchRepository.deleteAll();
        personInCharge = new PersonInCharge();
        personInCharge.setName(DEFAULT_NAME);
        personInCharge.setFirstName(DEFAULT_FIRST_NAME);
        personInCharge.setAdress(DEFAULT_ADRESS);
        personInCharge.setWork(DEFAULT_WORK);
        personInCharge.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        personInCharge.setMobilePhoneNumber(DEFAULT_MOBILE_PHONE_NUMBER);
        personInCharge.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createPersonInCharge() throws Exception {
        int databaseSizeBeforeCreate = personInChargeRepository.findAll().size();

        // Create the PersonInCharge

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isCreated());

        // Validate the PersonInCharge in the database
        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeCreate + 1);
        PersonInCharge testPersonInCharge = personInCharges.get(personInCharges.size() - 1);
        assertThat(testPersonInCharge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonInCharge.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPersonInCharge.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testPersonInCharge.getWork()).isEqualTo(DEFAULT_WORK);
        assertThat(testPersonInCharge.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersonInCharge.getMobilePhoneNumber()).isEqualTo(DEFAULT_MOBILE_PHONE_NUMBER);
        assertThat(testPersonInCharge.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the PersonInCharge in ElasticSearch
        PersonInCharge personInChargeEs = personInChargeSearchRepository.findOne(testPersonInCharge.getId());
        assertThat(personInChargeEs).isEqualToComparingFieldByField(testPersonInCharge);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personInChargeRepository.findAll().size();
        // set the field null
        personInCharge.setName(null);

        // Create the PersonInCharge, which fails.

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isBadRequest());

        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personInChargeRepository.findAll().size();
        // set the field null
        personInCharge.setFirstName(null);

        // Create the PersonInCharge, which fails.

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isBadRequest());

        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = personInChargeRepository.findAll().size();
        // set the field null
        personInCharge.setAdress(null);

        // Create the PersonInCharge, which fails.

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isBadRequest());

        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWorkIsRequired() throws Exception {
        int databaseSizeBeforeTest = personInChargeRepository.findAll().size();
        // set the field null
        personInCharge.setWork(null);

        // Create the PersonInCharge, which fails.

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isBadRequest());

        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobilePhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = personInChargeRepository.findAll().size();
        // set the field null
        personInCharge.setMobilePhoneNumber(null);

        // Create the PersonInCharge, which fails.

        restPersonInChargeMockMvc.perform(post("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(personInCharge)))
                .andExpect(status().isBadRequest());

        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonInCharges() throws Exception {
        // Initialize the database
        personInChargeRepository.saveAndFlush(personInCharge);

        // Get all the personInCharges
        restPersonInChargeMockMvc.perform(get("/api/person-in-charges?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(personInCharge.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
                .andExpect(jsonPath("$.[*].work").value(hasItem(DEFAULT_WORK.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].mobilePhoneNumber").value(hasItem(DEFAULT_MOBILE_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getPersonInCharge() throws Exception {
        // Initialize the database
        personInChargeRepository.saveAndFlush(personInCharge);

        // Get the personInCharge
        restPersonInChargeMockMvc.perform(get("/api/person-in-charges/{id}", personInCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(personInCharge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.work").value(DEFAULT_WORK.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.mobilePhoneNumber").value(DEFAULT_MOBILE_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonInCharge() throws Exception {
        // Get the personInCharge
        restPersonInChargeMockMvc.perform(get("/api/person-in-charges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonInCharge() throws Exception {
        // Initialize the database
        personInChargeRepository.saveAndFlush(personInCharge);
        personInChargeSearchRepository.save(personInCharge);
        int databaseSizeBeforeUpdate = personInChargeRepository.findAll().size();

        // Update the personInCharge
        PersonInCharge updatedPersonInCharge = new PersonInCharge();
        updatedPersonInCharge.setId(personInCharge.getId());
        updatedPersonInCharge.setName(UPDATED_NAME);
        updatedPersonInCharge.setFirstName(UPDATED_FIRST_NAME);
        updatedPersonInCharge.setAdress(UPDATED_ADRESS);
        updatedPersonInCharge.setWork(UPDATED_WORK);
        updatedPersonInCharge.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedPersonInCharge.setMobilePhoneNumber(UPDATED_MOBILE_PHONE_NUMBER);
        updatedPersonInCharge.setEmail(UPDATED_EMAIL);

        restPersonInChargeMockMvc.perform(put("/api/person-in-charges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPersonInCharge)))
                .andExpect(status().isOk());

        // Validate the PersonInCharge in the database
        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeUpdate);
        PersonInCharge testPersonInCharge = personInCharges.get(personInCharges.size() - 1);
        assertThat(testPersonInCharge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonInCharge.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPersonInCharge.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testPersonInCharge.getWork()).isEqualTo(UPDATED_WORK);
        assertThat(testPersonInCharge.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersonInCharge.getMobilePhoneNumber()).isEqualTo(UPDATED_MOBILE_PHONE_NUMBER);
        assertThat(testPersonInCharge.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the PersonInCharge in ElasticSearch
        PersonInCharge personInChargeEs = personInChargeSearchRepository.findOne(testPersonInCharge.getId());
        assertThat(personInChargeEs).isEqualToComparingFieldByField(testPersonInCharge);
    }

    @Test
    @Transactional
    public void deletePersonInCharge() throws Exception {
        // Initialize the database
        personInChargeRepository.saveAndFlush(personInCharge);
        personInChargeSearchRepository.save(personInCharge);
        int databaseSizeBeforeDelete = personInChargeRepository.findAll().size();

        // Get the personInCharge
        restPersonInChargeMockMvc.perform(delete("/api/person-in-charges/{id}", personInCharge.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean personInChargeExistsInEs = personInChargeSearchRepository.exists(personInCharge.getId());
        assertThat(personInChargeExistsInEs).isFalse();

        // Validate the database is empty
        List<PersonInCharge> personInCharges = personInChargeRepository.findAll();
        assertThat(personInCharges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPersonInCharge() throws Exception {
        // Initialize the database
        personInChargeRepository.saveAndFlush(personInCharge);
        personInChargeSearchRepository.save(personInCharge);

        // Search the personInCharge
        restPersonInChargeMockMvc.perform(get("/api/_search/person-in-charges?query=id:" + personInCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personInCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].work").value(hasItem(DEFAULT_WORK.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].mobilePhoneNumber").value(hasItem(DEFAULT_MOBILE_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
