package pwr.edu.pl.travelly.persistence.localisation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.edu.pl.travelly.persistence.localisation.entity.Localisation;

public interface LocalisationRepository extends JpaRepository<Localisation,Long> {
    boolean existsByCountryAndCity(final String country, final String city);
    Localisation findLocalisationByCountryAndCity(final String country, final String city);
}
