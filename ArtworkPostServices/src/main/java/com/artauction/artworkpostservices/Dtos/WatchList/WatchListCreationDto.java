package com.artauction.artworkpostservices.Dtos.WatchList;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WatchListCreationDto {
    private Integer buyerId;
    private Integer artworkPostId;
}
