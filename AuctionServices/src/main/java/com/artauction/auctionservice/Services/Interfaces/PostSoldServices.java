package com.artauction.auctionservice.Services.Interfaces;

import com.artauction.auctionservice.Dtos.PostSold.BuyerPostSoldDto;
import com.artauction.auctionservice.Dtos.PostSold.PostSoldDto;
import com.artauction.auctionservice.Dtos.PostSold.PostSoldPaidDto;
import com.artauction.auctionservice.Dtos.PostSold.UnpaidPostSoldDto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PostSoldServices {
    Collection<PostSoldDto> getAllPostSolds() throws Exception;
    List<UnpaidPostSoldDto> getUnpaidPostSoldForBuyer(int buyerId);
    Collection<BuyerPostSoldDto> getBuyerPostSolds(int buyerId);
    boolean markAsPaid(PostSoldPaidDto postSoldPaidDto);
}
