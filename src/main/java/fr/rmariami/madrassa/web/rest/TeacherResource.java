package fr.rmariami.madrassa.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.rmariami.madrassa.domain.Teacher;
import fr.rmariami.madrassa.repository.TeacherRepository;
import fr.rmariami.madrassa.repository.search.TeacherSearchRepository;
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
 * REST controller for managing Teacher.
 */
@RestController
@RequestMapping("/api")
public class TeacherResource {

    private final Logger log = LoggerFactory.getLogger(TeacherResource.class);
        
    @Inject
    private TeacherRepository teacherRepository;
    
    @Inject
    private TeacherSearchRepository teacherSearchRepository;
    
    /**
     * POST  /teachers : Create a new teacher.
     *
     * @param teacher the teacher to create
     * @return the ResponseEntity with status 201 (Created) and with body the new teacher, or with status 400 (Bad Request) if the teacher has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/teachers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) throws URISyntaxException {
        log.debug("REST request to save Teacher : {}", teacher);
        if (teacher.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("teacher", "idexists", "A new teacher cannot already have an ID")).body(null);
        }
        Teacher result = teacherRepository.save(teacher);
        teacherSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/teachers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("teacher", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teachers : Updates an existing teacher.
     *
     * @param teacher the teacher to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated teacher,
     * or with status 400 (Bad Request) if the teacher is not valid,
     * or with status 500 (Internal Server Error) if the teacher couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/teachers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teacher> updateTeacher(@Valid @RequestBody Teacher teacher) throws URISyntaxException {
        log.debug("REST request to update Teacher : {}", teacher);
        if (teacher.getId() == null) {
            return createTeacher(teacher);
        }
        Teacher result = teacherRepository.save(teacher);
        teacherSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("teacher", teacher.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teachers : get all the teachers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of teachers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/teachers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Teacher>> getAllTeachers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Teachers");
        Page<Teacher> page = teacherRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teachers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /teachers/:id : get the "id" teacher.
     *
     * @param id the id of the teacher to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the teacher, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/teachers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Teacher> getTeacher(@PathVariable Long id) {
        log.debug("REST request to get Teacher : {}", id);
        Teacher teacher = teacherRepository.findOne(id);
        return Optional.ofNullable(teacher)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /teachers/:id : delete the "id" teacher.
     *
     * @param id the id of the teacher to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/teachers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        log.debug("REST request to delete Teacher : {}", id);
        teacherRepository.delete(id);
        teacherSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("teacher", id.toString())).build();
    }

    /**
     * SEARCH  /_search/teachers?query=:query : search for the teacher corresponding
     * to the query.
     *
     * @param query the query of the teacher search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/teachers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Teacher>> searchTeachers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Teachers for query {}", query);
        Page<Teacher> page = teacherSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/teachers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
