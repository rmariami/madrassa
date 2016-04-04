package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.ClassRoom;
import fr.rmariami.madrassa.repository.ClassRoomRepository;
import fr.rmariami.madrassa.repository.search.ClassRoomSearchRepository;

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

import fr.rmariami.madrassa.domain.enumeration.MomentEnum;

/**
 * Test class for the ClassRoomResource REST controller.
 *
 * @see ClassRoomResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClassRoomResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final MomentEnum DEFAULT_MOMENT = MomentEnum.SATURDAY_MORNING;
    private static final MomentEnum UPDATED_MOMENT = MomentEnum.SUNDAY_MORNING;
    private static final String DEFAULT_START_HOUR = "AAAAA";
    private static final String UPDATED_START_HOUR = "BBBBB";
    private static final String DEFAULT_END_HOUR = "AAAAA";
    private static final String UPDATED_END_HOUR = "BBBBB";

    @Inject
    private ClassRoomRepository classRoomRepository;

    @Inject
    private ClassRoomSearchRepository classRoomSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClassRoomMockMvc;

    private ClassRoom classRoom;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassRoomResource classRoomResource = new ClassRoomResource();
        ReflectionTestUtils.setField(classRoomResource, "classRoomSearchRepository", classRoomSearchRepository);
        ReflectionTestUtils.setField(classRoomResource, "classRoomRepository", classRoomRepository);
        this.restClassRoomMockMvc = MockMvcBuilders.standaloneSetup(classRoomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        classRoomSearchRepository.deleteAll();
        classRoom = new ClassRoom();
        classRoom.setCode(DEFAULT_CODE);
        classRoom.setName(DEFAULT_NAME);
        classRoom.setMoment(DEFAULT_MOMENT);
        classRoom.setStartHour(DEFAULT_START_HOUR);
        classRoom.setEndHour(DEFAULT_END_HOUR);
    }

    @Test
    @Transactional
    public void createClassRoom() throws Exception {
        int databaseSizeBeforeCreate = classRoomRepository.findAll().size();

        // Create the ClassRoom

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isCreated());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeCreate + 1);
        ClassRoom testClassRoom = classRooms.get(classRooms.size() - 1);
        assertThat(testClassRoom.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testClassRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClassRoom.getMoment()).isEqualTo(DEFAULT_MOMENT);
        assertThat(testClassRoom.getStartHour()).isEqualTo(DEFAULT_START_HOUR);
        assertThat(testClassRoom.getEndHour()).isEqualTo(DEFAULT_END_HOUR);

        // Validate the ClassRoom in ElasticSearch
        ClassRoom classRoomEs = classRoomSearchRepository.findOne(testClassRoom.getId());
        assertThat(classRoomEs).isEqualToComparingFieldByField(testClassRoom);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setCode(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isBadRequest());

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setName(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isBadRequest());

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMomentIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setMoment(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isBadRequest());

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setStartHour(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isBadRequest());

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = classRoomRepository.findAll().size();
        // set the field null
        classRoom.setEndHour(null);

        // Create the ClassRoom, which fails.

        restClassRoomMockMvc.perform(post("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classRoom)))
                .andExpect(status().isBadRequest());

        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassRooms() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get all the classRooms
        restClassRoomMockMvc.perform(get("/api/class-rooms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())))
                .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR.toString())))
                .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR.toString())));
    }

    @Test
    @Transactional
    public void getClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);

        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", classRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(classRoom.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.moment").value(DEFAULT_MOMENT.toString()))
            .andExpect(jsonPath("$.startHour").value(DEFAULT_START_HOUR.toString()))
            .andExpect(jsonPath("$.endHour").value(DEFAULT_END_HOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassRoom() throws Exception {
        // Get the classRoom
        restClassRoomMockMvc.perform(get("/api/class-rooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);
        int databaseSizeBeforeUpdate = classRoomRepository.findAll().size();

        // Update the classRoom
        ClassRoom updatedClassRoom = new ClassRoom();
        updatedClassRoom.setId(classRoom.getId());
        updatedClassRoom.setCode(UPDATED_CODE);
        updatedClassRoom.setName(UPDATED_NAME);
        updatedClassRoom.setMoment(UPDATED_MOMENT);
        updatedClassRoom.setStartHour(UPDATED_START_HOUR);
        updatedClassRoom.setEndHour(UPDATED_END_HOUR);

        restClassRoomMockMvc.perform(put("/api/class-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClassRoom)))
                .andExpect(status().isOk());

        // Validate the ClassRoom in the database
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeUpdate);
        ClassRoom testClassRoom = classRooms.get(classRooms.size() - 1);
        assertThat(testClassRoom.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClassRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClassRoom.getMoment()).isEqualTo(UPDATED_MOMENT);
        assertThat(testClassRoom.getStartHour()).isEqualTo(UPDATED_START_HOUR);
        assertThat(testClassRoom.getEndHour()).isEqualTo(UPDATED_END_HOUR);

        // Validate the ClassRoom in ElasticSearch
        ClassRoom classRoomEs = classRoomSearchRepository.findOne(testClassRoom.getId());
        assertThat(classRoomEs).isEqualToComparingFieldByField(testClassRoom);
    }

    @Test
    @Transactional
    public void deleteClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);
        int databaseSizeBeforeDelete = classRoomRepository.findAll().size();

        // Get the classRoom
        restClassRoomMockMvc.perform(delete("/api/class-rooms/{id}", classRoom.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean classRoomExistsInEs = classRoomSearchRepository.exists(classRoom.getId());
        assertThat(classRoomExistsInEs).isFalse();

        // Validate the database is empty
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        assertThat(classRooms).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchClassRoom() throws Exception {
        // Initialize the database
        classRoomRepository.saveAndFlush(classRoom);
        classRoomSearchRepository.save(classRoom);

        // Search the classRoom
        restClassRoomMockMvc.perform(get("/api/_search/class-rooms?query=id:" + classRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())))
            .andExpect(jsonPath("$.[*].startHour").value(hasItem(DEFAULT_START_HOUR.toString())))
            .andExpect(jsonPath("$.[*].endHour").value(hasItem(DEFAULT_END_HOUR.toString())));
    }
}
