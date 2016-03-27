package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.Scholar;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Scholar entity.
 */
public interface ScholarRepository extends JpaRepository<Scholar,Long> {

    @Query("select distinct scholar from Scholar scholar left join fetch scholar.personInCharges")
    List<Scholar> findAllWithEagerRelationships();

    @Query("select scholar from Scholar scholar left join fetch scholar.personInCharges where scholar.id =:id")
    Scholar findOneWithEagerRelationships(@Param("id") Long id);

}
