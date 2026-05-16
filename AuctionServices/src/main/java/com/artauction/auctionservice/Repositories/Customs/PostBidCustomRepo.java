package com.artauction.auctionservice.Repositories.Customs;

import com.artauction.auctionservice.Config.IDs.PostBidId;
import com.artauction.auctionservice.Entities.PostBid;
import com.artauction.auctionservice.Entities.PostSold;

import java.util.Optional;

public interface PostBidCustomRepo {
    Optional<PostSold> getWinner(int artworkPostId);
    boolean createPostBid(PostBid postBid);
    boolean deletePostBid(PostBidId id);
}
