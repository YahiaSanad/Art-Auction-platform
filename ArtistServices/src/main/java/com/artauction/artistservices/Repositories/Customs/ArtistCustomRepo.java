package com.artauction.artistservices.Repositories.Customs;

public interface ArtistCustomRepo {
    boolean approveArtist(int artistId, int adminId);
    boolean rejectArtist(int artistId);
}