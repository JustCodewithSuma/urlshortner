package org.demo.urlshortner.service;

import org.demo.urlshortner.entity.EShortUrl;
import org.demo.urlshortner.helper.Constants;
import org.demo.urlshortner.helper.SequenceGenerator;
import org.demo.urlshortner.helper.ShortUrlValidator;
import org.demo.urlshortner.helper.UniqueIdCreator;
import org.demo.urlshortner.repository.ShortUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ShortUrlServiceImpl implements IShortUrlService{

    Logger logger = LoggerFactory.getLogger(ShortUrlServiceImpl.class);

    @Autowired
    private final UniqueIdCreator uniqueIdCreator;

    @Autowired
    private final ShortUrlRepository repository;

    public ShortUrlServiceImpl(ShortUrlRepository repository) {
        this.uniqueIdCreator = UniqueIdCreator.getInstance();
        this.repository = repository;
    }

    @Override
    public ResponseEntity<String> createShortUrl(String id, String originalUrl, Long ttl)  {

        logger.info("Starting to Create ShortUrl");
        if(!ShortUrlValidator.validateURL(originalUrl)) {
            throw new IllegalArgumentException(Constants.INVALID_URL);
        }
        EShortUrl shortUrl = new EShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setTtl(ttl);

        if (id != null) {
            Long searchId = uniqueIdCreator.retrieveUniqueId(id);
            if(repository.existsById(searchId))
                throw new IllegalArgumentException(Constants.ID_EXISTS);
            else{
                shortUrl.setId(searchId);
                repository.save(shortUrl);
                return new ResponseEntity<>(id, HttpStatus.CREATED);
            }
        }
        shortUrl.setId(SequenceGenerator.generatePrimaryKey());
        logger.info("Saving data into DB");
        EShortUrl db = repository.save(shortUrl);
        logger.info("Generating Unique ID");
        String shortUniqueId = uniqueIdCreator.createUniqueId(db.getId());
        return new ResponseEntity<>(shortUniqueId, HttpStatus.CREATED);
    }

    @Override
    public Optional<String> getOriginalUrl(String id) {
        logger.info("Getting the Id for the entered short url");
        Long searchId = uniqueIdCreator.retrieveUniqueId(id);
        logger.info("ID Found in DB");
        return repository.findById(searchId)
                .filter(ShortUrlValidator::isNotExpired)
                .map(EShortUrl::getOriginalUrl);
    }

    @Override
    public void deleteShortUrl(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    @Scheduled(fixedDelay = 24,timeUnit = TimeUnit.HOURS)
    public void cleanUpExpiredUrls() {
        repository.deleteByCreatedAtBefore(LocalDateTime.now());
    }

}
