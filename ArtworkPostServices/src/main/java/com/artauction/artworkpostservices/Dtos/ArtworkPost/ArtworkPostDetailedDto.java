package com.artauction.artworkpostservices.Dtos.ArtworkPost;

import com.artauction.artworkpostservices.Dtos.PostBid.PostBidDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ArtworkPostDetailedDto {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal initialPrice;
    private BigDecimal buyNowPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private String categoryName;
    private String artistName;
    private String[] tags;
    public List<PostBidDto> postBids;
}
