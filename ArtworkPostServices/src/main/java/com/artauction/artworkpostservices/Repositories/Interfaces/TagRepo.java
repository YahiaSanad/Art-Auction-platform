package com.artauction.artworkpostservices.Repositories.Interfaces;

import com.artauction.artworkpostservices.Entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends JpaRepository<Tag, Integer> {
}
