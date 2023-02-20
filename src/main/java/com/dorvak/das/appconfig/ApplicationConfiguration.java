package com.dorvak.das.appconfig;

import ink.organics.idgenerator.IDGeneratorManager;
import ink.organics.idgenerator.decorator.Decorator;
import ink.organics.idgenerator.generator.impl.SnowflakeGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public IDGeneratorManager idGeneratorManager() {
        return IDGeneratorManager.getInstance()
                .init(Decorator.builder()
                        .generatorId("snowflake_generator")
                        .generator(SnowflakeGenerator.build("server_1", List.of("server_1")))
                        .build());
    }
}
