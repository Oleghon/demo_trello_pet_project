package com.spd.trello.configuration;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
public class SwaggerConfig {

    private final TypeResolver resolver;

    public SwaggerConfig(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Bean
    public Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spd.trello"))
                .build()
                .alternateTypeRules(
                        newRule(resolver.resolve(Sort.class), resolver.resolve(List.class, String.class)));
    }
}
