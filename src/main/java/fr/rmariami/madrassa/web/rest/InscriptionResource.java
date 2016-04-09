package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.Inscription;
import fr.rmariami.madrassa.service.InscriptionService;
import fr.rmariami.madrassa.web.rest.util.HeaderUtil;
import fr.rmariami.madrassa.web.rest.util.PaginationUtil;
import fr.rmariami.madrassa.web.rest.dto.InscriptionDTO;
import fr.rmariami.madrassa.web.rest.mapper.InscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Inscription.
 */
@RestController
@RequestMapping("/api")
public class InscriptionResource {

    private final Logger log = LoggerFactory.getLogger(InscriptionResource.class);
        
    @Inject
    private InscriptionService inscriptionService;
    
    @Inject
    private InscriptionMapper inscriptionMapper;
    
    /**
     * POST  /inscriptions : Create a new inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscriptionDTO, or with status 400 (Bad Request) if the inscription has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscriptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscriptionDTO> createInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to save Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inscription", "idexists", "A new inscription cannot already have an ID")).body(null);
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.created(new URI("/api/inscriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inscription", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inscriptions : Updates an existing inscription.
     *
     * @param inscriptionDTO the inscriptionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscriptionDTO,
     * or with status 400 (Bad Request) if the inscriptionDTO is not valid,
     * or with status 500 (Internal Server Error) if the inscriptionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscriptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscriptionDTO> updateInscription(@Valid @RequestBody InscriptionDTO inscriptionDTO) throws URISyntaxException {
        log.debug("REST request to update Inscription : {}", inscriptionDTO);
        if (inscriptionDTO.getId() == null) {
            return createInscription(inscriptionDTO);
        }
        InscriptionDTO result = inscriptionService.save(inscriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inscription", inscriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inscriptions : get all the inscriptions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of inscriptions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/inscriptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<InscriptionDTO>> getAllInscriptions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Inscriptions");
        Page<Inscription> page = inscriptionService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inscriptions");
        return new ResponseEntity<>(inscriptionMapper.inscriptionsToInscriptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /inscriptions/:id : get the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscriptionDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inscriptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscriptionDTO> getInscription(@PathVariable Long id) {
        log.debug("REST request to get Inscription : {}", id);
        InscriptionDTO inscriptionDTO = inscriptionService.findOne(id);
        return Optional.ofNullable(inscriptionDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inscriptions/:id : delete the "id" inscription.
     *
     * @param id the id of the inscriptionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inscriptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInscription(@PathVariable Long id) {
        log.debug("REST request to delete Inscription : {}", id);
        inscriptionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inscription", id.toString())).build();
    }

    /**
     * SEARCH  /_search/inscriptions?query=:query : search for the inscription corresponding
     * to the query.
     *
     * @param query the query of the inscription search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/inscriptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<InscriptionDTO>> searchInscriptions(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Inscriptions for query {}", query);
        Page<Inscription> page = inscriptionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/inscriptions");
        return new ResponseEntity<>(inscriptionMapper.inscriptionsToInscriptionDTOs(page.getContent()), headers, HttpStatus.OK);
    }

}
