package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.PersonInCharge;
import fr.rmariami.madrassa.repository.PersonInChargeRepository;
import fr.rmariami.madrassa.repository.search.PersonInChargeSearchRepository;
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
 * REST controller for managing PersonInCharge.
 */
@RestController
@RequestMapping("/api")
public class PersonInChargeResource {

    private final Logger log = LoggerFactory.getLogger(PersonInChargeResource.class);
        
    @Inject
    private PersonInChargeRepository personInChargeRepository;
    
    @Inject
    private PersonInChargeSearchRepository personInChargeSearchRepository;
    
    /**
     * POST  /person-in-charges : Create a new personInCharge.
     *
     * @param personInCharge the personInCharge to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personInCharge, or with status 400 (Bad Request) if the personInCharge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-in-charges",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInCharge> createPersonInCharge(@Valid @RequestBody PersonInCharge personInCharge) throws URISyntaxException {
        log.debug("REST request to save PersonInCharge : {}", personInCharge);
        if (personInCharge.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personInCharge", "idexists", "A new personInCharge cannot already have an ID")).body(null);
        }
        PersonInCharge result = personInChargeRepository.save(personInCharge);
        personInChargeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/person-in-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personInCharge", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /person-in-charges : Updates an existing personInCharge.
     *
     * @param personInCharge the personInCharge to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personInCharge,
     * or with status 400 (Bad Request) if the personInCharge is not valid,
     * or with status 500 (Internal Server Error) if the personInCharge couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/person-in-charges",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInCharge> updatePersonInCharge(@Valid @RequestBody PersonInCharge personInCharge) throws URISyntaxException {
        log.debug("REST request to update PersonInCharge : {}", personInCharge);
        if (personInCharge.getId() == null) {
            return createPersonInCharge(personInCharge);
        }
        PersonInCharge result = personInChargeRepository.save(personInCharge);
        personInChargeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personInCharge", personInCharge.getId().toString()))
            .body(result);
    }

    /**
     * GET  /person-in-charges : get all the personInCharges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of personInCharges in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/person-in-charges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersonInCharge>> getAllPersonInCharges(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PersonInCharges");
        Page<PersonInCharge> page = personInChargeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/person-in-charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /person-in-charges/:id : get the "id" personInCharge.
     *
     * @param id the id of the personInCharge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personInCharge, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/person-in-charges/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PersonInCharge> getPersonInCharge(@PathVariable Long id) {
        log.debug("REST request to get PersonInCharge : {}", id);
        PersonInCharge personInCharge = personInChargeRepository.findOne(id);
        return Optional.ofNullable(personInCharge)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /person-in-charges/:id : delete the "id" personInCharge.
     *
     * @param id the id of the personInCharge to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/person-in-charges/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePersonInCharge(@PathVariable Long id) {
        log.debug("REST request to delete PersonInCharge : {}", id);
        personInChargeRepository.delete(id);
        personInChargeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personInCharge", id.toString())).build();
    }

    /**
     * SEARCH  /_search/person-in-charges?query=:query : search for the personInCharge corresponding
     * to the query.
     *
     * @param query the query of the personInCharge search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/person-in-charges",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersonInCharge>> searchPersonInCharges(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of PersonInCharges for query {}", query);
        Page<PersonInCharge> page = personInChargeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/person-in-charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
