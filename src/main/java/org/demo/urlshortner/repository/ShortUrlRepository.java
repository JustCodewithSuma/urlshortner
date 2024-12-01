package org.demo.urlshortner.repository;

import org.demo.urlshortner.entity.EShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ShortUrlRepository extends JpaRepository<EShortUrl, Long> {
    void deleteByCreatedAtBefore(LocalDateTime expiry);
}

