package com.artauction.artworkpostservices.Dtos.ArtworkPost;

import com.artauction.artworkpostservices.Entities.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArtworkPostCreationDto {
    @NotBlank
    private String title;

    @NotBlank
    @Length(max = 300)
    private String description;

    @NotNull
    @Positive
    private BigDecimal initialPrice;
    private BigDecimal buyNowPrice;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @NotNull
    private MultipartFile image;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer artistId;

    @NotEmpty
    private Integer[] tagIds;
}
