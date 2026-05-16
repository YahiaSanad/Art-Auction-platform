package com.artauction.auctionservice.Dtos.ArtworkPost;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBuyNowPriceDto {
    // Attributes
    private int artworkPostId;
    private BigDecimal buyNowPrice;

    // Constructor
    public UpdateBuyNowPriceDto(int artworkPostId, BigDecimal newBuyNowPrice) {
        this.artworkPostId = artworkPostId;
        this.buyNowPrice = newBuyNowPrice;
    }
}
