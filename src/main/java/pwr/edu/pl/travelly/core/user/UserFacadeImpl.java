package pwr.edu.pl.travelly.core.user;

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
import pwr.edu.pl.travelly.api.security.TokenProvider;
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

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    public UserFacadeImpl(final UserPort userPort,
                          final BCryptPasswordEncoder bcryptEncoder,
                          final TokenProvider jwtTokenUtil,
                          final AuthenticationManager authenticationManager) {
        this.userPort = userPort;
        this.bcryptEncoder = bcryptEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        final LoggedUserDto user = userPort.findByUserName(userName);
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), getAuthority(user));
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
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userPort.save(user);
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
