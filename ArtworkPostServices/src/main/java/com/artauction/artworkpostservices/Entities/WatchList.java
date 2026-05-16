package com.artauction.artworkpostservices.Entities;

import com.artauction.artworkpostservices.Config.IDs.WatchListId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "watch_lists")
@Getter
@Setter
public class WatchList {
    // Attributes
    @EmbeddedId
    private WatchListId id = new WatchListId();

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("artworkPostId")
    @JoinColumn(name = "artworkPostId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ArtworkPost artworkPost;
}
