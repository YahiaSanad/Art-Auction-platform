package com.artauction.artworkpostservices.Entities;

import com.artauction.artworkpostservices.Config.IDs.PostTagId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "post_tags")
@Getter
@Setter
public class PostTag {
    // Attributes
    @EmbeddedId
    private PostTagId id = new PostTagId();

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("artworkPostId")
    @JoinColumn(name = "artworkPost_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArtworkPost artworkPost;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tag tag;
}
