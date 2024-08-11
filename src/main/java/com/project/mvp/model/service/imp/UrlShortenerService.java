package com.project.mvp.model.service.imp;

import com.project.mvp.model.entity.Url;
import com.project.mvp.model.exception.UrlNotFoundException;
import com.project.mvp.model.service.contract.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlShortenerService
{
    @Autowired
    private UrlMappingRepository repository;

    // Method to create a new shortened URL
    public Url createShortenedUrl(String originalUrl) {
        String shortCode = generateShortCode(originalUrl);
        Url url = new Url(originalUrl, shortCode);
        return repository.save(url);
    }


    public String resolveUrl(String shortUrl)
    {
            return repository.findByShortCode(shortUrl)
                    .map(Url::getOriginalUrl)
                    .orElseThrow(() -> new UrlNotFoundException("URL not found"));

    }


    private String generateShortCode(String originalUrl) {
        return Integer.toHexString(originalUrl.hashCode());
    }


//    private String generateShortUrl()
//    {
//        Random random = new Random();
//        StringBuilder shortCode = new StringBuilder();
//        for (int i = 0; i < 6; i++)
//        {
//            shortCode.append((char) ('a' + random.nextInt(26)));
//        }
//        return shortCode.toString();
//    }

}
