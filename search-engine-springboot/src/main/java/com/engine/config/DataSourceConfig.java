package com.engine.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 普通数据库数据源
 */
@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "defaultDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSourceProperties druidDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "defaultDataSource")
    public DataSource ds1DataSource(@Qualifier("defaultDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

}
