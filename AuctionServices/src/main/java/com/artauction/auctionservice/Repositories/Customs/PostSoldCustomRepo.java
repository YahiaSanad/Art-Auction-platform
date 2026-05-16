package com.artauction.auctionservice.Repositories.Customs;

import com.artauction.auctionservice.Config.IDs.PostSoldId;
import com.artauction.auctionservice.Entities.PostSold;

public interface PostSoldCustomRepo {
    boolean createPostSold(PostSold postSold);
    boolean markAsPaid(PostSoldId id);
}
