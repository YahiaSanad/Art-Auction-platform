package com.artauction.auctionservice.Services.Implementations;

import com.artauction.auctionservice.Dtos.Buyer.BuyerDto;
import com.artauction.auctionservice.Dtos.Email.EmailDto;
import com.artauction.auctionservice.Email.EmailMessages;
import com.artauction.auctionservice.Entities.PostSold;
import com.artauction.auctionservice.Kafka.Request.KafkaRequestReplyService;
import com.artauction.auctionservice.Repositories.Customs.PostBidCustomRepo;
import com.artauction.auctionservice.Repositories.Customs.PostSoldCustomRepo;
import com.artauction.auctionservice.Repositories.Interfaces.PostSoldRepo;
import com.artauction.auctionservice.Services.Interfaces.AuctionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionServicesImp implements AuctionServices {
    // Attributes
    private final PostSoldRepo postSoldRepo;
    private final PostBidCustomRepo postBidCustomRepo;
    private final PostSoldCustomRepo postSoldCustomRepo;
    private final EmailMessages emailMessages;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    @Override
    public void determineWinner(Integer artworkPostId) {
        // Check if winner already determined — avoid duplicate PostSold records
        if (postSoldRepo.existsByIdArtworkPostId(artworkPostId))
            return;

        // Get winner mapped
        Optional<PostSold> postSold = postBidCustomRepo.getWinner(artworkPostId);

        // Build and save PostSold record — equivalent to CreatePostSold() in .NET
        if (postSold.isEmpty())
            return;
        if (!postSoldCustomRepo.createPostSold(postSold.get())){
            return;
        }

        // Get artwork title, buyer name and email
        String artworkTitle = null; // Note: replace with call to ArtworkPostServices
        String buyerName  = null; // Note: replace with call to AuthenticationServices
        String buyerEmail = null; // Note: replace with call to AuthenticationServices
        try {
            // Get title
            artworkTitle = kafkaRequestReplyService.fetchEndedPostData(postSold.get().getId().getArtworkPostId()).getTitle();

            // Get buyer name and email
            BuyerDto buyerDto = kafkaRequestReplyService.fetchBuyerData(postSold.get().getId().getBuyerId());
            buyerName = buyerDto.getName();
            buyerEmail = buyerDto.getEmail();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }

        // Send email using email service
        String subject = "Auction Ended 🎉";
        String emailBody = emailMessages.winnerHtmlMessage(buyerName, artworkTitle, postSold.get());
        EmailDto emailDto = new EmailDto();
        emailDto.setEmail(buyerEmail);
        emailDto.setSubject(subject);
        emailDto.setBody(emailBody);
        try {
            kafkaRequestReplyService.sendEmail(emailDto);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
