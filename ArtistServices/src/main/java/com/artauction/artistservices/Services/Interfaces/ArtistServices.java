package com.artauction.artistservices.Services.Interfaces;

import com.artauction.artistservices.Dtos.ArtistDto;
import java.util.List;

public interface ArtistServices {
    List<ArtistDto> getUnapprovedArtists();
    List<ArtistDto> getApprovedArtists();
    boolean approveArtist(int artistId, int adminId);
    boolean rejectArtist(int artistId);
}