package com.artauction.artworkpostservices.Repositories.Interfaces;

import com.artauction.artworkpostservices.Entities.ArtworkPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtworkPostRepo extends JpaRepository<ArtworkPost, Integer> {
    @Query("""
        SELECT DISTINCT ap FROM ArtworkPost ap
        LEFT JOIN FETCH ap.category 
        LEFT JOIN FETCH ap.postTags pt
        LEFT JOIN FETCH pt.tag
        WHERE ap.id = :artworkPostId AND ap.endDate <= :now AND ap.adminId IS NOT NULL
    """)
    Optional<ArtworkPost> findEndedPost(Integer artworkPostId, @Param("now") LocalDateTime now);

    @Query("""
        SELECT ap.id FROM ArtworkPost ap
        WHERE ap.endDate <= :now
        AND ap.adminId IS NOT NULL
        """)
    List<Integer> findEndedPostIds(@Param("now") LocalDateTime now);

    // Check existence
    boolean existsByIdAndAdminIdIsNotNullAndEndDateAfter(Integer id, LocalDateTime now);

    // Equivalent to GetAllArtworkPosts()
    // Note: Artist info → enrich via Artist microservice in service layer
    @Query(
        "SELECT DISTINCT ap FROM ArtworkPost ap " +
        "LEFT JOIN FETCH ap.category " +
        "LEFT JOIN FETCH ap.postTags pt " +
        "LEFT JOIN FETCH pt.tag " +
        "WHERE ap.endDate > :now AND ap.adminId IS NOT NULL"
    )
    List<ArtworkPost> findAllApprovedAndActive(@Param("now") LocalDateTime now);

    // Equivalent to GetAllArtworkPostsForArtist(int artistId)
    @Query(
        "SELECT DISTINCT ap FROM ArtworkPost ap " +
        "LEFT JOIN FETCH ap.category " +
        "LEFT JOIN FETCH ap.postTags pt " +
        "LEFT JOIN FETCH pt.tag " +
        "WHERE ap.artistId = :artistId AND ap.endDate > :now"
    )
    List<ArtworkPost> findAllByArtistIdAndActive(
            @Param("artistId") Integer artistId,
            @Param("now") LocalDateTime now
    );

    // Equivalent to GetArtworkPost(int artworkPostId)
    // Note: Artist entity → cross-service call in service layer
    // Note: PostBids → Buyer entity lives in Buyer microservice → cross-service call in service layer
    @Query(
        "SELECT DISTINCT ap FROM ArtworkPost ap " +
        "LEFT JOIN FETCH ap.category " +
        "LEFT JOIN FETCH ap.postTags pt " +
        "LEFT JOIN FETCH pt.tag " +
        "WHERE ap.id = :id AND ap.adminId IS NOT NULL AND ap.endDate > :now"
    )
    Optional<ArtworkPost> findApprovedAndActiveById(
            @Param("id") Integer id,
            @Param("now") LocalDateTime now
    );

    // Equivalent to GetUnapprovedArtworkPosts()
    // Note: Artist entity → cross-service call in service layer
    @Query(
        "SELECT DISTINCT ap FROM ArtworkPost ap " +
        "LEFT JOIN FETCH ap.category " +
        "LEFT JOIN FETCH ap.postTags pt " +
        "LEFT JOIN FETCH pt.tag " +
        "WHERE ap.adminId IS NULL"
    )
    List<ArtworkPost> findAllUnapproved();

    // Used by UpdateArtworkPost() — fetch with PostTags for update
    @Query(
        "SELECT ap FROM ArtworkPost ap " +
        "LEFT JOIN FETCH ap.postTags " +
        "WHERE ap.id = :id"
    )
    Optional<ArtworkPost> findByIdWithPostTags(@Param("id") Integer id);

    // Note: Artist existence check (used by CreateArtworkPost) →
    //       call Artist microservice via Feign/REST in service layer

    // Note: Admin existence check (used by MarkAsApproved) →
    //       call Admin microservice via Feign/REST in service layer
}
