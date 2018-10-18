package org.folio.rest.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpService {

  private RestTemplate restTemplate;

  public HttpService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType) {
    return this.restTemplate.exchange(url, method, request, responseType, new Object[0]);
  }

  // @formatter:off
  public <T> ResponseEntity<T> exchange(
    String url, HttpMethod method, HttpEntity<?> request, Class<T> responseType, Object[] uriVariables
  ) {
  // @formatter:on
    return this.restTemplate.exchange(url, method, request, responseType, uriVariables);
  }

}
