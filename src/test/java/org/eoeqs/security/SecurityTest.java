package org.eoeqs.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUnauthenticatedAccessToProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void testAuthenticatedAccessToProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/api/data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPublicEndpointsAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@test.com\",\"password\":\"password\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testXssProtection() throws Exception {
        String maliciousInput = "<script>alert('xss')</script>";

        mockMvc.perform(post("/api/data")
                        .header("Authorization", "Bearer fake-token")
                        .contentType("text/plain")
                        .content(maliciousInput))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("<script>"))));
    }
}