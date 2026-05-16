package com.artauction.artworkpostservices.Mappers.Tag;

import com.artauction.artworkpostservices.Dtos.Tag.TagDto;
import com.artauction.artworkpostservices.Entities.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);
    List<TagDto> toDtoList(List<Tag> tags);
}
