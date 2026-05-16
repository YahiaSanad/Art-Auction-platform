package com.artauction.auctionservice.Config.IDs;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PostBidId implements Serializable {
    private Integer buyerId;
    private Integer artworkPostId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostBidId)) return false;
        PostBidId that = (PostBidId) o;
        return Objects.equals(buyerId, that.buyerId) &&
                Objects.equals(artworkPostId, that.artworkPostId);
    }

    @Override public int hashCode() {
        return Objects.hash(buyerId, artworkPostId);
    }
}
