package pwr.edu.pl.travelly.api.email.context;

import org.springframework.web.util.UriComponentsBuilder;
import pwr.edu.pl.travelly.core.user.dto.UserDto;

public class AccountVerificationEmailContext extends AbstractEmailContext{
    @Override
    public <T> void init(T context){
        UserDto user = (UserDto) context; // pass the user info
        put("firstName", user.getFirstName());
        setTemplateLocation("email-verification");   // email template location in resources/templates
        setSubject("Complete your registration");
        setFrom("travelly.zpi@gmail.com");
        setTo(user.getUserName());
    }

    public void setToken(String token) {
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/register/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}
