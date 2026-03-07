package com.seyran.authservice.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProtectedEndpointIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testProtectedEndpointWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/protected", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
    @Test
    public void testProtectedEndpointWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String loginJson = "{\"email\":\"testuser@example.com\",\"password\":\"123456\"}";
        HttpEntity<String> loginEntity = new HttpEntity<>(loginJson, headers);
        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/auth/login", loginEntity, String.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        String token = loginResponse.getBody();
        assertNotNull(token);
        HttpHeaders protectedHeaders = new HttpHeaders();
        protectedHeaders.setBearerAuth(token);
        HttpEntity<String> protectedEntity = new HttpEntity<>(protectedHeaders);

        ResponseEntity<String> protectedResponse=restTemplate.exchange(
                "/api/protected",
                HttpMethod.GET,
                protectedEntity,
                String.class
        );
        assertEquals(HttpStatus.OK, protectedResponse.getStatusCode());
        assertEquals("This is a protected resource", protectedResponse.getBody());
    }
}
