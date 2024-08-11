package com.project.mvp.model.service.imp;

import com.project.mvp.model.entity.Url;
import com.project.mvp.model.exception.UrlNotFoundException;
import com.project.mvp.model.service.contract.UrlMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.net.URL;


@Service
public class UrlShortenerService
{
    @Autowired
    private UrlMappingRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);

    // Method to create a new shortened URL
    public Url createShortenedUrl(String originalUrl)
    {
        if (!isValidUrl(originalUrl))
        {
            logger.error("Invalid URL format: {}", originalUrl);
            throw new IllegalArgumentException("Invalid URL format");
        }
        // Check if the URL already exists
        Url existingUrl = findExistingUrl(originalUrl);
        if (existingUrl != null)
        {
            return existingUrl;
        }

        String shortCode = generateShortCode(originalUrl);
        Url url = new Url(originalUrl, shortCode);

        try
        {
            return repository.save(url);
        }
        catch (DataIntegrityViolationException e)
        {
            logger.error("DataIntegrityViolationException occurred: {}", e.getMessage());
            // Return the existing URL if it was inserted between the check and save
            return findExistingUrl(originalUrl);
        }
        catch (DataAccessResourceFailureException e)
        {
            logger.error("Database connection error: {}", e.getMessage());
            throw new RuntimeException("Database connection error", e);
        } //General Error Handling
        catch (Exception e)
        {
            logger.error("Unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }


    public String resolveUrl(String shortUrl)
    {
        return repository.findByShortCode(shortUrl)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found"));

    }


    private boolean isValidUrl(String urlStr)
    {
        try
        {
            new URL(urlStr).toURI();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private Url findExistingUrl(String originalUrl)
    {
        return repository.findByOriginalUrl(originalUrl).orElse(null);
    }

    private String generateShortCode(String originalUrl)
    {
        return Integer.toHexString(originalUrl.hashCode());
    }


}
