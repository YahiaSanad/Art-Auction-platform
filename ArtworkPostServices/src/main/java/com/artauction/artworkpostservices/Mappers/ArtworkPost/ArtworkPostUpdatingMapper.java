package com.artauction.artworkpostservices.Mappers.ArtworkPost;

import com.artauction.artworkpostservices.Config.IDs.PostTagId;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostUpdatingDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import com.artauction.artworkpostservices.Entities.Category;
import com.artauction.artworkpostservices.Entities.PostTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtworkPostUpdatingMapper {
    // Map tags
    @Mapping(target = "postTags", expression = "java(toPostTags(dto.getTagIds()))")
    @Mapping(target = "category", expression = "java(toCategory(dto.getCategoryId()))")
    @Mapping(target = "buyNowPrice", source = "buyNowPrice")

    // Manually mapped
    @Mapping(target = "image", ignore = true)

    // approved latter
    @Mapping(target = "adminId",  ignore = true)

    // Ignore cross-service and navigation properties
    @Mapping(target = "watchLists", ignore = true)

    // Note: Artist entity → Artist microservice, ignore navigation property
    @Mapping(target = "artistId", ignore = true)
    ArtworkPost toEntity(ArtworkPostUpdatingDto dto);

    // Map tags list method
    default List<PostTag> toPostTags(Integer[] tagIds) {
        if (tagIds == null) return List.of();
        return Arrays.stream(tagIds)
                .map(id -> {
                    // Create the composite key object
                    PostTagId compositeKey = new PostTagId();
                    compositeKey.setTagId(id);

                    // Create the entity and assign the key
                    PostTag pt = new PostTag();
                    pt.setId(compositeKey);
                    return pt;
                })
                .toList();
    }

    // Map category
    default Category toCategory(Integer categoryId){
        if (categoryId == null)
            return null;

        // Create new category object and return it
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
