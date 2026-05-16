package com.artauction.artworkpostservices.Repositories.Customs;

import com.artauction.artworkpostservices.Config.IDs.WatchListId;
import com.artauction.artworkpostservices.Entities.WatchList;

public interface WatchListCustomRepo {
    public boolean createWatchList(WatchList watchList);
    public boolean deleteWatchList(WatchListId watchListId);
}
