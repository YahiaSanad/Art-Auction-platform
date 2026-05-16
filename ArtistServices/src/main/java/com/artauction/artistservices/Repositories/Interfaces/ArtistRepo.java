package com.artauction.artistservices.Repositories.Interfaces;

import com.artauction.artistservices.Entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistRepo extends JpaRepository<Artist, Integer> {
    List<Artist> findByAdminIdIsNotNull();
    List<Artist> findByAdminIdIsNull();
}