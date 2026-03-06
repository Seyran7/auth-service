package com.seyran.authservice.integration;

import com.seyran.authservice.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public TestRestTemplate restTemplate() {
            return new TestRestTemplate();
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testRegisterAndLogin() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RegisterRequest> registerEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/auth/register", registerEntity, String.class);

        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
        assertEquals("User registered successfully", registerResponse.getBody());

        // 2️⃣ Login
        HttpEntity<RegisterRequest> loginEntity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/auth/login", loginEntity, String.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertTrue(loginResponse.getBody().startsWith("ey"));
    }
}