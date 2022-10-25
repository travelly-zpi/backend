package pwr.edu.pl.travelly.core.user;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import pwr.edu.pl.travelly.api.email.context.AccountVerificationEmailContext;
import pwr.edu.pl.travelly.api.security.TokenProvider;
import pwr.edu.pl.travelly.api.email.service.EmailService;
import pwr.edu.pl.travelly.core.common.exception.ExistsException;
import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade, UserDetailsService{

    private final UserPort userPort;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final TokenProvider jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;
    @Value("http://localhost:3000")
    private String baseURL;

    public UserFacadeImpl(final UserPort userPort,
                          final BCryptPasswordEncoder bcryptEncoder,
                          final TokenProvider jwtTokenUtil,
                          final AuthenticationManager authenticationManager,
                          final EmailService emailService) {
        this.userPort = userPort;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.emailService= emailService;
    }

    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        final LoggedUserDto user = userPort.findByUserName(userName);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(final LoggedUserDto user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
        return authorities;
    }

    @Override
    public UserDto save(final CreateUserForm user) {
        if(userPort.existsByUserName(user.getEmail())) {
            throw new ExistsException("EMAIL_EXISTS");
        }
        String password = user.getPassword();
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        UserDto userDto = userPort.save(user);
        sendRegistrationConfirmationEmail(userDto, user.getEmail(), password);
        return userDto;
    }

    private void sendRegistrationConfirmationEmail(UserDto user, String email, String password) {
        final Authentication authentication = authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(email, password));
        final String token = jwtTokenUtil.generateToken(authentication);

        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user);
        emailContext.setToken(token);
        emailContext.buildVerificationUrl(baseURL, token);
        try {
            emailService.sendEmail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyUser(String token) {
        if(StringUtils.isEmpty(token)){ throw new IllegalArgumentException("EMPTY_TOKEN"); }
        try {
            String userName = jwtTokenUtil.getUsernameFromToken(token);
            if (!userPort.existsByUserName(userName)) return;
            userPort.enableUser(userName);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("EXPIRED_TOKEN");
        } catch (JwtException e) {
            throw new IllegalArgumentException("INVALID_TOKEN");
        }
    }

    @Override
    public void resendVerification(final LoginUserForm loginUserForm) {
        String email = loginUserForm.getEmail();
        sendRegistrationConfirmationEmail(userPort.findByEmail(email), email, loginUserForm.getPassword());
    }

    @Override
    public UserDto findByUuid(final UUID uuid) {
        return userPort.findByUuid(uuid);
    }

    @Override
    public AuthResponse generateToken(final LoginUserForm loginUserForm) {
        final LoggedUserDto user = this.userPort.findByUserName(loginUserForm.getEmail());

        if(falsePassword(loginUserForm.getPassword(),user.getPassword())) {
            throw new IllegalArgumentException("FALSE_PASSWORD");
        }

        if (!user.getEnable()) { throw new IllegalArgumentException("NOT_ACTIVATED"); }

        final Authentication authentication = authenticationManager.
                authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginUserForm.getEmail(),
                                loginUserForm.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtTokenUtil.generateToken(authentication);
        return new AuthResponse(token,this.TOKEN_VALIDITY,user);
    }

    @Override
    public UserDto update(final UpdateUserForm updateUserForm) {
        if(userPort.existsByUserNameAndUuidNot(updateUserForm.getUserName(), updateUserForm.getUuid())) {
            throw new ExistsException("EMAIL_EXISTS");
        }
        return userPort.update(updateUserForm);
    }

    private boolean falsePassword(final CharSequence rawPassword, final String encodedPassword){
        return !this.bcryptEncoder.matches(rawPassword, encodedPassword);
    }

}
