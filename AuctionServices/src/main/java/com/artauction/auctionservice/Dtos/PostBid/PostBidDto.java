package com.artauction.auctionservice.Dtos.PostBid;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostBidDto {
    private String buyerName;
    private BigDecimal buyerPrice;
    private LocalDateTime bidTime;
}
