package org.demo.urlshortner.helper;

import org.demo.urlshortner.entity.EShortUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShortUrlValidator {

    static Logger logger = LoggerFactory.getLogger(ShortUrlValidator.class);

    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

    public static boolean validateURL(String url) {
        logger.info("Validating URL");
        Matcher m = URL_PATTERN.matcher(url);
        return m.matches();
    }

    public static boolean isNotExpired(EShortUrl shortUrl) {
        logger.info("Checking for Expiration TTL ");
        if (shortUrl.getTtl() == null) {
            return true;
        }
        LocalDateTime expiry = shortUrl.getCreatedAt().plusSeconds(shortUrl.getTtl());
        return LocalDateTime.now().isBefore(expiry);
    }
}
