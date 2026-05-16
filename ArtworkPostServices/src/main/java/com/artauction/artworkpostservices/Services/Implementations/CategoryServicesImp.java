package com.artauction.artworkpostservices.Services.Implementations;

import com.artauction.artworkpostservices.Dtos.Category.CategoryDto;
import com.artauction.artworkpostservices.Entities.Category;
import com.artauction.artworkpostservices.Mappers.Category.CategoryMapper;
import com.artauction.artworkpostservices.Repositories.Interfaces.CategoryRepo;
import com.artauction.artworkpostservices.Services.Interfaces.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServicesImp implements CategoryServices {
    // Attributes
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    // Methods
    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categoryMapper.toDtoList(categories);
    }
}
