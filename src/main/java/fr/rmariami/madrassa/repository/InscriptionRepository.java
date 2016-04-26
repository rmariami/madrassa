package fr.rmariami.madrassa.repository;

import fr.rmariami.madrassa.domain.Inscription;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Inscription entity.
 */
public interface InscriptionRepository extends JpaRepository<Inscription,Long> {

    @Query("select inscription from Inscription inscription where inscription.author.login = ?#{principal}")
    List<Inscription> findByAuthorIsCurrentUser();

}
