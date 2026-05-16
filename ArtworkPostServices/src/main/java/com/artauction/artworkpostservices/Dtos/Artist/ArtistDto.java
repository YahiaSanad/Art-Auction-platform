package com.artauction.artworkpostservices.Dtos.Artist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistDto {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private String country;
}
