package pwr.edu.pl.travelly.persistence.localisation.entity;

import pwr.edu.pl.travelly.core.localisation.dto.LocalisationDto;

public class LocalisationMapper {

    public static LocalisationDto toDto(final Localisation localisation) {
        return LocalisationDto.builder()
                .country(localisation.getCountry())
                .city(localisation.getCity())
                .build();
    }

}
