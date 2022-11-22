package pwr.edu.pl.travelly.core.user;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
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
import org.springframework.web.multipart.MultipartFile;
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
import pwr.edu.pl.travelly.core.user.form.UpdatePasswordForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;

import java.io.IOException;
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

    private final String AZURE_CONNECTION = "AccountName=travelly;" +
            "AccountKey=GdmI12VBEQ8CzuVV9ezLbkXX2NYosROQnvz40eKXvU9e6AUa6eRFdsKkIkwhYYzYrhRK/HYGBJEB+AStxFhdwg==;" +
            "EndpointSuffix=core.windows.net;" +
            "DefaultEndpointsProtocol=https;";

    private final String PROFILE_IMAGE_PREFIX = "profile_";

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;
    @Value("https://proud-pond-0b8236103.2.azurestaticapps.net/")   //"http://localhost:3000"
    private String baseURL;

    private final BlobContainerClient containerClient;

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

        containerClient = new BlobContainerClientBuilder()
                .connectionString(AZURE_CONNECTION)
                .containerName("images")
                .buildClient();

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
        final UserDto user = userPort.findByUuid(uuid);
        final BlobClient profileImageBlobClient = containerClient.getBlobClient(PROFILE_IMAGE_PREFIX+user.getUuid().toString());
        if(profileImageBlobClient.exists()) {
            user.setImageUrl(PROFILE_IMAGE_PREFIX+user.getUuid().toString());
            user.setImageCreationDate(profileImageBlobClient.getProperties().getCreationTime());
        }
        return user;
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
    public UserDto update(final UpdateUserForm updateUserForm) throws IOException {
        if (userPort.existsByUserNameAndUuidNot(updateUserForm.getEmail(), updateUserForm.getUuid())) {
            throw new ExistsException("EMAIL_EXISTS");
        }
        final UserDto updatedUser = userPort.update(updateUserForm);
        return updatedUser;
    }


    @Override
    public void updatePassword(final UpdatePasswordForm updateUserForm) {
        final String currentPassword = userPort.getUserPassword(updateUserForm.getUuid());
        if(falsePassword(updateUserForm.getOldPassword(), currentPassword)) {
            throw new IllegalArgumentException("FALSE_PASSWORD");
        }
        userPort.setNewUserPassword(updateUserForm.getUuid(), bcryptEncoder.encode(updateUserForm.getNewPassword()));
    }

    @Override
    public void uploadImage(final MultipartFile image, final UUID userUuid) throws IOException {
        final UserDto user = userPort.findByUuid(userUuid);
        final BlobClient blob = containerClient.getBlobClient(PROFILE_IMAGE_PREFIX+user.getUuid().toString());
        if(blob.exists()) {
            blob.delete();
        }
        blob.upload(image.getInputStream(), image.getSize(), true);
    }

    @Override
    public void removeImage(final UUID userUuid) {
        final UserDto user = userPort.findByUuid(userUuid);
        final BlobClient blob = containerClient.getBlobClient(PROFILE_IMAGE_PREFIX+user.getUuid().toString());
        blob.delete();
    }

    private boolean falsePassword(final CharSequence rawPassword, final String encodedPassword){
        return !this.bcryptEncoder.matches(rawPassword, encodedPassword);
    }

}
