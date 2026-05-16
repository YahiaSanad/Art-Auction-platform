package com.artauction.artworkpostservices.Config.IDs;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class WatchListId implements Serializable {
    @Column(name = "buyerId")
    private Integer buyerId;
    @Column(name = "artworkPostId")
    private Integer artworkPostId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WatchListId)) return false;
        WatchListId that = (WatchListId) o;
        return Objects.equals(buyerId, that.buyerId) &&
                Objects.equals(artworkPostId, that.artworkPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerId, artworkPostId);
    }
}
