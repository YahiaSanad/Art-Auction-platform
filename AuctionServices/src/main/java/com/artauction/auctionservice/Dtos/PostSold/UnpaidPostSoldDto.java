package com.artauction.auctionservice.Dtos.PostSold;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UnpaidPostSoldDto {
    private Integer buyerId;
    private Integer artworkPostId;
    private String artworkPostTitle;
    private BigDecimal finalPrice;
}
