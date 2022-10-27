package pwr.edu.pl.travelly.core.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedUserDto {
    private UUID uuid;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String role;
    @JsonIgnore
    private Boolean enable;
}
