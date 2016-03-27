package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.ClassRoom;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClassRoom entity.
 */
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {

}
