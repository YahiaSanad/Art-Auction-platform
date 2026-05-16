import { filterArtworks, normalizeArtworkFilters } from '../utils/filterParser'
import * as artworkPostapis from "../api/ArtworkPostApis"
import * as postBidApis from "../api/PostBidApis"
import { getAuctionState } from '../utils/time'

function toDataUrl(rawImage) {
  if (!rawImage) return ''
  if (String(rawImage).startsWith('data:')) return rawImage
  return `data:image/jpeg;base64,${rawImage}`
}

function toFiniteNumber(value, fallback = 0) {
  const parsed = Number(value)
  return Number.isFinite(parsed) ? parsed : fallback
}

function normalizeArtwork(artwork) {
  const normalizedImage = toDataUrl(artwork.image || artwork.imageUrl)
  const initialPrice = toFiniteNumber(artwork.initialPrice, 0)
  const buyNowPrice = toFiniteNumber(
    artwork.buyNowPrice ?? artwork.buyNewPrice ?? initialPrice,
    initialPrice,
  )

  return {
    ...artwork,
    initialPrice,
    buyNowPrice,
    // Keep backward compatibility with legacy frontend fields.
    buyNewPrice: buyNowPrice,
    image: normalizedImage,
    imageUrl: normalizedImage,
    tags: Array.isArray(artwork.tags) ? artwork.tags : [],
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
      let postBids = []
      try {
        // Some backend versions don't expose /PostBid routes yet.
        // Keep Home page usable by falling back to an empty bid feed.
        postBids = await postBidApis.getAllPostBids(artwork.id);
      } catch (error) {
        postBids = []
      }

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
