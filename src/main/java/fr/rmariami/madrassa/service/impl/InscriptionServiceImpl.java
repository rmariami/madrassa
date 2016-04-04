package fr.rmariami.madrassa.service.impl;

import fr.rmariami.madrassa.service.InscriptionService;
import fr.rmariami.madrassa.domain.Inscription;
import fr.rmariami.madrassa.repository.InscriptionRepository;
import fr.rmariami.madrassa.repository.search.InscriptionSearchRepository;
import fr.rmariami.madrassa.web.rest.dto.InscriptionDTO;
import fr.rmariami.madrassa.web.rest.mapper.InscriptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Inscription.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService{

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);
    
    @Inject
    private InscriptionRepository inscriptionRepository;
    
    @Inject
    private InscriptionMapper inscriptionMapper;
    
    @Inject
    private InscriptionSearchRepository inscriptionSearchRepository;
    
    /**
     * Save a inscription.
     * 
     * @param inscriptionDTO the entity to save
     * @return the persisted entity
     */
    public InscriptionDTO save(InscriptionDTO inscriptionDTO) {
        log.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.inscriptionDTOToInscription(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        InscriptionDTO result = inscriptionMapper.inscriptionToInscriptionDTO(inscription);
        inscriptionSearchRepository.save(inscription);
        return result;
    }

    /**
     *  Get all the inscriptions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Inscription> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        Page<Inscription> result = inscriptionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one inscription by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public InscriptionDTO findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        Inscription inscription = inscriptionRepository.findOne(id);
        InscriptionDTO inscriptionDTO = inscriptionMapper.inscriptionToInscriptionDTO(inscription);
        return inscriptionDTO;
    }

    /**
     *  Delete the  inscription by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.delete(id);
        inscriptionSearchRepository.delete(id);
    }

    /**
     * Search for the inscription corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Inscription> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Inscriptions for query {}", query);
        return inscriptionSearchRepository.search(queryStringQuery(query), pageable);
    }
}
