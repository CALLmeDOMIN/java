package com.example.dev.config;

import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

@TestConfiguration
public class TestConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        EmbeddedPostgres postgres = EmbeddedPostgres.builder().start();
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url(postgres.getJdbcUrl("postgres", "postgres"))
                .username("postgres")
                .password("postgres")
                .build();
    }
}
