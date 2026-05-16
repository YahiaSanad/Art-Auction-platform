import * as watchListApis from '../api/WatchListApis'

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

function toInt(value, label) {
  const parsed = Number(value)
  if (!Number.isInteger(parsed) || parsed <= 0) {
    throw new Error(`Invalid ${label}.`)
  }
  return parsed
}

function toErrorMessage(error) {
  if (typeof error === 'string') return error
  return error?.response?.data?.title || error?.message || String(error)
}

export async function listWatchlist(userId) {
  const buyerId = toInt(userId, 'buyer id')
  const watchlist = await watchListApis.getWatchListForBuyer(buyerId)
  return Array.isArray(watchlist) ? watchlist.map(normalizeArtwork) : []
}

export async function addToWatchlist(userId, artworkId) {
  const buyerId = toInt(userId, 'buyer id')
  const artworkPostId = toInt(artworkId, 'artwork id')
  return watchListApis.createWatchList({
    BuyerId: buyerId,
    ArtworkPostId: artworkPostId,
  })
}

export async function removeFromWatchlist(userId, artworkId) {
  const buyerId = toInt(userId, 'buyer id')
  const artworkPostId = toInt(artworkId, 'artwork id')
  return watchListApis.deleteWatchList(buyerId, artworkPostId)
}

export async function toggleWatchlist(userId, artworkId, isInWatchlist) {
  const buyerId = toInt(userId, 'buyer id')
  const artworkPostId = toInt(artworkId, 'artwork id')

  let saved = Boolean(isInWatchlist)
  if (typeof isInWatchlist !== 'boolean') {
    const currentWatchlist = await listWatchlist(buyerId)
    saved = currentWatchlist.some((item) => Number(item.id) === artworkPostId)
  }

  if (saved) {
    try {
      await removeFromWatchlist(buyerId, artworkPostId)
      return { saved: false }
    } catch (error) {
      const message = toErrorMessage(error).toLowerCase()
      if (message.includes('not found')) {
        return { saved: false }
      }
      throw error
    }
  }

  try {
    await addToWatchlist(buyerId, artworkPostId)
    return { saved: true }
  } catch (error) {
    const message = toErrorMessage(error).toLowerCase()
    if (message.includes('duplicate')) {
      return { saved: true }
    }
    throw error
  }
}
