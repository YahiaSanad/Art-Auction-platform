package com.artauction.artworkpostservices.Services.Implementations;

import com.artauction.artworkpostservices.Dtos.Tag.TagDto;
import com.artauction.artworkpostservices.Entities.Tag;
import com.artauction.artworkpostservices.Mappers.Tag.TagMapper;
import com.artauction.artworkpostservices.Repositories.Interfaces.TagRepo;
import com.artauction.artworkpostservices.Services.Interfaces.TagServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServicesImp implements TagServices {
    // Attributes
    private final TagRepo tagRepo;
    private final TagMapper tagMapper;

    // Methods
    @Override
    public List<TagDto> getAllTags() {
        List<Tag> tags = tagRepo.findAll();
        return tagMapper.toDtoList(tags);
    }
}
