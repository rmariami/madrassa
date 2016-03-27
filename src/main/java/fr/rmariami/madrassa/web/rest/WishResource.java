package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.Wish;
import fr.rmariami.madrassa.repository.WishRepository;
import fr.rmariami.madrassa.repository.search.WishSearchRepository;
import fr.rmariami.madrassa.web.rest.util.HeaderUtil;
import fr.rmariami.madrassa.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Wish.
 */
@RestController
@RequestMapping("/api")
public class WishResource {

    private final Logger log = LoggerFactory.getLogger(WishResource.class);
        
    @Inject
    private WishRepository wishRepository;
    
    @Inject
    private WishSearchRepository wishSearchRepository;
    
    /**
     * POST  /wishes : Create a new wish.
     *
     * @param wish the wish to create
     * @return the ResponseEntity with status 201 (Created) and with body the new wish, or with status 400 (Bad Request) if the wish has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/wishes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Wish> createWish(@RequestBody Wish wish) throws URISyntaxException {
        log.debug("REST request to save Wish : {}", wish);
        if (wish.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("wish", "idexists", "A new wish cannot already have an ID")).body(null);
        }
        Wish result = wishRepository.save(wish);
        wishSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/wishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("wish", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wishes : Updates an existing wish.
     *
     * @param wish the wish to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated wish,
     * or with status 400 (Bad Request) if the wish is not valid,
     * or with status 500 (Internal Server Error) if the wish couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/wishes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Wish> updateWish(@RequestBody Wish wish) throws URISyntaxException {
        log.debug("REST request to update Wish : {}", wish);
        if (wish.getId() == null) {
            return createWish(wish);
        }
        Wish result = wishRepository.save(wish);
        wishSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("wish", wish.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wishes : get all the wishes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of wishes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/wishes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Wish>> getAllWishes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Wishes");
        Page<Wish> page = wishRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wishes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wishes/:id : get the "id" wish.
     *
     * @param id the id of the wish to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the wish, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/wishes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Wish> getWish(@PathVariable Long id) {
        log.debug("REST request to get Wish : {}", id);
        Wish wish = wishRepository.findOne(id);
        return Optional.ofNullable(wish)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /wishes/:id : delete the "id" wish.
     *
     * @param id the id of the wish to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/wishes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        log.debug("REST request to delete Wish : {}", id);
        wishRepository.delete(id);
        wishSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("wish", id.toString())).build();
    }

    /**
     * SEARCH  /_search/wishes?query=:query : search for the wish corresponding
     * to the query.
     *
     * @param query the query of the wish search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/wishes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Wish>> searchWishes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Wishes for query {}", query);
        Page<Wish> page = wishSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wishes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
