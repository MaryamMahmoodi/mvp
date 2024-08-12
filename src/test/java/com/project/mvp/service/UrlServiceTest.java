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

import static org.junit.jupiter.api.Assertions.*;

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


    //Test for URL Not Found Handling (HTTP 404 Not Found)
    @Test
    public void testResolveUrl_NotFound()
    {
        String nonExistentShortCode = "nonexistent";

        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/api/url/" + nonExistentShortCode, String.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Check if the body is null, as 404 responses often don't include a body
        String responseBody = responseEntity.getBody();
        if (responseBody != null)
        {
            assertTrue(responseBody.contains("URL not found"));
        }
    }

    //Test for Invalid Input Handling (HTTP 400 Bad Request)
    @Test
    public void testCreateShortenedUrl_InvalidInput()
    {
        String invalidUrl = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"url\": \"" + invalidUrl + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/url/shorten", requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Invalid URL"));
    }

    //Test for Null Input Handling (HTTP 400 Bad Request)
    @Test
    public void testCreateShortenedUrl_NullInput()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"url\": null}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/url/shorten", requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Invalid URL"));
    }


}
