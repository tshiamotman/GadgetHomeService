package za.co.wethinkcode.gadgethomeserver.controller;


import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.wethinkcode.gadgethomeserver.models.domain.AuthenticationResponseDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.service.RefreshTokenService;
import za.co.wethinkcode.gadgethomeserver.service.UserDetailsService;
import za.co.wethinkcode.gadgethomeserver.util.SessionToken;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    protected final Log logger = LogFactory.getLog(getClass());

    final AuthenticationManager authenticationManager;
    final UserDetailsService userDetailsService;
    final RefreshTokenService refreshTokenService;
    final SessionToken token;

    public AuthenticationController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService,
                                    UserDetailsService userDetailsService, SessionToken token) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
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
                RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(userLogin.getUserName());
                return ResponseEntity.ok(
                    new AuthenticationResponseDto(false, "Logged In")
                        .setToken(token)
                        .setUser(userDetailsService.getUserDto(userLogin.getUserName()))
                        .setRefreshToken(refreshToken.getToken())
                        .getResponseBody()
                    );
            } else {
                return ResponseEntity.status(401).body(
                    new AuthenticationResponseDto(true, "Invalid Credentials")
                        .getResponseBody()
                    );
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new AuthenticationResponseDto(true, "User is disabled")
                    .getResponseBody()
                );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(
                new AuthenticationResponseDto(true, "Invalid Credentials")
                    .getResponseBody()
                );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new AuthenticationResponseDto(true, e.getMessage())
                    .getResponseBody()
                );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto registerUser) {
        try {
            UserDetails userDetails = userDetailsService.createUserDetails(registerUser);
            String token = this.token.generateToken(userDetails);
            RefreshToken refreshToken = this.refreshTokenService.createRefreshToken(registerUser.getUserName());
            return ResponseEntity.ok(
                    new AuthenticationResponseDto(false, "Account Created Successfully")
                        .setToken(token)
                        .setUser(registerUser)
                        .setRefreshToken(refreshToken.getToken())
                        .getResponseBody()
                    );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(409).body(
                new AuthenticationResponseDto(true, e.getMessage())
                    .getResponseBody()
                );
        }
    }

    @GetMapping("/refresh/{refreshToken}/{username}")
    public ResponseEntity<?> getRefreshToken(@PathVariable String refreshToken, @PathVariable String username) {
        
        if(this.refreshTokenService.verifyRefreshToken(refreshToken, username)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = this.token.generateToken(userDetails);
            return ResponseEntity.ok(
                new AuthenticationResponseDto(false, "Token Refresh Successful")
                    .setToken(token)
                    .setUser(userDetailsService.getUserDto(username))
                    .setRefreshToken(refreshToken)
                    .getResponseBody()
            );
        }

        return ResponseEntity.status(401).body(
            new AuthenticationResponseDto(true, "Invalid Credentials")
                .getResponseBody()
        );
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String tokenString) {
        String token = tokenString.substring(7);
        
        this.token.invalidateToken(token);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.getPrincipal();
        if(authentication.isAuthenticated()) {
            String username = authentication.getName();
            this.refreshTokenService.deleteRefreshToken(username);
            return ResponseEntity.ok(Map.of(
                "message", "User logged out",
                "user", username
            ));
        }

        return null;
    }
}
