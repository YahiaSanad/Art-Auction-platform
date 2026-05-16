package com.artauction.artworkpostservices.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artwork_posts")
@Getter
@Setter
public class ArtworkPost {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 300)
    private String description;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal initialPrice;

    @Column(precision = 18, scale = 2)
    private BigDecimal buyNowPrice;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Lob
    @Column(nullable = false)
    private byte[] image;

    // Cross-service — Auth Service owns these users, store ID only
    @Column(nullable = false)
    private Integer artistId;
    private Integer adminId;

    // Relationships
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId", nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Category category;

    @OneToMany(mappedBy = "artworkPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "artworkPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WatchList> watchLists = new ArrayList<>();
}
