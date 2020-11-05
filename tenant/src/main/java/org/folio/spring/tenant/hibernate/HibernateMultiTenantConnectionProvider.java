package org.folio.spring.tenant.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.folio.spring.tenant.properties.BuildInfo;
import org.folio.spring.tenant.properties.Tenant;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HibernateMultiTenantConnectionProvider implements MultiTenantConnectionProvider {

  private static final long serialVersionUID = 5748544845255283079L;

  @Value("${spring.datasource.platform:h2}")
  private String platform;

  @Autowired
  private Tenant tenantProperties;

  @Autowired
  private BuildInfo buildInfoProperties;

  @Autowired
  private DataSource dataSource;

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenant) throws SQLException {
    final Connection connection = getAnyConnection();
    try {
      switch (platform) {
      case "h2":
        connection.createStatement().execute("USE " + toSchema(tenant));
        break;
      case "postgres":
        connection.setSchema(toSchema(tenant));
        break;
      default:
        throw new HibernateException("Unknown datasource platform [" + platform + "]");
      }
    } catch (SQLException e) {
      throw new HibernateException("Could not alter JDBC connection to use schema [" +  toSchema(tenant) + "]", e);
    }
    return connection;
  }

  @Override
  public void releaseConnection(String tenant, Connection connection) throws SQLException {
    try {
      switch (platform) {
      case "h2":
        connection.createStatement().execute("USE " + toSchema(tenantProperties.getDefaultTenant()));
        break;
      case "postgres":
        connection.setSchema(toSchema(tenantProperties.getDefaultTenant()));
        break;
      default:
        throw new HibernateException("Unknown datasource platform [" + platform + "]");
      }
    } catch (SQLException e) {
      throw new HibernateException("Could not alter JDBC connection to use schema [" + toSchema(tenantProperties.getDefaultTenant()) + "]", e);
    }
    connection.close();
  }

  @Override
  @SuppressWarnings("rawtypes")
  public boolean isUnwrappableAs(Class unwrapType) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return null;
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return true;
  }

  private String toSchema(String tenant) {
    return String.format("%s_%s", tenant, buildInfoProperties.getArtifact()).replace("-", "_").toUpperCase();
  }

}
