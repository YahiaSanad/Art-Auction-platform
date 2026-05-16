package com.artauction.artworkpostservices.Config.IDs;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class PostTagId implements Serializable {
    private Integer artworkPostId;
    private Integer tagId;

    // equals() and hashCode() required
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostTagId)) return false;
        PostTagId that = (PostTagId) o;
        return Objects.equals(artworkPostId, that.artworkPostId) &&
                Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artworkPostId, tagId);
    }
}