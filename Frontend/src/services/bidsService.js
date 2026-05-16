import * as postBidApis from '../api/PostBidApis'

export async function placeBid({ artworkId, bidder, amount }) {
  if (!bidder || String(bidder.role || '').toLowerCase() !== 'buyer') {
    throw new Error('Only logged in buyers can place bids.')
  }

  const buyerId = toInt(bidder.id, 'buyer id')
  const artworkPostId = toInt(artworkId, 'artwork id')
  const buyerPrice = Number(amount)

  if (!Number.isFinite(buyerPrice) || buyerPrice <= 0) {
    throw new Error('Invalid bid amount.')
  }

  try {
    await postBidApis.createPostBid({
      buyerId,
      artworkPostId,
      buyerPrice,
    })
  } catch (error) {
    throw new Error(toErrorMessage(error))
  }

  return {
    artworkId: artworkPostId,
    bidderId: buyerId,
    bidderName: bidder.name || 'You',
    price: buyerPrice,
    timestamp: new Date().toISOString(),
  }
}

export async function listBuyerBids(buyerId) {
  const parsedBuyerId = toInt(buyerId, 'buyer id')
  const bids = await postBidApis.getBuyerPostBids(parsedBuyerId)

  if (!Array.isArray(bids)) return []

  return bids.map((bid, index) => ({
    id: `${bid.artworkPostId}-${index}`,
    artworkId: Number(bid.artworkPostId),
    artworkTitle: bid.title || `Artwork #${bid.artworkPostId}`,
    price: Number(bid.buyerPrice ?? bid.BuyerPrice ?? 0),
    bidTime: bid.bidTime ?? bid.createdAt ?? null,
  }))
}

export async function listArtworkBids(artworkId) {
  const artworkPostId = toInt(artworkId, 'artwork id')
  const bids = await postBidApis.getAllPostBids(artworkPostId)

  if (!Array.isArray(bids)) return []

  return bids.map((bid, index) => ({
    id: `${artworkPostId}-${index}`,
    buyerName: bid.buyerName || bid.BuyerName || 'Unknown Buyer',
    buyerPrice: Number(bid.buyerPrice ?? bid.BuyerPrice ?? 0),
    bidTime: bid.bidTime ?? null,
  }))
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
  if (Array.isArray(error)) return error.join(', ')
  if (error?.errors) {
    const validationMessages = Object.values(error.errors).flat().filter(Boolean)
    if (validationMessages.length) return validationMessages.join(', ')
  }
  return error?.title || error?.message || 'Failed to place bid.'
}
