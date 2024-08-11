package com.project.mvp.controller;

import com.project.mvp.model.dto.UrlRequest;
import com.project.mvp.model.dto.UrlResponse;
import com.project.mvp.model.entity.Url;
import com.project.mvp.model.exception.UrlNotFoundException;
import com.project.mvp.model.service.imp.UrlShortenerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/url")
public class UrlShortenerController
{

    @Autowired
    private UrlShortenerService service;

    @Operation(summary = "Create a shortened URL")
    @ApiResponse(responseCode = "201", description = "URL shortened successfully")
    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> createShortenedUrl(@Valid @RequestBody UrlRequest urlRequest)
    {
        Url createdUrl = service.createShortenedUrl(urlRequest.getOriginalUrl());
        UrlResponse urlResponse = new UrlResponse(createdUrl.getShortCode());

        return new ResponseEntity<>(urlResponse, HttpStatus.CREATED);
    }


    @Operation(summary = "Resolve a shortened URL")
    @ApiResponse(responseCode = "200", description = "Original URL retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Short code not found")
    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlRequest> resolveUrl(@PathVariable String shortCode)
    {
        try
        {
            String originalUrl = service.resolveUrl(shortCode);
            UrlRequest urlRequest = new UrlRequest(originalUrl);
            return new ResponseEntity<>(urlRequest, HttpStatus.OK);
        }
        catch (UrlNotFoundException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
