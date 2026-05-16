package com.artauction.artworkpostservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties
public class ArtworkPostServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtworkPostServicesApplication.class, args);
    }
}
