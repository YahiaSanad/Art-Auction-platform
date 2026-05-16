package com.artauction.artistservices.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArtistDto {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private String country;

    public ArtistDto(Integer id, String name, String email, String country, String city, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country = country;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }
}
