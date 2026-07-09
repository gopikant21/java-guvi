package org.example.backend.integration.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void customerShouldRegisterLoginAndFetchProfile() throws Exception {
        String email = "flow-customer@test.local";
        String registerPayload = """
                {
                  "name": "Flow Customer",
                  "email": "%s",
                  "password": "password123",
                  "phone": "9876500001"
                }
                """.formatted(email);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated());

        String loginPayload = """
                {
                  "email": "%s",
                  "password": "password123"
                }
                """.formatted(email);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        String profileResponse = mockMvc.perform(get("/api/customer/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode profile = objectMapper.readTree(profileResponse);
        assertThat(profile.get("email").asText()).isEqualTo(email);
        assertThat(profile.get("role").asText()).isEqualTo("CUSTOMER");
    }

    @Test
    void adminShouldLoginAndAccessDashboard() throws Exception {
        String adminLoginPayload = """
                {
                  "email": "admin@test.local",
                  "password": "Admin@123"
                }
                """;

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(adminLoginPayload))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        String dashboardResponse = mockMvc.perform(get("/api/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode dashboard = objectMapper.readTree(dashboardResponse);
        assertThat(dashboard.has("totalCustomers")).isTrue();
        assertThat(dashboard.has("totalLoans")).isTrue();
    }
}

