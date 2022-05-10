package com.spd.trello.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String location;
}
