package za.co.wethinkcode.gadgethomeserver.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.wethinkcode.gadgethomeserver.models.domain.AuthenticationDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.services.UserDetailsService;
import za.co.wethinkcode.gadgethomeserver.util.SessionToken;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    protected final Log logger = LogFactory.getLog(getClass());

    final AuthenticationManager authenticationManager;
    final UserDetailsService userDetailsService;
    final SessionToken token;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService, SessionToken token) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.token = token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userLogin) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getUserName(), userLogin.getPassword()));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin.getUserName());
                String token = this.token.generateToken(userDetails);
                return ResponseEntity.ok(
                    new AuthenticationDto(false, "Logged In")
                        .setToken(token)
                        .setUser(userLogin.getUserName())
                        .setRefreshToken("refreshToken")
                        .build()
                    );
            } else {
                return ResponseEntity.status(401).body(
                    new AuthenticationDto(true, "Invalid Credentials")
                        .build()
                    );
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new AuthenticationDto(true, "User is disabled")
                    .build()
                );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                new AuthenticationDto(true, "Invalid Credentials")
                    .build()
                );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new AuthenticationDto(true, e.getMessage())
                    .build()
                );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto registerUser) {
        try {
            UserDetails userDetails = userDetailsService.createUserDetails(registerUser);
            String token = this.token.generateToken(userDetails);
            return ResponseEntity.ok(
                    new AuthenticationDto(false, "Logged In")
                        .setToken(token)
                        .setUser(registerUser.getUserName())
                        .setRefreshToken("refreshToken")
                        .build()
                    );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(
                new AuthenticationDto(true, e.getMessage())
                    .build()
                );
        }
    }
}
