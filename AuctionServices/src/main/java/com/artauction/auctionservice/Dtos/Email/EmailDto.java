package com.artauction.auctionservice.Dtos.Email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String subject;
    private String email;
    private String body;
}
