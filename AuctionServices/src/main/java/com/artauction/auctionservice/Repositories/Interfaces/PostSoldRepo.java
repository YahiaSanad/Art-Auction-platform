package com.artauction.auctionservice.Repositories.Interfaces;

import com.artauction.auctionservice.Config.IDs.PostSoldId;
import com.artauction.auctionservice.Entities.PostSold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PostSoldRepo extends JpaRepository<PostSold, PostSoldId> {
    boolean existsByIdArtworkPostId(@Param("artworkPostId") Integer artworkPostId);

    @Query("""
        SELECT ps FROM PostSold ps
        WHERE ps.id.buyerId = :buyerId AND ps.isPaid = false
    """)
    Optional<PostSold> findUnpaidByBuyerId(@Param("buyerId") Integer buyerId);

    @Query("""
        SELECT ps FROM PostSold ps
        WHERE ps.id.buyerId = :buyerId AND ps.isPaid != false
    """)
    Collection<PostSold> findAllByBuyerId(@Param("buyerId") Integer buyerId);
}
