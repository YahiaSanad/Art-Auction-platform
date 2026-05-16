package com.artauction.authenticationservices.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfiguration {
    private String issuer;
    private String audience;
    private String secretKey;
    private int expiryHours;
}
