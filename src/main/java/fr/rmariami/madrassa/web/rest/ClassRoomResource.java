package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.ClassRoom;
import fr.rmariami.madrassa.repository.ClassRoomRepository;
import fr.rmariami.madrassa.repository.search.ClassRoomSearchRepository;
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
 * REST controller for managing ClassRoom.
 */
@RestController
@RequestMapping("/api")
public class ClassRoomResource {

    private final Logger log = LoggerFactory.getLogger(ClassRoomResource.class);
        
    @Inject
    private ClassRoomRepository classRoomRepository;
    
    @Inject
    private ClassRoomSearchRepository classRoomSearchRepository;
    
    /**
     * POST  /class-rooms : Create a new classRoom.
     *
     * @param classRoom the classRoom to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classRoom, or with status 400 (Bad Request) if the classRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/class-rooms",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClassRoom> createClassRoom(@Valid @RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to save ClassRoom : {}", classRoom);
        if (classRoom.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("classRoom", "idexists", "A new classRoom cannot already have an ID")).body(null);
        }
        ClassRoom result = classRoomRepository.save(classRoom);
        classRoomSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/class-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("classRoom", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /class-rooms : Updates an existing classRoom.
     *
     * @param classRoom the classRoom to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classRoom,
     * or with status 400 (Bad Request) if the classRoom is not valid,
     * or with status 500 (Internal Server Error) if the classRoom couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/class-rooms",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClassRoom> updateClassRoom(@Valid @RequestBody ClassRoom classRoom) throws URISyntaxException {
        log.debug("REST request to update ClassRoom : {}", classRoom);
        if (classRoom.getId() == null) {
            return createClassRoom(classRoom);
        }
        ClassRoom result = classRoomRepository.save(classRoom);
        classRoomSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("classRoom", classRoom.getId().toString()))
            .body(result);
    }

    /**
     * GET  /class-rooms : get all the classRooms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of classRooms in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/class-rooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClassRoom>> getAllClassRooms(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ClassRooms");
        Page<ClassRoom> page = classRoomRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/class-rooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /class-rooms/:id : get the "id" classRoom.
     *
     * @param id the id of the classRoom to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classRoom, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/class-rooms/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClassRoom> getClassRoom(@PathVariable Long id) {
        log.debug("REST request to get ClassRoom : {}", id);
        ClassRoom classRoom = classRoomRepository.findOne(id);
        return Optional.ofNullable(classRoom)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /class-rooms/:id : delete the "id" classRoom.
     *
     * @param id the id of the classRoom to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/class-rooms/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteClassRoom(@PathVariable Long id) {
        log.debug("REST request to delete ClassRoom : {}", id);
        classRoomRepository.delete(id);
        classRoomSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("classRoom", id.toString())).build();
    }

    /**
     * SEARCH  /_search/class-rooms?query=:query : search for the classRoom corresponding
     * to the query.
     *
     * @param query the query of the classRoom search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/class-rooms",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClassRoom>> searchClassRooms(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ClassRooms for query {}", query);
        Page<ClassRoom> page = classRoomSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/class-rooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
