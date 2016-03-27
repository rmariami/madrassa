package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.Scholar;
import fr.rmariami.madrassa.repository.ScholarRepository;
import fr.rmariami.madrassa.repository.search.ScholarSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.rmariami.madrassa.domain.enumeration.SexEnum;

/**
 * Test class for the ScholarResource REST controller.
 *
 * @see ScholarResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class ScholarResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_SCHOLAR_NUMBER = 1;
    private static final Integer UPDATED_SCHOLAR_NUMBER = 2;

    private static final SexEnum DEFAULT_SEX = SexEnum.MAN;
    private static final SexEnum UPDATED_SEX = SexEnum.WOMEN;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BIRTH_DATE_STR = dateTimeFormatter.format(DEFAULT_BIRTH_DATE);
    private static final String DEFAULT_BIRTH_PLACE = "AAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_NB_YEARS_XP = 1;
    private static final Integer UPDATED_NB_YEARS_XP = 2;

    @Inject
    private ScholarRepository scholarRepository;

    @Inject
    private ScholarSearchRepository scholarSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScholarMockMvc;

    private Scholar scholar;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScholarResource scholarResource = new ScholarResource();
        ReflectionTestUtils.setField(scholarResource, "scholarSearchRepository", scholarSearchRepository);
        ReflectionTestUtils.setField(scholarResource, "scholarRepository", scholarRepository);
        this.restScholarMockMvc = MockMvcBuilders.standaloneSetup(scholarResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scholarSearchRepository.deleteAll();
        scholar = new Scholar();
        scholar.setScholarNumber(DEFAULT_SCHOLAR_NUMBER);
        scholar.setSex(DEFAULT_SEX);
        scholar.setName(DEFAULT_NAME);
        scholar.setFirstName(DEFAULT_FIRST_NAME);
        scholar.setBirthDate(DEFAULT_BIRTH_DATE);
        scholar.setBirthPlace(DEFAULT_BIRTH_PLACE);
        scholar.setPhoto(DEFAULT_PHOTO);
        scholar.setPhotoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        scholar.setNbYearsXP(DEFAULT_NB_YEARS_XP);
    }

    @Test
    @Transactional
    public void createScholar() throws Exception {
        int databaseSizeBeforeCreate = scholarRepository.findAll().size();

        // Create the Scholar

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isCreated());

        // Validate the Scholar in the database
        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeCreate + 1);
        Scholar testScholar = scholars.get(scholars.size() - 1);
        assertThat(testScholar.getScholarNumber()).isEqualTo(DEFAULT_SCHOLAR_NUMBER);
        assertThat(testScholar.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testScholar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScholar.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testScholar.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testScholar.getBirthPlace()).isEqualTo(DEFAULT_BIRTH_PLACE);
        assertThat(testScholar.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testScholar.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testScholar.getNbYearsXP()).isEqualTo(DEFAULT_NB_YEARS_XP);

        // Validate the Scholar in ElasticSearch
        Scholar scholarEs = scholarSearchRepository.findOne(testScholar.getId());
        assertThat(scholarEs).isEqualToComparingFieldByField(testScholar);
    }

    @Test
    @Transactional
    public void checkScholarNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setScholarNumber(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setName(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setFirstName(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setBirthDate(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setBirthPlace(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNbYearsXPIsRequired() throws Exception {
        int databaseSizeBeforeTest = scholarRepository.findAll().size();
        // set the field null
        scholar.setNbYearsXP(null);

        // Create the Scholar, which fails.

        restScholarMockMvc.perform(post("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scholar)))
                .andExpect(status().isBadRequest());

        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScholars() throws Exception {
        // Initialize the database
        scholarRepository.saveAndFlush(scholar);

        // Get all the scholars
        restScholarMockMvc.perform(get("/api/scholars?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scholar.getId().intValue())))
                .andExpect(jsonPath("$.[*].scholarNumber").value(hasItem(DEFAULT_SCHOLAR_NUMBER)))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)))
                .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE.toString())))
                .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
                .andExpect(jsonPath("$.[*].nbYearsXP").value(hasItem(DEFAULT_NB_YEARS_XP)));
    }

    @Test
    @Transactional
    public void getScholar() throws Exception {
        // Initialize the database
        scholarRepository.saveAndFlush(scholar);

        // Get the scholar
        restScholarMockMvc.perform(get("/api/scholars/{id}", scholar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scholar.getId().intValue()))
            .andExpect(jsonPath("$.scholarNumber").value(DEFAULT_SCHOLAR_NUMBER))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE_STR))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.nbYearsXP").value(DEFAULT_NB_YEARS_XP));
    }

    @Test
    @Transactional
    public void getNonExistingScholar() throws Exception {
        // Get the scholar
        restScholarMockMvc.perform(get("/api/scholars/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScholar() throws Exception {
        // Initialize the database
        scholarRepository.saveAndFlush(scholar);
        scholarSearchRepository.save(scholar);
        int databaseSizeBeforeUpdate = scholarRepository.findAll().size();

        // Update the scholar
        Scholar updatedScholar = new Scholar();
        updatedScholar.setId(scholar.getId());
        updatedScholar.setScholarNumber(UPDATED_SCHOLAR_NUMBER);
        updatedScholar.setSex(UPDATED_SEX);
        updatedScholar.setName(UPDATED_NAME);
        updatedScholar.setFirstName(UPDATED_FIRST_NAME);
        updatedScholar.setBirthDate(UPDATED_BIRTH_DATE);
        updatedScholar.setBirthPlace(UPDATED_BIRTH_PLACE);
        updatedScholar.setPhoto(UPDATED_PHOTO);
        updatedScholar.setPhotoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        updatedScholar.setNbYearsXP(UPDATED_NB_YEARS_XP);

        restScholarMockMvc.perform(put("/api/scholars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedScholar)))
                .andExpect(status().isOk());

        // Validate the Scholar in the database
        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeUpdate);
        Scholar testScholar = scholars.get(scholars.size() - 1);
        assertThat(testScholar.getScholarNumber()).isEqualTo(UPDATED_SCHOLAR_NUMBER);
        assertThat(testScholar.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testScholar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScholar.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testScholar.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testScholar.getBirthPlace()).isEqualTo(UPDATED_BIRTH_PLACE);
        assertThat(testScholar.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testScholar.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testScholar.getNbYearsXP()).isEqualTo(UPDATED_NB_YEARS_XP);

        // Validate the Scholar in ElasticSearch
        Scholar scholarEs = scholarSearchRepository.findOne(testScholar.getId());
        assertThat(scholarEs).isEqualToComparingFieldByField(testScholar);
    }

    @Test
    @Transactional
    public void deleteScholar() throws Exception {
        // Initialize the database
        scholarRepository.saveAndFlush(scholar);
        scholarSearchRepository.save(scholar);
        int databaseSizeBeforeDelete = scholarRepository.findAll().size();

        // Get the scholar
        restScholarMockMvc.perform(delete("/api/scholars/{id}", scholar.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean scholarExistsInEs = scholarSearchRepository.exists(scholar.getId());
        assertThat(scholarExistsInEs).isFalse();

        // Validate the database is empty
        List<Scholar> scholars = scholarRepository.findAll();
        assertThat(scholars).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchScholar() throws Exception {
        // Initialize the database
        scholarRepository.saveAndFlush(scholar);
        scholarSearchRepository.save(scholar);

        // Search the scholar
        restScholarMockMvc.perform(get("/api/_search/scholars?query=id:" + scholar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scholar.getId().intValue())))
            .andExpect(jsonPath("$.[*].scholarNumber").value(hasItem(DEFAULT_SCHOLAR_NUMBER)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE_STR)))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].nbYearsXP").value(hasItem(DEFAULT_NB_YEARS_XP)));
    }
}
