package com.artauction.auctionservice.Services.Implementations;

import com.artauction.auctionservice.Config.IDs.PostBidId;
import com.artauction.auctionservice.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.auctionservice.Dtos.Buyer.BuyerDto;
import com.artauction.auctionservice.Dtos.PostBid.BuyerPostBidDto;
import com.artauction.auctionservice.Dtos.PostBid.PostBidCreationDto;
import com.artauction.auctionservice.Dtos.PostBid.PostBidDto;
import com.artauction.auctionservice.Entities.PostBid;
import com.artauction.auctionservice.Kafka.Request.KafkaRequestReplyService;
import com.artauction.auctionservice.Mappers.PostBid.BuyerPostBidMapper;
import com.artauction.auctionservice.Mappers.PostBid.PostBidCreationMapper;
import com.artauction.auctionservice.Mappers.PostBid.PostBidMapper;
import com.artauction.auctionservice.Repositories.Customs.PostBidCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostBidRepo;
import com.artauction.auctionservice.Services.Interfaces.PostBidServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostBidServicesImp implements PostBidServices {
    // Attributes
    private final PostBidRepo postBidRepo;
    private final PostBidCustomRepo postBidCustomRepo;
    private final BuyerPostBidMapper buyerPostBidMapper;
    private final PostBidCreationMapper postBidCreationMapper;
    private final PostBidMapper postBidMapper;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Override
    public List<PostBidDto> getAllPostBids(int artworkPostId) {
        // Get post bids
        List<PostBid> postBids = postBidRepo.findAllByArtworkPostId(artworkPostId);

        // Map and enrich with Buyer Name
        return postBids.stream()
                .map(postBid -> {
                    // Map basic entity fields to DTO
                    PostBidDto dto = postBidMapper.toDto(postBid);

                    try {
                        // Fetch Buyer Data via Kafka
                        BuyerDto buyerData = kafkaRequestReplyService.fetchBuyerData(postBid.getId().getBuyerId());

                        // Set the buyer name
                        if (buyerData != null) {
                            dto.setBuyerName(buyerData.getName());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }

                    return dto;
                })
                .toList();
    }

    @Override
    public Collection<BuyerPostBidDto> getAllBuyerBids(int buyerId) {
        // Get buyer bids
        Collection<PostBid> postBids = postBidRepo.findAllByBuyerId(buyerId);

        return postBids.stream()
                .map(postBid -> {
                    // Map entity to DTO
                    BuyerPostBidDto dto = buyerPostBidMapper.toDto(postBid);

                    try {
                        // Fetch Artwork data via Kafka
                        ArtworkPostDto postData = kafkaRequestReplyService.fetchPostData(postBid.getId().getArtworkPostId());

                        // Set the title
                        if (postData != null) {
                            dto.setTitle(postData.getTitle());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }

                    return dto;
                })
                .toList();
    }

    @Override
    public boolean createPostBid(PostBidCreationDto postBidCreationDto) {
        try {
            // Validate Artwork Post existence and approval via Kafka
            Boolean isPostValid = kafkaRequestReplyService
                    .checkPostExists(postBidCreationDto.getArtworkPostId());
            if (Boolean.FALSE.equals(isPostValid)) {
                return false;
            }

            // Validate Buyer existence via Kafka
            Boolean isBuyerValid = kafkaRequestReplyService
                    .checkBuyerExists(postBidCreationDto.getBuyerId());
            if (Boolean.FALSE.equals(isBuyerValid)) {
                return false;
            }

            // Update buyNowPrice
            boolean updated = kafkaRequestReplyService.updateBuyNowPrice(
                    postBidCreationDto.getArtworkPostId(),
                    postBidCreationDto.getBuyerPrice().add(BigDecimal.TEN)
            );
            if (!updated)
                return false;

        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

        // Map to entity
        PostBid postBid = postBidCreationMapper.toEntity(postBidCreationDto);

        // Save to DB
        return postBidCustomRepo.createPostBid(postBid);
    }

    @Override
    public boolean deletePostBid(int artworkPostId, int buyerId) {
        // Build composite key
        PostBidId postBidId = new PostBidId();
        postBidId.setArtworkPostId(artworkPostId);
        postBidId.setBuyerId(buyerId);

        return postBidCustomRepo.deletePostBid(postBidId);
    }
}
