package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.PersonInCharge;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonInCharge entity.
 */
public interface PersonInChargeRepository extends JpaRepository<PersonInCharge,Long> {

}
