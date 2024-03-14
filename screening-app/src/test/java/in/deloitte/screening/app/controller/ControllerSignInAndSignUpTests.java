package in.deloitte.screening.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.deloitte.screening.app.exceptions.AuthorizationException;
import in.deloitte.screening.app.exceptions.UserSignupException;
import in.deloitte.screening.app.user.controllers.AuthenticationController;
import in.deloitte.screening.app.user.dto.JWTRequest;
import in.deloitte.screening.app.user.dto.JWTResponse;
import in.deloitte.screening.app.user.dto.SignUpDto;
import in.deloitte.screening.app.user.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ControllerSignInAndSignUpTests {
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private AuthenticationController authController;
    @Autowired
    private MockMvc mockMvc;
    private SignUpDto dto;

    @BeforeEach
    public void setUp() {
        dto = new SignUpDto("shwlandge@deloitte.com", "Shweta@123", "Shweta@123");
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @AfterEach
    public void tearDown() {
        dto = null;
        mockMvc = null;
    }

    @Test
    @DisplayName("test for signup user success")
    public void testUserSaveSuccess() throws Exception {
        when(userService.SaveSignUp(any())).thenReturn("SUCCESS");
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(dto)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("test for signup user failure")
    public void testUserSaveFailure() {

        when(userService.SaveSignUp(dto))
                .thenThrow(new UserSignupException("User Already Registered!"));

        Assertions.assertThrows(UserSignupException.class, () -> authController.signUp(dto));

        verify(userService, times(1)).SaveSignUp(dto);
    }

    @Test
    @DisplayName("test for signIn user success")
    public void testUserLoginSuccess() throws Exception {
        JWTResponse response = new JWTResponse();
        response.setEmail("shwlandge@deloitte.com");
        response.setUsername("shwlandge");
        response.setJwtToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1yc2F3YW50MyIsImlhdCI6MTcxMDMwODc4MywiZXhwIjoxNzEwMzEyMzgzfQ.bbCH3QIOGuGL5VBv67LjCEJFpeGArLERsMNGMxfUpuE");
        when(userService.validateLogin(any())).thenReturn(response);
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertJsonToString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.username").value("shwlandge"))
                .andExpect(jsonPath("$.email").value("shwlandge@deloitte.com"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("test for signIn user failed")
    public void testUserLoginFailed() {

        JWTRequest request = new JWTRequest();
        request.setEmail("nonexistingemail@deloitte.com");
        request.setPassString("password");
        when(userService.validateLogin(request))
                .thenThrow(new AuthorizationException("Credentials Not Valid!"));

        Assertions.assertThrows(AuthorizationException.class, () -> authController.login(request));

        verify(userService, times(1)).validateLogin(request);
    }

    private String convertJsonToString(final Object obj) {
        String jsonString;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonString;
    }

}
