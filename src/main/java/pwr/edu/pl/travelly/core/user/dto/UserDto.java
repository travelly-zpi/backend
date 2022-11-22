package pwr.edu.pl.travelly.core.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID uuid;
    @JsonProperty("email")
    private String userName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String languages;
    private String description;
    private String imageUrl;
    private OffsetDateTime imageCreationDate;
    @JsonIgnore
    private String role;
    private String localisation;
}
