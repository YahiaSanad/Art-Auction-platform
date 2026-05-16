package com.artauction.artworkpostservices.Controllers;

import com.artauction.artworkpostservices.Services.Interfaces.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Category")
@RequiredArgsConstructor
public class CategoryController {
    // Attributes
    private final CategoryServices categoryServices;

    // Get all categories API
    @GetMapping("/GetAllCategories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryServices.getAllCategories());
    }
}
