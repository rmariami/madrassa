package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.Wish;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Wish entity.
 */
public interface WishRepository extends JpaRepository<Wish,Long> {

}
