import * as postSoldApis from '../api/PostSoldApis'
import * as artworkPostApis from '../api/ArtworkPostApis'

const locallyReadByBuyer = new Map()

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
    const messages = Object.values(error.errors).flat().filter(Boolean)
    if (messages.length) return messages.join(', ')
  }
  return error?.title || error?.message || 'Request failed.'
}

function getReadSet(buyerId) {
  if (!locallyReadByBuyer.has(buyerId)) {
    locallyReadByBuyer.set(buyerId, new Set())
  }
  return locallyReadByBuyer.get(buyerId)
}

function buildPendingKey(buyerId, postSold, index) {
  const safeTitle = String(postSold?.title || 'artwork').trim().toLowerCase()
  const safePrice = Number(postSold?.finalPrice ?? 0)
  return `pending-${buyerId}-${safeTitle}-${safePrice}-${index}`
}

function isPaidFlag(postSold) {
  // Backend may return either `paid` or `isPaid` depending on DTO getter naming.
  if (typeof postSold?.paid === 'boolean') return postSold.paid
  if (typeof postSold?.isPaid === 'boolean') return postSold.isPaid
  return false
}

function getArtworkPostId(postSold) {
  return Number(postSold?.artworkPostId ?? postSold?.ArtworkPostId)
}

function toDataUrl(rawImage) {
  if (!rawImage) return ''
  if (String(rawImage).startsWith('data:')) return rawImage
  return `data:image/jpeg;base64,${rawImage}`
}

export async function listNotifications(userId) {
  const buyerId = toInt(userId, 'buyer id')
  let postSolds = []

  try {
    postSolds = await postSoldApis.getUnpaidPostForBuyer(buyerId)
  } catch (error) {
    throw new Error(toErrorMessage(error))
  }
  console.log(postSolds);

  const rows = Array.isArray(postSolds) ? postSolds : []
  const readSet = getReadSet(buyerId)
  const sortedRows = [...rows].sort((a, b) => {
    const titleCompare = String(a?.artworkPostTitle || '').localeCompare(String(b?.artworkPostTitle || ''))
    if (titleCompare !== 0) return titleCompare
    return Number(a?.finalPrice ?? 0) - Number(b?.finalPrice ?? 0)
  })

  return sortedRows
    .map((postSold, index) => {
      const id = buildPendingKey(buyerId, postSold, index)
      const amount = Number(postSold?.finalPrice ?? 0)
      const title = postSold?.artworkPostTitle || 'Artwork'
      const artworkPostId = getArtworkPostId(postSold)

      return {
        id,
        title: 'Payment required',
        message: `Please pay for "${title}".`,
        amount,
        type: 'payment_required',
        paymentStatus: 'pending',
        isRead: readSet.has(id),
        timestamp: null,
        artworkPostId: Number.isInteger(artworkPostId) ? artworkPostId : null,
        canPay: Number.isInteger(artworkPostId),
      }
    })
}

export async function markNotificationAsRead(notificationId, userId) {
  const buyerId = toInt(userId, 'buyer id')
  if (!notificationId) throw new Error('Notification id is required.')

  const readSet = getReadSet(buyerId)
  readSet.add(notificationId)

  return { id: notificationId, isRead: true }
}

export async function listPurchasedArtworks(userId) {
  const buyerId = toInt(userId, 'buyer id')
  let postSolds = []

  try {
    postSolds = await postSoldApis.getPostSoldForBuyer(buyerId)
  } catch (error) {
    throw new Error(toErrorMessage(error))
  }

  const rows = Array.isArray(postSolds) ? postSolds : []
  const sortedRows = [...rows].sort((a, b) => {
    const titleCompare = String(a?.title || '').localeCompare(String(b?.title || ''))
    if (titleCompare !== 0) return titleCompare
    return Number(a?.finalPrice ?? 0) - Number(b?.finalPrice ?? 0)
  })

  const paidRows = sortedRows.filter((postSold) => isPaidFlag(postSold) === true)

  return Promise.all(
    paidRows.map(async (postSold, index) => {
      const artworkPostId = Number(postSold?.artworkPostId ?? postSold?.ArtworkPostId)
      let artworkDetails = null

      if (Number.isInteger(artworkPostId) && artworkPostId > 0) {
        try {
          artworkDetails = await artworkPostApis.getPostWithDetails(artworkPostId)
        } catch {
          artworkDetails = null
        }
      }

      return {
        id: `purchase-${buyerId}-${artworkPostId || index}`,
        artworkTitle: artworkDetails?.title || postSold?.title || 'Artwork',
        artistName: artworkDetails?.artistName || 'Unknown artist',
        imageUrl: toDataUrl(artworkDetails?.image || artworkDetails?.imageUrl),
        amount: Number(postSold?.finalPrice ?? 0),
        purchasedAt: artworkDetails?.endDate || null,
      }
    }),
  )
}

export async function getPaymentNotification(notificationId, userId) {
  const notifications = await listNotifications(userId)
  const notification = notifications.find((item) => item.id === notificationId)

  if (!notification) {
    throw new Error('Payment request not found.')
  }

  if (!notification.canPay) {
    throw new Error(
      'Backend does not return ArtworkPostId for this item yet, so payment cannot be completed.',
    )
  }

  return notification
}

export async function completePayment({
  notificationId,
  userId,
}) {
  const buyerId = toInt(userId, 'buyer id')
  const notification = await getPaymentNotification(notificationId, buyerId)

  const artworkPostId = Number(notification.artworkPostId)
  if (!Number.isInteger(artworkPostId) || artworkPostId <= 0) {
    throw new Error('Missing artwork id for payment.')
  }

  try {
    await postSoldApis.markAsPaid({
      buyerId,
      artworkPostId,
    })
  } catch (error) {
    throw new Error(toErrorMessage(error))
  }

  return {
    id: `${buyerId}-${artworkPostId}`,
    buyerId,
    artworkPostId,
    amount: notification.amount,
  }
}
