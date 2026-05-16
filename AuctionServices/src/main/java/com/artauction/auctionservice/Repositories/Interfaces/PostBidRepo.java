package com.artauction.auctionservice.Repositories.Interfaces;

import com.artauction.auctionservice.Config.IDs.PostBidId;
import com.artauction.auctionservice.Entities.PostBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostBidRepo extends JpaRepository<PostBid, PostBidId> {
    @Query("""
        SELECT pb FROM PostBid pb
        WHERE pb.id.artworkPostId = :artworkPostId
    """)
    List<PostBid> findAllByArtworkPostId(@Param("artworkPostId") int artworkPostId);

    @Query("""
        SELECT pb FROM PostBid pb
        WHERE pb.id.artworkPostId = :artworkPostId
        ORDER BY pb.buyerPrice DESC
        LIMIT 1
    """)
    Optional<PostBid> findTopBidByArtworkPostId(@Param("artworkPostId") int artworkPostId);

    @Query("""
        SELECT pb FROM PostBid pb
        WHERE pb.id.buyerId = :buyerId
    """)
    Collection<PostBid> findAllByBuyerId(@Param("buyerId") int buyerId);
}
