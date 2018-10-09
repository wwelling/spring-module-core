package org.folio.rest.tenant.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.folio.rest.tenant.TenantConstants;
import org.folio.rest.tenant.config.AdditionalHibernateConfig;
import org.folio.rest.tenant.exception.TenantAlreadyExistsException;
import org.folio.rest.tenant.exception.TenantDoesNotExistsException;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

@Service
public class HibernateSchemaService {

  private final static String CONNECTION_DRIVER_CLASS = "connection.driver_class";
  private final static String DIALECT = "dialect";
  private final static String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
  private final static String HIBERNATE_DEFAULT_SCHEMA = "hibernate.default_schema";
  private final static String HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION = "hibernate.jdbc.lob.non_contextual_creation";
  private final static String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
  private final static String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
  private final static String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

  private final List<String> domainPackages = new ArrayList<String>();

  @Autowired
  private AdditionalHibernateConfig additionalHibernateConfig;

  @Autowired
  private SqlTemplateService sqlTemplateService;

  @Autowired
  private DataSourceProperties dataSourceProperties;

  @Autowired
  private JpaProperties jpaProperties;

  @Autowired
  private ResourceLoader resourceLoader;

  @PostConstruct
  private void initialize() throws SQLException, IOException {
    domainPackages.add("org.folio.rest.model");
    for (String additionalDomainPackage : additionalHibernateConfig.getDomainPackages()) {
      domainPackages.add(additionalDomainPackage);
    }
    // TODO: remove this in production to avoid generating tables for public schema
    Map<String, String> settings = getSettings(TenantConstants.DEFAULT_TENANT);
    Connection connection = getConnection(settings);
    initializeSchema(connection, settings);
    connection.close();
  }

  public void createTenant(String tenant) throws SQLException, IOException {
    Map<String, String> settings = getSettings(tenant);
    Connection connection = getConnection(settings);
    if (schemaExists(connection, tenant)) {
      throw new TenantAlreadyExistsException("Tenant already exists: " + tenant);
    }
    initializeSchema(connection, settings);
    connection.close();
  }

  public void deleteTenant(String tenant) throws SQLException {
    Map<String, String> settings = getSettings(tenant);
    Connection connection = getConnection(settings);
    if (!schemaExists(connection, tenant)) {
      throw new TenantDoesNotExistsException("Tenant does not exist: " + tenant);
    }
    dropSchema(connection, tenant);
    connection.close();
  }

  private void initializeSchema(Connection connection, Map<String, String> settings) throws SQLException, IOException {
    String schema = getSchema(settings);
    createSchema(connection, schema);
    createTables(settings);
    initializeData(connection, schema);
    createAdditionalSchema(connection, schema);
  }

  private void createSchema(Connection connection, String schema) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(String.format("CREATE SCHEMA IF NOT EXISTS %s;", schema.toUpperCase()));
    statement.close();
  }

  private void createTables(Map<String, String> settings) {
    MetadataImplementor metadata = buildMetadata(settings);
    SchemaExport schemaExport = new SchemaExport();
    schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
  }

  private void initializeData(Connection connection, String schema) throws SQLException {
    Statement statement = connection.createStatement();
    statement.execute(sqlTemplateService.templateImportSql(schema));
    statement.close();
  }

  private void createAdditionalSchema(Connection connection, String schema) throws SQLException, IOException {
    Statement statement = connection.createStatement();
    statement.execute(sqlTemplateService.templateSelectSchemaSql(schema));
    for (String path : additionalHibernateConfig.getSchemaScripts()) {
      Resource resource = resourceLoader.getResource(path);
      File script;
      if (resource.getURI().getScheme().equals("jar")) {
        script = File.createTempFile("schema", ".sql");
        script.deleteOnExit();
        IOUtils.copy(resource.getInputStream(), new FileOutputStream(script));
      } else {
        script = resource.getFile();
      }
      statement.execute(FileUtils.readFileToString(script));
    }
    statement.close();
  }

  private void dropSchema(Connection connection, String schema) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(String.format("DROP SCHEMA IF EXISTS %s CASCADE;", schema.toUpperCase()));
    statement.close();
  }

  private boolean schemaExists(Connection connection, String schema) throws SQLException {
    Statement statement = connection.createStatement();
    String queryTemplate = "SELECT EXISTS(SELECT 1 FROM information_schema.schemata WHERE schema_name = '%s');";
    ResultSet resultSet = statement.executeQuery(String.format(queryTemplate, schema.toUpperCase()));
    resultSet.next();
    boolean exists = resultSet.getBoolean(1);
    statement.close();
    return exists;
  }

  private Map<String, String> getSettings(String schema) {
    Map<String, String> settings = new HashMap<String, String>();
    settings.put(CONNECTION_DRIVER_CLASS, dataSourceProperties.getDriverClassName());
    settings.put(DIALECT, jpaProperties.getDatabasePlatform());
    settings.put(HIBERNATE_CONNECTION_URL, dataSourceProperties.getUrl());
    settings.put(HIBERNATE_DEFAULT_SCHEMA, schema);
    settings.put(HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION, "true");
    settings.put(HIBERNATE_CONNECTION_USERNAME, dataSourceProperties.getUsername());
    settings.put(HIBERNATE_CONNECTION_PASSWORD, dataSourceProperties.getPassword());
    settings.put(HIBERNATE_HBM2DDL_AUTO, "none");
    settings.put("show_sql", String.valueOf(jpaProperties.isShowSql()));
    return settings;
  }

  private String getSchema(Map<String, String> settings) {
    return settings.get(HIBERNATE_DEFAULT_SCHEMA);
  }

  private Connection getConnection(Map<String, String> settings) throws SQLException {
    String jdbcUrl = settings.get(HIBERNATE_CONNECTION_URL);
    String username = settings.get(HIBERNATE_CONNECTION_USERNAME);
    String password = settings.get(HIBERNATE_CONNECTION_PASSWORD);
    return DriverManager.getConnection(jdbcUrl, username, password);
  }

  private MetadataImplementor buildMetadata(Map<String, String> settings) {
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(settings).build();
    MetadataSources sources = addEntities(new MetadataSources(registry));
    return (MetadataImplementor) sources.getMetadataBuilder().build();
  }

  private MetadataSources addEntities(MetadataSources sources) {
    ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
    for (String domainPackage : domainPackages) {
      for (BeanDefinition beanDefinition : scanner.findCandidateComponents(domainPackage)) {
        sources.addAnnotatedClassName(beanDefinition.getBeanClassName());
      }
    }
    return sources;
  }

}
