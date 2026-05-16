package com.artauction.auctionservice.Dtos.PostBid;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class BuyerPostBidDto {
    private Integer artworkPostId;
    private String title;
    private BigDecimal buyerPrice;
    private LocalDateTime bidTime;
}
