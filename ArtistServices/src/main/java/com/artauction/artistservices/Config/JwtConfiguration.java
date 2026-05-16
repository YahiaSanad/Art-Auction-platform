package com.artauction.artistservices.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfiguration {
    private String secretKey;
    private String issuer;
    private String audience;
    private int expiryHours;
}
