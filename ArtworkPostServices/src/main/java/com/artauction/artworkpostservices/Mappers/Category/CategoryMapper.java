package com.artauction.artworkpostservices.Mappers.Category;

import com.artauction.artworkpostservices.Dtos.Category.CategoryDto;
import com.artauction.artworkpostservices.Entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    List<CategoryDto> toDtoList(List<Category> categories);
}
