package com.spd.trello.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Data
@PropertySource(value = "/properties/application_local_storage.yml")
@Component
public class StorageProperties {
    @Value("${location}")
    private String location;
}
