package com.artauction.artworkpostservices.Dtos.PostTag;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostTagCreationDto {
    @NotBlank
    private Integer artworkPostId;

    @NotBlank
    private Integer tagId;
}
