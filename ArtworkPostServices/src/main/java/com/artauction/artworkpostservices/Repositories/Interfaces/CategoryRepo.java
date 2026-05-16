package com.artauction.artworkpostservices.Repositories.Interfaces;

import com.artauction.artworkpostservices.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
}
