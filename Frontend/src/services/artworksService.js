import { filterArtworks, normalizeArtworkFilters } from '../utils/filterParser'
import * as artworkPostapis from "../api/ArtworkPostApis"
import * as postBidApis from "../api/PostBidApis"
import { getAuctionState } from '../utils/time'

function toDataUrl(rawImage) {
  if (!rawImage) return ''
  if (String(rawImage).startsWith('data:')) return rawImage
  return `data:image/jpeg;base64,${rawImage}`
}

function normalizeArtwork(artwork) {
  const normalizedImage = toDataUrl(artwork.image || artwork.imageUrl)

  return {
    ...artwork,
    image: normalizedImage,
    imageUrl: normalizedImage,
    auctionStartTime: artwork.auctionStartTime || artwork.startDate,
    auctionEndTime: artwork.auctionEndTime || artwork.endDate,
  }
}

export async function listPublicArtworks(filters = {}) {
  // 1. Fetch all posts from the API
  const allArtworks = await artworkPostapis.getAllArtworkPosts();
  const normalized = allArtworks.map(normalizeArtwork)

  // 2. Filter for approved artworks (where adminId is not null)
  const approved = normalized.filter((artwork) => artwork.adminId !== null);

  // 3. Apply the rest of your utility filters and return
  return filterArtworks(approved, normalizeArtworkFilters(filters));
}

export async function listOngoingBidFeed() {
  const artworkPosts = await artworkPostapis.getAllArtworkPosts();
  const normalized = artworkPosts.map(normalizeArtwork)
  const ongoingPosts = normalized.filter(
    (artwork) => getAuctionState(artwork.startDate, artwork.endDate) === 'live',
  )

  const artworkPostsWithBids = ongoingPosts
    .map(async (artwork) => {
      const postBids = await postBidApis.getAllPostBids(artwork.id);

      return {
        ...artwork,
        postBids,
      }
    });

  return Promise.all(artworkPostsWithBids);
}

export async function getArtworkById(artworkId) {

  const artworkPost = await artworkPostapis.getPostWithDetails(artworkId);
  if (!artworkPost) return null
  return normalizeArtwork(artworkPost)
}

export async function getPublicArtworkById(artworkId) {
  const artworkPost = await artworkPostapis.getPostWithDetails(artworkId);

  if (!artworkPost) return null
  return normalizeArtwork(artworkPost)
}
