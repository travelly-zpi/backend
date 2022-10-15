package pwr.edu.pl.travelly.persistence.localisation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pwr.edu.pl.travelly.persistence.common.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "localisation")
public class Localisation extends AbstractEntity {
    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;
}
