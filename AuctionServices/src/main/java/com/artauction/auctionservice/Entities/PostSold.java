package com.artauction.auctionservice.Entities;

import com.artauction.auctionservice.Config.IDs.PostSoldId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "post_solds")
@Getter
@Setter
public class PostSold {
    // Attributes
    @EmbeddedId
    private PostSoldId id = new PostSoldId();

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal finalPrice;
    private boolean isPaid = false;
}
