package fr.rmariami.madrassa.repository.search;

import fr.rmariami.madrassa.domain.PersonInCharge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PersonInCharge entity.
 */
public interface PersonInChargeSearchRepository extends ElasticsearchRepository<PersonInCharge, Long> {
}
