package com.artauction.auctionservice.Entities;

import com.artauction.auctionservice.Config.IDs.PostBidId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_bids")
@Getter
@Setter
public class PostBid {
    // Attributes
    @EmbeddedId
    private PostBidId id = new PostBidId();

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal buyerPrice;

    @Column(nullable = false)
    private LocalDateTime bidTime = LocalDateTime.now();
}
