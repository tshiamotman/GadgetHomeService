package za.co.wethinkcode.gadgethomeserver.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import za.co.wethinkcode.gadgethomeserver.models.User;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.services.UserDetailsService;
import za.co.wethinkcode.gadgethomeserver.util.SessionToken;

import java.util.HashMap;
import java.util.Map;

//Authentication authentication = SecurityContextHolder
//        .getContext().getAuthentication();
//        String username = authentication.getName();

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    protected final Log logger = LogFactory.getLog(getClass());

    final UserRepository userRepository;
    final AuthenticationManager authenticationManager;
    final UserDetailsService userDetailsService;
    final SessionToken token;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService, SessionToken token) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.token = token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> map) {
        String username = map.get("user_name");
        String password = map.get("password");
        Map<String, Object> responseMap = new HashMap<>();
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username
                    , password));
            if (auth.isAuthenticated()) {
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String token = this.token.generateToken(userDetails);
                responseMap.put("error", false);
                responseMap.put("message", "Logged In");
                responseMap.put("token", token);
                responseMap.put("user", userRepository.findUserByUserName(username));
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("error", true);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "User is disabled");
            return ResponseEntity.status(500).body(responseMap);
        } catch (BadCredentialsException e) {
            responseMap.put("error", true);
            responseMap.put("message", "Invalid Credentials");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> map) {
        String userName = map.get("user_name");
        Map<String, Object> responseMap = new HashMap<>();
        User user = new User();
        user.setFirstName(map.get("first_name"));
        user.setLastName(map.get("last_name"));
        user.setEmail(map.get("email"));
        user.setNumber(map.get("number"));
        user.setPassword(new BCryptPasswordEncoder().encode(map.get("password")));
        user.setRole("USER");
        user.setUserName(userName);
        try {
            if(userRepository.existsByUserName(user.getUserName())){
                responseMap.put("error", true);
                responseMap.put("message", "Username exists");
                return ResponseEntity.ok(responseMap);
            }

            userRepository.save(user);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            String token = this.token.generateToken(userDetails);
            userRepository.save(user);
            responseMap.put("error", false);
            responseMap.put("username", userName);
            responseMap.put("message", "Account created successfully");
            responseMap.put("user", userRepository.findUserByUserName(userName));
            responseMap.put("token", token);

        } catch (SQLGrammarException e) {
            e.printStackTrace();
            responseMap.put("error", true);
            responseMap.put("message", "Something went wrong");
        }
        return ResponseEntity.ok(responseMap);
    }
}
