package com.artauction.artworkpostservices.Controllers;

import com.artauction.artworkpostservices.Dtos.Tag.TagDto;
import com.artauction.artworkpostservices.Services.Interfaces.TagServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Tag")
@RequiredArgsConstructor
public class TagController {
    // Attributes
    private final TagServices tagServices;

    // Get all tags API
    @GetMapping("/GetAllTags")
    public ResponseEntity<?> getAllTags() {
        return ResponseEntity.ok(tagServices.getAllTags());
    }
}
