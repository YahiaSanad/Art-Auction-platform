package com.artauction.artworkpostservices.Dtos.ArtworkPost;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ArtistArtworkPostDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal initialPrice;
    private BigDecimal buyNowPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private String categoryName;
    private Integer adminId;
    private String[] tags;
}
