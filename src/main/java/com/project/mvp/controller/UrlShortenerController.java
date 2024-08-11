package com.project.mvp.controller;

import com.project.mvp.model.entity.Url;
import com.project.mvp.model.service.imp.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController
{

    @Autowired
    private UrlShortenerService service;

    @PostMapping("/shorten")
    public ResponseEntity<String> createShortenedUrl(@RequestBody String originalUrl)
    {
        Url createdUrl = service.createShortenedUrl(originalUrl);
        return new ResponseEntity<>(createdUrl.getShortCode(), HttpStatus.CREATED);
       // return ResponseEntity.ok("http://localhost:8080/" + shortCode);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> redirectToOriginalUrl(@PathVariable String shortUrl)
    {
        String originalUrl = service.resolveUrl(shortUrl);
        return new ResponseEntity<>(originalUrl, HttpStatus.OK);
    }
}
