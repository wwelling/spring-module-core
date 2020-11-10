package org.folio.spring.tenant.config;

import static org.hibernate.MultiTenancyStrategy.SCHEMA;
import static org.hibernate.cfg.AvailableSettings.C3P0_ACQUIRE_INCREMENT;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_MIN_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_TIMEOUT;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER;
import static org.hibernate.cfg.AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class HibernateMultiTenantConfig {

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  @Primary
  // @formatter:off
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    DataSource dataSource,
    JpaProperties jpaProperties,
    MultiTenantConnectionProvider multiTenantConnectionProvider,
    CurrentTenantIdentifierResolver currentTenantIdentifierResolver
  ) {
  // @formatter:on
    Map<String, Object> properties = new HashMap<>();
    properties.putAll(jpaProperties.getProperties());
    properties.put(MULTI_TENANT, SCHEMA);
    properties.put(MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    properties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
    properties.put(C3P0_MIN_SIZE, 4);
    properties.put(C3P0_MAX_SIZE, 16);
    properties.put(C3P0_ACQUIRE_INCREMENT, 5);
    properties.put(C3P0_TIMEOUT, 30000);
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan("org.folio.rest");
    em.setJpaVendorAdapter(jpaVendorAdapter());
    em.setJpaPropertyMap(properties);
    return em;
  }

}
