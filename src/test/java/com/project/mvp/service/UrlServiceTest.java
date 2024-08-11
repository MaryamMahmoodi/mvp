package com.project.mvp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mvp.model.service.contract.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UrlServiceTest
{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UrlMappingRepository repository;

    @Test
    public void testCreateShortenedUrlAndResolve() throws JsonProcessingException
    {
        String originalUrl = "https://www.exame.de";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"originalUrl\": \"" + originalUrl + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/url/shorten", requestEntity, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
        String shortCode = responseBody.get("shortCode").asText();
        assertNotNull(shortCode);

        // Resolve the shortened URL
        ResponseEntity<String> resolveResponse = restTemplate.getForEntity("http://localhost:" + port + "/api/url/" + shortCode, String.class);

        assertEquals(HttpStatus.OK, resolveResponse.getStatusCode());

        JsonNode resolvedResponseBody = objectMapper.readTree(resolveResponse.getBody());
        String resolvedOriginalUrl = resolvedResponseBody.get("originalUrl").asText();

        assertEquals(originalUrl, resolvedOriginalUrl);

        // Clean up the database after test
        repository.deleteAll();

    }
}
