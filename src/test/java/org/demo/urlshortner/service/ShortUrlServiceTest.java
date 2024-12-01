package org.demo.urlshortner.service;

import org.demo.urlshortner.entity.EShortUrl;
import org.demo.urlshortner.helper.ShortUrlValidator;
import org.demo.urlshortner.helper.UniqueIdCreator;
import org.demo.urlshortner.repository.ShortUrlRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository repository;

    @InjectMocks
    private ShortUrlServiceImpl urlShortenerService ;

    @Mock
    private UniqueIdCreator idCreatorMock;

    public EShortUrl shortUrl = new EShortUrl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setOriginalUrl("mock");
        shortUrl.setId(1L);
        shortUrl.setTtl(120L);
    }

    @AfterEach
    void tearDown() throws Exception {
        Field instance = UniqueIdCreator.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void createShortUrl() {
        try(MockedStatic<ShortUrlValidator> validatorMockedStatic = mockStatic(ShortUrlValidator.class)){
            validatorMockedStatic.when(()-> ShortUrlValidator.validateURL(anyString())).thenReturn(true);
            boolean methodCallValue = ShortUrlValidator.validateURL(shortUrl.getOriginalUrl());
            assertTrue(methodCallValue);
            validatorMockedStatic.verify(()-> ShortUrlValidator.validateURL(anyString()),times(1));
            when(repository.existsById(anyLong())).thenReturn(false);
            when(repository.save(any())).thenReturn(shortUrl);
            when(idCreatorMock.createUniqueId(anyLong())).thenReturn("b");
            String mockedValue = idCreatorMock.createUniqueId(anyLong());
            verify(idCreatorMock,times(1)).createUniqueId(anyLong());
            when(idCreatorMock.retrieveUniqueId(anyString())).thenReturn(1L);
            Long uniqueId = idCreatorMock.retrieveUniqueId(anyString());
            verify(idCreatorMock,times(1)).retrieveUniqueId(anyString());
            String dbShortUrl = urlShortenerService.createShortUrl(null,shortUrl.getOriginalUrl(), null).getBody();
            assertEquals(dbShortUrl,mockedValue);
            assertEquals(uniqueId,1L);
        }
    }

    @Test
    void createShortUrlWithCustomId() {
        try(MockedStatic<ShortUrlValidator> validatorMockedStatic = mockStatic(ShortUrlValidator.class)){
            validatorMockedStatic.when(()-> ShortUrlValidator.validateURL(anyString())).thenReturn(true);
            boolean methodCallValue = ShortUrlValidator.validateURL(shortUrl.getOriginalUrl());
            assertTrue(methodCallValue);
            validatorMockedStatic.verify(()-> ShortUrlValidator.validateURL(anyString()),times(1));
            when(repository.existsById(anyLong())).thenReturn(false);
            when(repository.save(any())).thenReturn(shortUrl);
            when(idCreatorMock.createUniqueId(anyLong())).thenReturn("b");
            String mockedValue = idCreatorMock.createUniqueId(anyLong());
            verify(idCreatorMock,times(1)).createUniqueId(anyLong());
            when(idCreatorMock.retrieveUniqueId(anyString())).thenReturn(1L);
            Long uniqueId = idCreatorMock.retrieveUniqueId(anyString());
            verify(idCreatorMock,times(1)).retrieveUniqueId(anyString());
            String dbShortUrl = urlShortenerService.createShortUrl("14",shortUrl.getOriginalUrl(), null).getBody();
            assertEquals(dbShortUrl,"14");
            assertEquals(uniqueId,1L);
        }
    }

    @Test
    void createShortUrlWithInvalidUrlException() {
        try(MockedStatic<ShortUrlValidator> validatorMockedStatic = mockStatic(ShortUrlValidator.class)) {
            validatorMockedStatic.when(() -> ShortUrlValidator.validateURL(anyString())).thenReturn(false);
            boolean methodCallValue = ShortUrlValidator.validateURL(shortUrl.getOriginalUrl());
            assertFalse(methodCallValue);
            validatorMockedStatic.verify(() -> ShortUrlValidator.validateURL(anyString()), times(1));
            IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl("14", shortUrl.getOriginalUrl(), 1L));
            assertEquals(illegalArgumentException.getMessage(), "Invalid URL.Please check the entered URL.");
        }
    }

    @Test
    void createShortUrlWithIdExistsException() {
        try(MockedStatic<ShortUrlValidator> validatorMockedStatic = mockStatic(ShortUrlValidator.class)) {
            validatorMockedStatic.when(() -> ShortUrlValidator.validateURL(anyString())).thenReturn(true);
            boolean methodCallValue = ShortUrlValidator.validateURL(shortUrl.getOriginalUrl());
            assertTrue(methodCallValue);
            validatorMockedStatic.verify(() -> ShortUrlValidator.validateURL(anyString()), times(1));
            when(repository.existsById(anyLong())).thenReturn(true);
            IllegalArgumentException illegalArgumentException = Assertions.assertThrows(IllegalArgumentException.class, () -> urlShortenerService.createShortUrl("14", shortUrl.getOriginalUrl(), 1L));
            assertEquals(illegalArgumentException.getMessage(), "ID already exists");
        }
    }

    @Test
    void getOriginalUrl() {
        try(MockedStatic<ShortUrlValidator> validatorMockedStatic = mockStatic(ShortUrlValidator.class)) {
            validatorMockedStatic.when(() -> ShortUrlValidator.isNotExpired(any())).thenReturn(false);
            boolean methodCallValue = ShortUrlValidator.isNotExpired(any());
            assertFalse(methodCallValue);
            validatorMockedStatic.verify(() -> ShortUrlValidator.isNotExpired(any()), times(1));
            when(repository.findById(any())).thenReturn(Optional.of(shortUrl));
            when(idCreatorMock.retrieveUniqueId(anyString())).thenReturn(1L);
            Long mockedValue = idCreatorMock.retrieveUniqueId(anyString());
            assertEquals(mockedValue,shortUrl.getId());
            verify(idCreatorMock,times(1)).retrieveUniqueId(anyString());
            Optional<String> dbId = urlShortenerService.getOriginalUrl(anyString());
            assertFalse(dbId.isPresent());
        }
    }

    @Test
    void deleteShortUrl() {
    }

    @Test
    void cleanUpExpiredUrls() {
    }


    private void setSingeltonMock(UniqueIdCreator uniqueIdCreatorMock) {
        try {
            Field uniqueIdMock = UniqueIdCreator.class.getDeclaredField("instance");
            uniqueIdMock.setAccessible(true);
            uniqueIdMock.set(uniqueIdMock, uniqueIdCreatorMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}