package com.artauction.artworkpostservices.Services.Interfaces;

import com.artauction.artworkpostservices.Dtos.Tag.TagDto;

import java.util.List;

public interface TagServices {
    public List<TagDto> getAllTags();
}
