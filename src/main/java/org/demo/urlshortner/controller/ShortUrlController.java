package org.demo.urlshortner.controller;

import org.demo.urlshortner.helper.Constants;
import org.demo.urlshortner.service.ShortUrlServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/url")
public class ShortUrlController {

    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);

    @Autowired
    public ShortUrlServiceImpl service;

    @PostMapping("/short")
    public ResponseEntity<?> shortenUrl(
            @RequestParam String url,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Long ttl) {
        log.info("URL entered is {} ",url);
        return service.createShortUrl(id, url, ttl);
    }

    @GetMapping("/{id}")
    public RedirectView redirectToOriginalUrl(@PathVariable String id) {
        if(service.getOriginalUrl(id).isPresent()) {
            String originalUrl = service.getOriginalUrl(id).get();
            StringBuilder uri = new StringBuilder();
            if(!originalUrl.startsWith(Constants.SCHEMA))
                uri.append(Constants.SCHEMA);
            uri.append(originalUrl);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(uri.toString());
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
            log.info("URL is "+redirectView);
            return redirectView;
        }else
            throw new IllegalArgumentException(Constants.URL_NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShortUrl(@PathVariable Long id) {
        service.deleteShortUrl(id);
        return ResponseEntity.noContent().build();
    }
}
