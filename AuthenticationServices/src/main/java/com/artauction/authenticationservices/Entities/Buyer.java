package com.artauction.authenticationservices.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Buyer")
@Getter
@Setter
public class Buyer extends User {
    // Attributes
    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(length = 150)
    private String address;

    @Column(length = 13)
    private String phoneNumber;

    // Relationships live in AuctionServices and ArtworkPostServices
}
