package pwr.edu.pl.travelly.core.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private final String _token;
    private final long expiresIn;
    private final LoggedUserDto user;

    public AuthResponse(final String _token,
                        final long _tokenExpDate,
                        final LoggedUserDto user){
        this._token = _token;
        this.expiresIn = _tokenExpDate;
        this.user = user;
    }

}
