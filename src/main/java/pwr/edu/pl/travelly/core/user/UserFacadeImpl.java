package pwr.edu.pl.travelly.core.user;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
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
import pwr.edu.pl.travelly.api.security.TokenProvider;
import pwr.edu.pl.travelly.core.common.exception.ExistsException;
import pwr.edu.pl.travelly.core.user.dto.AuthResponse;
import pwr.edu.pl.travelly.core.user.dto.LoggedUserDto;
import pwr.edu.pl.travelly.core.user.dto.UserDto;
import pwr.edu.pl.travelly.core.user.form.CreateUserForm;
import pwr.edu.pl.travelly.core.user.form.LoginUserForm;
import pwr.edu.pl.travelly.core.user.form.UpdateUserForm;
import pwr.edu.pl.travelly.core.user.port.UserPort;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service("userFacade")
public class UserFacadeImpl implements UserFacade, UserDetailsService{

    private final UserPort userPort;
    private final BCryptPasswordEncoder bcryptEncoder;
    private final TokenProvider jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    private final String AZURE_CONNECTION = "AccountName=travelly;" +
            "AccountKey=GdmI12VBEQ8CzuVV9ezLbkXX2NYosROQnvz40eKXvU9e6AUa6eRFdsKkIkwhYYzYrhRK/HYGBJEB+AStxFhdwg==;" +
            "EndpointSuffix=core.windows.net;" +
            "DefaultEndpointsProtocol=https;";

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
    public UserDto update(final UpdateUserForm updateUserForm) throws IOException {
        if (userPort.existsByUserNameAndUuidNot(updateUserForm.getEmail(), updateUserForm.getUuid())) {
            throw new ExistsException("EMAIL_EXISTS");
        }

        if(Objects.nonNull(updateUserForm.getImage())) {
          uploadImage(updateUserForm.getImage(), updateUserForm.getUuid());
        }

        return userPort.update(updateUserForm);
    }

    private void uploadImage(final MultipartFile image, final UUID userUuid) throws IOException {
        BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(AZURE_CONNECTION)
                .containerName("images")
                .buildClient();

        BlobClient blob = containerClient.getBlobClient(userUuid.toString());
        blob.upload(image.getInputStream(), image.getSize(), true);
    }

    private boolean falsePassword(final CharSequence rawPassword, final String encodedPassword){
        return !this.bcryptEncoder.matches(rawPassword, encodedPassword);
    }

}
