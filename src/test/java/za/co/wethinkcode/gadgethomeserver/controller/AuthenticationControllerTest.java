package za.co.wethinkcode.gadgethomeserver.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import za.co.wethinkcode.gadgethomeserver.models.domain.AuthenticationResponseDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.RefreshToken;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.service.RefreshTokenService;
import za.co.wethinkcode.gadgethomeserver.service.UserDetailsService;
import za.co.wethinkcode.gadgethomeserver.util.SessionToken;

@AutoConfigureMockMvc
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest extends AbstractControllerTest {

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    SessionToken token;
    
    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
        
        UserDetails user = new User("user", "password", new ArrayList<>());

        UserDto userDto = new UserDto();

        BDDMockito.given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            "user", "password")))
            .willReturn(new TestingAuthenticationToken(user, null, new ArrayList<>()));

        BDDMockito.given(userDetailsService.loadUserByUsername("user")).willReturn(user);

        BDDMockito.given(userDetailsService.createUserDetails(userDto)).willReturn(user);

        BDDMockito.given(refreshTokenService.createRefreshToken("user"))
            .willReturn(new RefreshToken(new za.co.wethinkcode.gadgethomeserver.models.database.User("user", "password")));

        BDDMockito.given(refreshTokenService.verifyRefreshToken("1234567890", "user")).willReturn(true);

        BDDMockito.given(token.generateToken(user)).willReturn(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwibmFtZSI6IlVzZXIgVGVzdCIsImlhdCI6MTkxNjIzOTAyMn0.EvXUbYITRaF9bFoxeEfGf2byTsDSj4fl8O1Ay9kOd1I");

    }

    @Test
    void testGetRefreshToken() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders
                .get("/auth/refresh/{refreshToken}/{username}", "1234567890", "user")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        
        AuthenticationResponseDto res = mapFromJson(response.getContentAsString(), AuthenticationResponseDto.class);
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(res.getError());
        assertEquals("Token Refresh Successful", res.getMessage());
    }

    @Test
    void testLoginUser() throws JsonProcessingException, Exception {
        
        UserDto userDto = new UserDto();
        userDto.setUserName("user");
        userDto.setPassword("password");

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/auth/login")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(mapToJson(userDto).getBytes())
                                            .accept(MediaType.APPLICATION_JSON))
                                            .andReturn().getResponse();

        AuthenticationResponseDto res = mapFromJson(response.getContentAsString(), AuthenticationResponseDto.class);
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(res.getError());
        assertEquals("Logged In", res.getMessage());
    }

    @Test
    void testRegisterUser() throws JsonProcessingException, Exception {
        UserDto userDto = new UserDto();
        userDto.setUserName("user");
        userDto.setPassword("password");

        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/auth/register")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(mapToJson(userDto).getBytes())
                                            .accept(MediaType.APPLICATION_JSON))
                                            .andReturn().getResponse();

        AuthenticationResponseDto res = mapFromJson(response.getContentAsString(), AuthenticationResponseDto.class);
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertFalse(res.getError());
        assertEquals("Account Created Successfully", res.getMessage());

    }
}
