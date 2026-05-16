package com.artauction.artworkpostservices.Config.Security;

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
