package za.co.wethinkcode.gadgethomeserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

import java.util.Optional;

public class UserDetailsServiceTest {

    UserDetailsService service;
    UserRepository repo;

    UserMapper userMapper;

    @BeforeEach
    void setup() {
        repo = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);

        service = new UserDetailsService(repo, userMapper);
    }

    @Test
    void testcreateUserDetails() throws Exception {
        when(repo.existsByEmail("test@gadgethome.co")).thenReturn(false);
        when(repo.existsByUserName("user")).thenReturn(false);

        UserDto userDto = new UserDto();
        userDto.setUserName("user");
        userDto.setFirstName("User");
        userDto.setLastName("Test");
        userDto.setRole("USER");
        userDto.setNumber("0600000000");
        userDto.setEmail("test@gadgethome.co");
        userDto.setPassword("password");

        UserDetails user = service.createUserDetails(userDto);

        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertEquals(1, user.getAuthorities().size());

        user.getAuthorities().forEach(authority -> assertEquals("USER", authority.toString()));
    }

    @Test
    void testLoadUserByUsername() {
        User userDb = new User("user", new BCryptPasswordEncoder().encode("password"));
        userDb.setRole("USER");
        when(repo.findById("user")).thenReturn(Optional.of(userDb));

        UserDetails user = service.loadUserByUsername("user");

        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertEquals(1, user.getAuthorities().size());

        user.getAuthorities().forEach(authority -> assertEquals("USER", authority.toString()));
    }
}
