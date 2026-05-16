package com.artauction.auctionservice.Dtos.PostBid;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PostBidCreationDto {
    @NotNull
    private Integer buyerId;

    @NotNull
    private Integer artworkPostId;

    @NotNull
    @Positive
    private BigDecimal buyerPrice;
}
