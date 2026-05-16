package com.artauction.auctionservice.Dtos.PostSold;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BuyerPostSoldDto {
    private String title;
    private BigDecimal finalPrice;
    private boolean isPaid;
}
