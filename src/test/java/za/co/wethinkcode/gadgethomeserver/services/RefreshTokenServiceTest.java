package za.co.wethinkcode.gadgethomeserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;
import za.co.wethinkcode.gadgethomeserver.repository.RefreshTokenRepository;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

public class RefreshTokenServiceTest {

    RefreshTokenService service;
    RefreshTokenRepository tokenRepo;
    UserRepository userRepo;

    @BeforeEach
    void setup() {
        tokenRepo = mock(RefreshTokenRepository.class);
        userRepo = mock(UserRepository.class);

        service= new RefreshTokenService(tokenRepo, userRepo);
    }

    @Test
    void testCreateRefreshToken_NewToken() {
        when(userRepo.findById("user")).thenReturn(Optional.of(new User("user", "password")));
        when(tokenRepo.findByUser("user")).thenReturn(Optional.empty());

        RefreshToken token = service.createRefreshToken("user");

        assertEquals(RefreshToken.class, token.getClass());

        assertEquals("user", token.getUser());
        assertNotNull(token.getToken());
    }

    @Test  
    void testCreateRefreshToken_UserEmpty() {
        when(userRepo.findById("user")).thenReturn(Optional.empty());
        when(tokenRepo.findByUser("user")).thenReturn(Optional.empty());

        RefreshToken token = service.createRefreshToken("user");

        assertNull(token);
    }

    @Test
    void testDeleteRefreshToken() {
        RefreshToken token = new RefreshToken(new User("user", "password"));
        when(tokenRepo.findByUser("user")).thenReturn(Optional.of(token));
        
        assertTrue(service.deleteRefreshToken("user"));
    }

    @Test
    void testVerifyRefreshToken_True() {
        User user = new User("user", "password");
        RefreshToken token = new RefreshToken(user);

        when(tokenRepo.findByToken(token.getToken())).thenReturn(Optional.of(token));

        assertTrue(service.verifyRefreshToken(token.getToken(), "user"));
    }

    @Test
    void testVerifyRefreshToken_False() {
        User user = new User("user", "password");
        RefreshToken token = new RefreshToken(user);

        when(tokenRepo.findByToken(token.getToken())).thenReturn(Optional.of(token));

        assertFalse(service.verifyRefreshToken(token.getToken(), "user1"));
    }
}
