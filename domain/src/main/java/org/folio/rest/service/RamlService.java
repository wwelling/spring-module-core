package org.folio.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.folio.rest.controller.exception.SchemaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class RamlService {

  private static final Pattern INCLUDE_MATCH_PATTERN = Pattern.compile("(?<=!include ).*");

  private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

  @Autowired
  private ResourcePatternResolver resolver;

  public List<String> getRamls() throws IOException {
    List<String> ramls = new ArrayList<>();
    Resource[] resources = resolver.getResources("classpath:ramls/*.raml");
    for (Resource resource : resources) {
      String name = resource.getFilename();
      ramls.add(name);
    }
    return ramls;
  }

  public JsonNode getRamlByName(@PathVariable String name, String okapiUrl) throws IOException {
    Resource resource = resolver.getResource("classpath:ramls/" + name);
    if (resource.exists()) {
      if (resource.isFile() && resource.getFilename().endsWith(".raml")) {
        return replaceReferences(IOUtils.toString(resource.getInputStream(), "UTF-8"), okapiUrl);
      }
    }
    throw new SchemaNotFoundException("RAML " + name + " not found");
  }

  private JsonNode replaceReferences(String raml, String okapiUrl) throws IOException {
    Matcher matcher = INCLUDE_MATCH_PATTERN.matcher(raml);
    StringBuffer sb = new StringBuffer(raml.length());
    while (matcher.find()) {
      String ref = matcher.group(0).substring(matcher.group(0).lastIndexOf("/") + 1);
      if (ref.endsWith(".raml")) {
        matcher.appendReplacement(sb, Matcher.quoteReplacement(okapiUrl + "/_/raml/" + ref));
      } else {
        matcher.appendReplacement(sb, Matcher.quoteReplacement(okapiUrl + "/_/jsonSchema/" + ref));
      }
    }
    matcher.appendTail(sb);
    return YAML_MAPPER.readValue(sb.toString(), JsonNode.class);
  }

}
