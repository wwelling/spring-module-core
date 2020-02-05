package org.folio.spring.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

  @Value("${http.connection-timeout:3600}")
  private int connectionTimeout;

  @Value("${http.read-timeout:3600}")
  private int readTimeout;

  private RestTemplate restTemplate;

  public HttpService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder
        .setConnectTimeout(Duration.ofSeconds(connectionTimeout))
        .setReadTimeout(Duration.ofSeconds(readTimeout))
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
