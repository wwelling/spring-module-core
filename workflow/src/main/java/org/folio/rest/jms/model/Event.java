package org.folio.rest.jms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class Event {

  private String triggerId;

  private TriggerType triggerType;

  private String pathPattern;

  private String method;

  private String tenant;

  private String path;

  private Map<String, String> headers;

  private JsonNode payload;

  private List<String> processDefinitionIds;

  private List<String> taskIds;

  // @formatter:off
  public Event(
    String triggerId,
    TriggerType triggerType,
    String pathPattern,
    String method,
    String tenant,
    String path
  ) {
  // @formatter:on
    this.triggerId = triggerId;
    this.triggerType = triggerType;
    this.pathPattern = pathPattern;
    this.tenant = tenant;
    this.path = path;
    this.method = method;
    this.headers = new HashMap<String, String>();
    this.payload = JsonNodeFactory.instance.objectNode();
    this.processDefinitionIds = new ArrayList<String>();
    this.taskIds = new ArrayList<String>();
  }

  // @formatter:off
  public Event(
    String triggerId,
    TriggerType triggerType,
    String pathPattern,
    String method,
    String tenant,
    String path,
    Map<String, String> headers
  ) {
  // @formatter:on
    this(triggerId, triggerType, pathPattern, method, tenant, path);
    this.headers = headers;
  }

  // @formatter:off
  public Event(
    String triggerId,
    TriggerType triggerType,
    String pathPattern,
    String method,
    String tenant,
    String path,
    Map<String, String> headers,
    JsonNode payload
  ) {
  // @formatter:on
    this(triggerId, triggerType, pathPattern, method, tenant, path, headers);
    this.payload = payload;
  }

  public String getTriggerId() {
    return triggerId;
  }

  public void setTriggerId(String triggerId) {
    this.triggerId = triggerId;
  }

  public TriggerType getTriggerType() {
    return triggerType;
  }

  public void setTriggerType(TriggerType triggerType) {
    this.triggerType = triggerType;
  }

  public String getPathPattern() {
    return pathPattern;
  }

  public void setPathPattern(String pathPattern) {
    this.pathPattern = pathPattern;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public JsonNode getPayload() {
    return payload;
  }

  public void setPayload(JsonNode payload) {
    this.payload = payload;
  }

  public List<String> getProcessDefinitionIds() {
    return processDefinitionIds;
  }

  public void setProcessDefinitionIds(List<String> processDefinitionIds) {
    this.processDefinitionIds = processDefinitionIds;
  }

  public List<String> getTaskIds() {
    return taskIds;
  }

  public void setTaskIds(List<String> taskIds) {
    this.taskIds = taskIds;
  }

}
