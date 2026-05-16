package com.artauction.auctionservice.Dtos.PostSold;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSoldPaidDto {
    private Integer buyerId;
    private Integer artworkPostId;
}
