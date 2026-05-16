package com.artauction.auctionservice.Services.Implementations;

import com.artauction.auctionservice.Config.IDs.PostSoldId;
import com.artauction.auctionservice.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.auctionservice.Dtos.Buyer.BuyerDto;
import com.artauction.auctionservice.Dtos.PostSold.BuyerPostSoldDto;
import com.artauction.auctionservice.Dtos.PostSold.PostSoldDto;
import com.artauction.auctionservice.Dtos.PostSold.PostSoldPaidDto;
import com.artauction.auctionservice.Dtos.PostSold.UnpaidPostSoldDto;
import com.artauction.auctionservice.Entities.PostSold;
import com.artauction.auctionservice.Kafka.Request.KafkaRequestReplyService;
import com.artauction.auctionservice.Mappers.PostSold.BuyerPostSoldMapper;
import com.artauction.auctionservice.Mappers.PostSold.PostSoldMapper;
import com.artauction.auctionservice.Mappers.PostSold.UnpaidPostSoldMapper;
import com.artauction.auctionservice.Repositories.Customs.PostSoldCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostSoldRepo;
import com.artauction.auctionservice.Services.Interfaces.PostSoldServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostSoldServicesImp implements PostSoldServices {
    // Attributes
    private final PostSoldRepo postSoldRepo;
    private final PostSoldCustomRepo postSoldCustomRepo;
    private final BuyerPostSoldMapper buyerPostSoldMapper;
    private final UnpaidPostSoldMapper unpaidPostSoldMapper;
    private final PostSoldMapper postSoldMapper;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Override
    public Collection<PostSoldDto> getAllPostSolds() {
        // Get post solds
        Collection<PostSold> postSolds = postSoldRepo.findAll();

        // Map entity to dto
        List<PostSoldDto> postSoldDtos = postSolds.stream()
                .map(postSold -> {
                    // Convert the basic entity to DTO
                    PostSoldDto dto = postSoldMapper.toDto(postSold);

                    // Fetch the external data using the ID from the entity
                    ArtworkPostDto postData = null;
                    BuyerDto buyerDto = null;
                    System.out.println(postSold.getId().getBuyerId());
                    try {
                        postData = kafkaRequestReplyService
                                .fetchPostData(postSold.getId().getArtworkPostId());
                        buyerDto = kafkaRequestReplyService.fetchBuyerData(postSold.getId().getBuyerId());
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }

                    // Set the external data into DTO
                    dto.setArtworkPost(postData);
                    dto.setBuyerName(buyerDto.getName());

                    return dto;
                })
                .toList();

        return postSoldDtos;
    }

    @Override
    public List<UnpaidPostSoldDto> getUnpaidPostSoldForBuyer(int buyerId) {
        // Get unpaid post sold for buyer
        Optional<PostSold> unpaidPost = postSoldRepo.findUnpaidByBuyerId(buyerId);

        // Map entity to dto
        return unpaidPost.stream()
                .map(postSold -> {
                    // Map to DTO
                    UnpaidPostSoldDto unpaidPostSoldDto = unpaidPostSoldMapper.toDto(postSold);

                    // Fetch the external data using the ID from the entity
                    try {
                        ArtworkPostDto postData = kafkaRequestReplyService.
                                fetchEndedPostData(postSold.getId().getArtworkPostId());

                        // Set only the title in the DTO
                        if (postData != null) {
                            unpaidPostSoldDto.setArtworkPostTitle(postData.getTitle());
                            unpaidPostSoldDto.setArtworkPostId(postData.getId());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }

                    return unpaidPostSoldDto;
                })
                .toList();
    }

    @Override
    public Collection<BuyerPostSoldDto> getBuyerPostSolds(int buyerId) {
        // Get post solds for buyer
        Collection<PostSold> postSolds = postSoldRepo.findAllByBuyerId(buyerId);

        // Map and get artwork data from another service
        return postSolds.stream()
            .map(postSold -> {
                // Map the basic entity fields to DTO
                BuyerPostSoldDto dto = buyerPostSoldMapper.toDto(postSold);

                try {
                    // Fetch post data via Kafka
                    ArtworkPostDto postData = kafkaRequestReplyService.
                            fetchPostData(postSold.getId().getArtworkPostId());

                    // Set title
                    dto.setTitle(postData.getTitle());
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }

                return dto;
            })
            .toList();
    }

    @Override
    public boolean markAsPaid(PostSoldPaidDto postSoldPaidDto) {
        // Build composite key
        PostSoldId postSoldId = new PostSoldId();
        postSoldId.setArtworkPostId(postSoldPaidDto.getArtworkPostId());
        postSoldId.setBuyerId(postSoldPaidDto.getBuyerId());

        return postSoldCustomRepo.markAsPaid(postSoldId);
    }
}
