package org.demo.urlshortner.controller;

import org.demo.urlshortner.entity.EShortUrl;
import org.demo.urlshortner.service.ShortUrlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortUrlController.class)
public class ShortUrlControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShortUrlServiceImpl service;

    EShortUrl shortUrl = new EShortUrl();

    @BeforeEach
    public void setUp(){
        shortUrl.setId(1L);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setOriginalUrl("Testing");
    }

    @WithMockUser(username = "mock",password = "mock")
    @Test
    public void createShortUrlTest() throws Exception {
        when(service.createShortUrl(any(),any(),any())).thenReturn(new ResponseEntity<>("abc", HttpStatus.CREATED));
        ResultActions response = mockMvc.perform(post("/api/url/short").with(csrf()).queryParams(postParams()));
        response.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.content().string("abc"));
    }

    @Test
    public void createShortUrlTestNotAuthenticated() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/url/short").with(csrf()).queryParams(postParams()));
        response.andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "mock",password = "mock")
    @Test
    public void redirectToOriginalUrlTest() throws Exception {
        when(service.getOriginalUrl(any())).thenReturn(Optional.of("success"));
        ResultActions response = mockMvc.perform(get("/api/url/:id=1234").with(csrf()));
        response.andExpect(status().is3xxRedirection());
    }

    @WithMockUser(username = "mock",password = "mock")
    @Test
    public void redirectToOriginalUrlTestException() throws Exception {
        when(service.getOriginalUrl(any())).thenReturn(Optional.empty());
        ResultActions response = mockMvc.perform(get("/api/url/:id=1234").with(csrf()));
        response.andExpect(status().is4xxClientError());
    }

    public MultiValueMap<String,String> postParams(){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("url","testUrl");
        return params;
    }

    public MultiValueMap<String,String> getParams(){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id","12234abcde");
        return params;
    }
}
