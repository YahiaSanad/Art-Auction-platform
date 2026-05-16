package com.artauction.artworkpostservices.Repositories.Interfaces;

import com.artauction.artworkpostservices.Config.IDs.WatchListId;
import com.artauction.artworkpostservices.Entities.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepo extends JpaRepository<WatchList, WatchListId> {
    // Equivalent to GetWatchListForBuyer(int buyerId)
    // Note: ArtworkPost → Artist info
    @Query(
        "SELECT DISTINCT wl FROM WatchList wl " +
        "LEFT JOIN FETCH wl.artworkPost ap " +
        "LEFT JOIN FETCH ap.category " +
        "LEFT JOIN FETCH ap.postTags pt " +
        "LEFT JOIN FETCH pt.tag " +
        "WHERE wl.id.buyerId = :buyerId"
    )
    List<WatchList> findAllByBuyerId(@Param("buyerId") Integer buyerId);
}
