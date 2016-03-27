package fr.rmariami.madrassa.repository.search;

import fr.rmariami.madrassa.domain.ClassRoom;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ClassRoom entity.
 */
public interface ClassRoomSearchRepository extends ElasticsearchRepository<ClassRoom, Long> {
}
