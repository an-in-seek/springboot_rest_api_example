package com.example.restapi.file.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.servlet.multipart")
public class FileProperties {
    private final Boolean enabled;
    private final String location;
    private final String maxFileSize;
    private final String maxRequestSize;
    private final String fileSizeThreshold;
}
