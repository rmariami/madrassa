package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.Teacher;
import fr.rmariami.madrassa.repository.TeacherRepository;
import fr.rmariami.madrassa.repository.search.TeacherSearchRepository;

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
 * Test class for the TeacherResource REST controller.
 *
 * @see TeacherResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class TeacherResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_ADRESS = "AAAAA";
    private static final String UPDATED_ADRESS = "BBBBB";
    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_MOBILE_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_MOBILE_PHONE_NUMBER = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private TeacherRepository teacherRepository;

    @Inject
    private TeacherSearchRepository teacherSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTeacherMockMvc;

    private Teacher teacher;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TeacherResource teacherResource = new TeacherResource();
        ReflectionTestUtils.setField(teacherResource, "teacherSearchRepository", teacherSearchRepository);
        ReflectionTestUtils.setField(teacherResource, "teacherRepository", teacherRepository);
        this.restTeacherMockMvc = MockMvcBuilders.standaloneSetup(teacherResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        teacherSearchRepository.deleteAll();
        teacher = new Teacher();
        teacher.setName(DEFAULT_NAME);
        teacher.setFirstName(DEFAULT_FIRST_NAME);
        teacher.setAdress(DEFAULT_ADRESS);
        teacher.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        teacher.setMobilePhoneNumber(DEFAULT_MOBILE_PHONE_NUMBER);
        teacher.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createTeacher() throws Exception {
        int databaseSizeBeforeCreate = teacherRepository.findAll().size();

        // Create the Teacher

        restTeacherMockMvc.perform(post("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teacher)))
                .andExpect(status().isCreated());

        // Validate the Teacher in the database
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeCreate + 1);
        Teacher testTeacher = teachers.get(teachers.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeacher.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testTeacher.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testTeacher.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testTeacher.getMobilePhoneNumber()).isEqualTo(DEFAULT_MOBILE_PHONE_NUMBER);
        assertThat(testTeacher.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Teacher in ElasticSearch
        Teacher teacherEs = teacherSearchRepository.findOne(testTeacher.getId());
        assertThat(teacherEs).isEqualToComparingFieldByField(testTeacher);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setName(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc.perform(post("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teacher)))
                .andExpect(status().isBadRequest());

        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setFirstName(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc.perform(post("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teacher)))
                .andExpect(status().isBadRequest());

        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setAdress(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc.perform(post("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teacher)))
                .andExpect(status().isBadRequest());

        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMobilePhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = teacherRepository.findAll().size();
        // set the field null
        teacher.setMobilePhoneNumber(null);

        // Create the Teacher, which fails.

        restTeacherMockMvc.perform(post("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(teacher)))
                .andExpect(status().isBadRequest());

        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeachers() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get all the teachers
        restTeacherMockMvc.perform(get("/api/teachers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].mobilePhoneNumber").value(hasItem(DEFAULT_MOBILE_PHONE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);

        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(teacher.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.mobilePhoneNumber").value(DEFAULT_MOBILE_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeacher() throws Exception {
        // Get the teacher
        restTeacherMockMvc.perform(get("/api/teachers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        teacherSearchRepository.save(teacher);
        int databaseSizeBeforeUpdate = teacherRepository.findAll().size();

        // Update the teacher
        Teacher updatedTeacher = new Teacher();
        updatedTeacher.setId(teacher.getId());
        updatedTeacher.setName(UPDATED_NAME);
        updatedTeacher.setFirstName(UPDATED_FIRST_NAME);
        updatedTeacher.setAdress(UPDATED_ADRESS);
        updatedTeacher.setPhoneNumber(UPDATED_PHONE_NUMBER);
        updatedTeacher.setMobilePhoneNumber(UPDATED_MOBILE_PHONE_NUMBER);
        updatedTeacher.setEmail(UPDATED_EMAIL);

        restTeacherMockMvc.perform(put("/api/teachers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTeacher)))
                .andExpect(status().isOk());

        // Validate the Teacher in the database
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeUpdate);
        Teacher testTeacher = teachers.get(teachers.size() - 1);
        assertThat(testTeacher.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeacher.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testTeacher.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testTeacher.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testTeacher.getMobilePhoneNumber()).isEqualTo(UPDATED_MOBILE_PHONE_NUMBER);
        assertThat(testTeacher.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Teacher in ElasticSearch
        Teacher teacherEs = teacherSearchRepository.findOne(testTeacher.getId());
        assertThat(teacherEs).isEqualToComparingFieldByField(testTeacher);
    }

    @Test
    @Transactional
    public void deleteTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        teacherSearchRepository.save(teacher);
        int databaseSizeBeforeDelete = teacherRepository.findAll().size();

        // Get the teacher
        restTeacherMockMvc.perform(delete("/api/teachers/{id}", teacher.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean teacherExistsInEs = teacherSearchRepository.exists(teacher.getId());
        assertThat(teacherExistsInEs).isFalse();

        // Validate the database is empty
        List<Teacher> teachers = teacherRepository.findAll();
        assertThat(teachers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTeacher() throws Exception {
        // Initialize the database
        teacherRepository.saveAndFlush(teacher);
        teacherSearchRepository.save(teacher);

        // Search the teacher
        restTeacherMockMvc.perform(get("/api/_search/teachers?query=id:" + teacher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teacher.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].mobilePhoneNumber").value(hasItem(DEFAULT_MOBILE_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
