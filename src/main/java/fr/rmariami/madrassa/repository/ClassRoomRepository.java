package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.ClassRoom;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ClassRoom entity.
 */
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

    @Query("select distinct classRoom from ClassRoom classRoom left join fetch classRoom.scholars")
    List<ClassRoom> findAllWithEagerRelationships();

    @Query("select classRoom from ClassRoom classRoom left join fetch classRoom.scholars where classRoom.id =:id")
    ClassRoom findOneWithEagerRelationships(@Param("id") Long id);

}
