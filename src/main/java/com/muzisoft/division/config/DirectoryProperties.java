package com.muzisoft.division.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "directory")
@Getter
@Setter
public class DirectoryProperties {

    private Map<String, String> paths;
}
