package fr.rmariami.madrassa.repository.search;

import fr.rmariami.madrassa.domain.Inscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Inscription entity.
 */
public interface InscriptionSearchRepository extends ElasticsearchRepository<Inscription, Long> {
}
