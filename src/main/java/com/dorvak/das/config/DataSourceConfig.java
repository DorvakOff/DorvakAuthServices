package com.dorvak.das.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private com.dorvak.das.config.Configuration config;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(config.getDriverClassName());
        dataSourceBuilder.url(config.getDatabaseHost());
        dataSourceBuilder.username(config.getDatabaseUsername());
        dataSourceBuilder.password(config.getDatabasePassword());
        return dataSourceBuilder.build();
    }

}
