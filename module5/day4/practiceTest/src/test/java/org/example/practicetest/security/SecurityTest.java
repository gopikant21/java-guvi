package org.example.practicetest.security;

import org.example.practicetest.controller.AuthController;
import org.example.practicetest.controller.MerchantController;
import org.example.practicetest.dto.auth.LoginResponse;
import org.example.practicetest.service.AuthService;
import org.example.practicetest.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class, MerchantController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthService authService;

    @MockBean
    private MerchantService merchantService;

    @Test
    void loginAndRegisterShouldBePublic() throws Exception {
        when(authService.login(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new LoginResponse("token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"secret\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"XYZ\",\"email\":\"user@example.com\",\"taxId\":\"AB12CD34EF\",\"password\":\"secret\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void missingJwtShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/merchants"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidJwtShouldReturnUnauthorized() throws Exception {
        when(jwtService.extractUsername("bad-token")).thenThrow(new IllegalArgumentException("invalid"));

        mockMvc.perform(get("/api/merchants").header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expiredJwtShouldReturnUnauthorized() throws Exception {
        when(jwtService.extractUsername("expired-token")).thenThrow(new IllegalStateException("expired"));

        mockMvc.perform(get("/api/merchants").header("Authorization", "Bearer expired-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validJwtShouldAccessProtectedEndpoint() throws Exception {
        when(jwtService.extractUsername("good-token")).thenReturn("merchant@xyz.com");
        when(jwtService.isTokenValid("good-token", "merchant@xyz.com")).thenReturn(true);
        when(merchantService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/merchants").header("Authorization", "Bearer good-token"))
                .andExpect(status().isOk());
    }
}


