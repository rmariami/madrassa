package fr.rmariami.madrassa.web.rest;

import fr.rmariami.madrassa.MadrassaApp;
import fr.rmariami.madrassa.domain.Wish;
import fr.rmariami.madrassa.repository.WishRepository;
import fr.rmariami.madrassa.repository.search.WishSearchRepository;

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

import fr.rmariami.madrassa.domain.enumeration.ClassMoment;

/**
 * Test class for the WishResource REST controller.
 *
 * @see WishResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MadrassaApp.class)
@WebAppConfiguration
@IntegrationTest
public class WishResourceIntTest {


    private static final ClassMoment DEFAULT_MOMENT = ClassMoment.SATURDAY_MORNING;
    private static final ClassMoment UPDATED_MOMENT = ClassMoment.SUNDAY_MORNING;

    @Inject
    private WishRepository wishRepository;

    @Inject
    private WishSearchRepository wishSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restWishMockMvc;

    private Wish wish;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WishResource wishResource = new WishResource();
        ReflectionTestUtils.setField(wishResource, "wishSearchRepository", wishSearchRepository);
        ReflectionTestUtils.setField(wishResource, "wishRepository", wishRepository);
        this.restWishMockMvc = MockMvcBuilders.standaloneSetup(wishResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        wishSearchRepository.deleteAll();
        wish = new Wish();
        wish.setMoment(DEFAULT_MOMENT);
    }

    @Test
    @Transactional
    public void createWish() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // Create the Wish

        restWishMockMvc.perform(post("/api/wishes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(wish)))
                .andExpect(status().isCreated());

        // Validate the Wish in the database
        List<Wish> wishes = wishRepository.findAll();
        assertThat(wishes).hasSize(databaseSizeBeforeCreate + 1);
        Wish testWish = wishes.get(wishes.size() - 1);
        assertThat(testWish.getMoment()).isEqualTo(DEFAULT_MOMENT);

        // Validate the Wish in ElasticSearch
        Wish wishEs = wishSearchRepository.findOne(testWish.getId());
        assertThat(wishEs).isEqualToComparingFieldByField(testWish);
    }

    @Test
    @Transactional
    public void getAllWishes() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get all the wishes
        restWishMockMvc.perform(get("/api/wishes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
                .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())));
    }

    @Test
    @Transactional
    public void getWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(wish.getId().intValue()))
            .andExpect(jsonPath("$.moment").value(DEFAULT_MOMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWish() throws Exception {
        // Get the wish
        restWishMockMvc.perform(get("/api/wishes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);
        wishSearchRepository.save(wish);
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish
        Wish updatedWish = new Wish();
        updatedWish.setId(wish.getId());
        updatedWish.setMoment(UPDATED_MOMENT);

        restWishMockMvc.perform(put("/api/wishes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWish)))
                .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishes = wishRepository.findAll();
        assertThat(wishes).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishes.get(wishes.size() - 1);
        assertThat(testWish.getMoment()).isEqualTo(UPDATED_MOMENT);

        // Validate the Wish in ElasticSearch
        Wish wishEs = wishSearchRepository.findOne(testWish.getId());
        assertThat(wishEs).isEqualToComparingFieldByField(testWish);
    }

    @Test
    @Transactional
    public void deleteWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);
        wishSearchRepository.save(wish);
        int databaseSizeBeforeDelete = wishRepository.findAll().size();

        // Get the wish
        restWishMockMvc.perform(delete("/api/wishes/{id}", wish.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean wishExistsInEs = wishSearchRepository.exists(wish.getId());
        assertThat(wishExistsInEs).isFalse();

        // Validate the database is empty
        List<Wish> wishes = wishRepository.findAll();
        assertThat(wishes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);
        wishSearchRepository.save(wish);

        // Search the wish
        restWishMockMvc.perform(get("/api/_search/wishes?query=id:" + wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
            .andExpect(jsonPath("$.[*].moment").value(hasItem(DEFAULT_MOMENT.toString())));
    }
}
