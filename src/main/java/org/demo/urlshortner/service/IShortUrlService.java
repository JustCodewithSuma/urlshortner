package org.demo.urlshortner.service;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IShortUrlService {

    ResponseEntity<String> createShortUrl(String id, String originalUrl, Long ttl);

    Optional<String> getOriginalUrl(String id);

    void deleteShortUrl(Long id);

    void cleanUpExpiredUrls();
}
