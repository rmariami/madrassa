package fr.rmariami.madrassa.repository.search;

import fr.rmariami.madrassa.domain.Scholar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Scholar entity.
 */
public interface ScholarSearchRepository extends ElasticsearchRepository<Scholar, Long> {
}
