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

  // @formatter:off
  public ResponseEntity<?> exchange(
    String url, HttpMethod method, HttpEntity<?> request, Class<?> responseType, Object[] uriVariables
  ) {
  // @formatter:on
    return this.restTemplate.exchange(url, method, request, responseType, uriVariables);
  }

}
