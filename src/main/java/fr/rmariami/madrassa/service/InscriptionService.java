package fr.rmariami.madrassa.service;

import fr.rmariami.madrassa.domain.Inscription;
import fr.rmariami.madrassa.web.rest.dto.InscriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Inscription.
 */
public interface InscriptionService {

    /**
     * Save a inscription.
     * 
     * @param inscriptionDTO the entity to save
     * @return the persisted entity
     */
    InscriptionDTO save(InscriptionDTO inscriptionDTO);

    /**
     *  Get all the inscriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Inscription> findAll(Pageable pageable);

    /**
     *  Get the "id" inscription.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    InscriptionDTO findOne(Long id);

    /**
     *  Delete the "id" inscription.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the inscription corresponding to the query.
     * 
     *  @param query the query of the search
     *  @return the list of entities
     */
    Page<Inscription> search(String query, Pageable pageable);
}
