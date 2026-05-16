package com.artauction.artworkpostservices.Services.Interfaces;

import com.artauction.artworkpostservices.Dtos.Category.CategoryDto;

import java.util.List;

public interface CategoryServices {
    public List<CategoryDto> getAllCategories();
}
