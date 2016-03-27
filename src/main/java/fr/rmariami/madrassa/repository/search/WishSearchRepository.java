package fr.rmariami.madrassa.repository.search;

import fr.rmariami.madrassa.domain.Wish;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Wish entity.
 */
public interface WishSearchRepository extends ElasticsearchRepository<Wish, Long> {
}
