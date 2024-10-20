package gr.aueb.cf.datatest.authentication;

import gr.aueb.cf.datatest.core.excpetions.AppObjectNotAuthorizedException;
import gr.aueb.cf.datatest.core.excpetions.AppObjectNotFoundException;
import gr.aueb.cf.datatest.dto.AuthenticationRequestDTO;
import gr.aueb.cf.datatest.dto.AuthenticationResponseDTO;
import gr.aueb.cf.datatest.model.User;
import gr.aueb.cf.datatest.repository.UserRepository;
import gr.aueb.cf.datatest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {
        // Create an authentication token from username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        // If authentication was successful, generate JWT token
        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDTO(user.getFirstname(), user.getLastname(), token);
    }
}
