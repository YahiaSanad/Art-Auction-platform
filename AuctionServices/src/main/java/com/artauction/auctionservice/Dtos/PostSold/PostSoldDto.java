package com.artauction.auctionservice.Dtos.PostSold;

import com.artauction.auctionservice.Dtos.ArtworkPost.ArtworkPostDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PostSoldDto {
    private ArtworkPostDto artworkPost;
    private String buyerName;
    private BigDecimal finalPrice;
    private boolean isPaid;
}
