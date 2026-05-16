package com.artauction.authenticationservices.Dtos.Buyer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerDto {
    // Attributes
    private String name;
    private String email;

    // Constructors
    public BuyerDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
