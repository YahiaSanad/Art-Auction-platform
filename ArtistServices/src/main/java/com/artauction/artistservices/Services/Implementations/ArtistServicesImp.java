package com.artauction.artistservices.Services.Implementations;

import com.artauction.artistservices.Dtos.ArtistDto;
import com.artauction.artistservices.Mappers.ArtistMapper;
import com.artauction.artistservices.Repositories.Interfaces.ArtistRepo;
import com.artauction.artistservices.Services.Interfaces.ArtistServices;
import com.artauction.artistservices.Services.Strategy.ApproveArtistStrategy;
import com.artauction.artistservices.Services.Strategy.RejectArtistStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistServicesImp implements ArtistServices {
    // Attributes
    private final ArtistRepo artistRepo;
    private final ArtistMapper artistMapper;
    private final ApproveArtistStrategy approveArtistStrategy;
    private final RejectArtistStrategy rejectArtistStrategy;

    // Methods
    @Override
    public List<ArtistDto> getUnapprovedArtists() {
        // Return unapproved artists
        return artistRepo.findByAdminIdIsNull()
                .stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtistDto> getApprovedArtists() {
        // Return approved artists
        return artistRepo.findByAdminIdIsNotNull()
                .stream()
                .map(artistMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean approveArtist(int artistId, int adminId) {
        return approveArtistStrategy.execute(artistId,adminId);
    }

    @Override
    public boolean rejectArtist(int artistId) {
        return rejectArtistStrategy.execute(artistId, null);
    }
}