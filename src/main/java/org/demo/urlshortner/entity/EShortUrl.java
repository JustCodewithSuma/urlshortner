package org.demo.urlshortner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EShortUrl {
    @Id
    private Long id; // Custom or auto-generated short ID

    @Column(nullable = false)
    private String originalUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Long ttl; // Optional TTL in seconds

    // Getters and setters
}
