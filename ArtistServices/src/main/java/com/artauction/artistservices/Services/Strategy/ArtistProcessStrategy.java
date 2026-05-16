package com.artauction.artistservices.Services.Strategy;

public interface ArtistProcessStrategy {
    boolean execute(int artistId, Integer adminId);
}