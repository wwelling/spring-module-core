package org.folio.spring.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.folio.spring.converter.ObjectPlainTextConverter;

@Service
public class HttpService {

  private static final Logger logger = LoggerFactory.getLogger(HttpService.class);

  @Value("${http.connection-timeout:3600}")
  private int connectionTimeout;

  @Value("${http.read-timeout:3600}")
  private int readTimeout;

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  private RestTemplate restTemplate;

  @PostConstruct
  public void setup() {
    logger.info("Rest template connection timeout: {} seconds", connectionTimeout);
    logger.info("Rest template read timeout: {} seconds", readTimeout);

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(new ObjectPlainTextConverter(StandardCharsets.UTF_8));

    this.restTemplate = restTemplateBuilder
        .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
        .setReadTimeout(Duration.ofSeconds(readTimeout))
        .additionalMessageConverters(messageConverters)
        .build();
  }

  public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
    return this.restTemplate.exchange(url, method, request, responseType, (Object[]) new String[0]);
  }

  // @formatter:off
  public <T> ResponseEntity<T> exchange(
    String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType, Object[] uriVariables
  ) {
  // @formatter:on
    return this.restTemplate.exchange(url, method, request, responseType, uriVariables);
  }

}
