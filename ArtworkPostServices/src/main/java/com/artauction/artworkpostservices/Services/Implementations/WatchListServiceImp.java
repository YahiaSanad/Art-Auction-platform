package com.artauction.artworkpostservices.Services.Implementations;

import com.artauction.artworkpostservices.Config.IDs.WatchListId;
import com.artauction.artworkpostservices.Dtos.Artist.ArtistDto;
import com.artauction.artworkpostservices.Dtos.ArtworkPost.ArtworkPostDto;
import com.artauction.artworkpostservices.Dtos.WatchList.WatchListCreationDto;
import com.artauction.artworkpostservices.Entities.ArtworkPost;
import com.artauction.artworkpostservices.Entities.WatchList;
import com.artauction.artworkpostservices.Kafka.Request.KafkaRequestReplyService;
import com.artauction.artworkpostservices.Mappers.WatchList.WatchListCreationMapper;
import com.artauction.artworkpostservices.Mappers.WatchList.WatchListMapper;
import com.artauction.artworkpostservices.Repositories.Customs.WatchListCustomRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.ArtworkPostRepo;
import com.artauction.artworkpostservices.Repositories.Interfaces.WatchListRepo;
import com.artauction.artworkpostservices.Services.Interfaces.WatchListServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchListServiceImp implements WatchListServices {
    // Attributes
    private final WatchListRepo watchListRepo;
    private final WatchListCustomRepo watchListCustomRepo;
    private final ArtworkPostRepo artworkPostRepo;
    private final WatchListCreationMapper watchListCreationMapper;
    private final WatchListMapper watchListMapper;
    private final KafkaRequestReplyService kafkaRequestReplyService;

    // Methods
    @Override
    public List<ArtworkPostDto> getWatchListForBuyer(Integer buyerId) {
        List<WatchList> watchList = watchListRepo.findAllByBuyerId(buyerId);

        // Get all artists
        return watchList.stream()
                .map(artworkPost -> {
                    // Get artworkPost
                    ArtworkPostDto artworkPostDto = watchListMapper.toArtworkPostDto(artworkPost);

                    try {
                        // Fetch artist data
                        ArtistDto artistDto = kafkaRequestReplyService.fetchArtistData(
                                artworkPost.getArtworkPost().getArtistId());

                        // Check artist
                        if (artistDto != null){
                            artworkPostDto.setArtistName(artistDto.getName());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e);
                    }

                    return artworkPostDto;
                }).toList();
    }

    @Override
    public boolean createWatchList(WatchListCreationDto watchListCreationDto) {
        // Check buyer existence
        try {
            if (!kafkaRequestReplyService.checkBuyerExists(watchListCreationDto.getBuyerId()))
                return false;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

        // Map DTO to entity
        WatchList watchList = watchListCreationMapper.toEntity(watchListCreationDto);
        ArtworkPost postProxy = artworkPostRepo.getReferenceById(watchListCreationDto.getArtworkPostId());
        watchList.setArtworkPost(postProxy);

        // Add relation and return result
        return watchListCustomRepo.createWatchList(watchList);
    }

    @Override
    public boolean deleteWatchList(Integer buyerId, Integer artworkPostId) {
        // Create watch list id
        WatchListId watchListId = new WatchListId();
        watchListId.setArtworkPostId(artworkPostId);
        watchListId.setBuyerId(buyerId);

        // Delete relation
        return watchListCustomRepo.deleteWatchList(watchListId);
    }
}
