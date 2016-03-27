package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.Scholar;
import fr.rmariami.madrassa.repository.ScholarRepository;
import fr.rmariami.madrassa.repository.search.ScholarSearchRepository;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Scholar.
 */
@RestController
@RequestMapping("/api")
public class ScholarResource {

    private final Logger log = LoggerFactory.getLogger(ScholarResource.class);
        
    @Inject
    private ScholarRepository scholarRepository;
    
    @Inject
    private ScholarSearchRepository scholarSearchRepository;
    
    /**
     * POST  /scholars : Create a new scholar.
     *
     * @param scholar the scholar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scholar, or with status 400 (Bad Request) if the scholar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scholars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scholar> createScholar(@Valid @RequestBody Scholar scholar) throws URISyntaxException {
        log.debug("REST request to save Scholar : {}", scholar);
        if (scholar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scholar", "idexists", "A new scholar cannot already have an ID")).body(null);
        }
        Scholar result = scholarRepository.save(scholar);
        scholarSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scholars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scholar", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scholars : Updates an existing scholar.
     *
     * @param scholar the scholar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scholar,
     * or with status 400 (Bad Request) if the scholar is not valid,
     * or with status 500 (Internal Server Error) if the scholar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/scholars",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scholar> updateScholar(@Valid @RequestBody Scholar scholar) throws URISyntaxException {
        log.debug("REST request to update Scholar : {}", scholar);
        if (scholar.getId() == null) {
            return createScholar(scholar);
        }
        Scholar result = scholarRepository.save(scholar);
        scholarSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scholar", scholar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scholars : get all the scholars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scholars in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/scholars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Scholar>> getAllScholars(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Scholars");
        Page<Scholar> page = scholarRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scholars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scholars/:id : get the "id" scholar.
     *
     * @param id the id of the scholar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scholar, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/scholars/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scholar> getScholar(@PathVariable Long id) {
        log.debug("REST request to get Scholar : {}", id);
        Scholar scholar = scholarRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(scholar)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scholars/:id : delete the "id" scholar.
     *
     * @param id the id of the scholar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/scholars/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScholar(@PathVariable Long id) {
        log.debug("REST request to delete Scholar : {}", id);
        scholarRepository.delete(id);
        scholarSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scholar", id.toString())).build();
    }

    /**
     * SEARCH  /_search/scholars?query=:query : search for the scholar corresponding
     * to the query.
     *
     * @param query the query of the scholar search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/scholars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Scholar>> searchScholars(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Scholars for query {}", query);
        Page<Scholar> page = scholarSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/scholars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
