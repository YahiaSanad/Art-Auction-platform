package com.artauction.artworkpostservices.Dtos.ArtworkPost;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArtworkPostDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal initialPrice;
    private BigDecimal buyNowPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private String artistName;
    private String categoryName;
    private String[] tags;
}
