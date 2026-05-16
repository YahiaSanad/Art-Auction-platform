import { db, nextId, wait } from '../data/mocks/inMemoryDb'
import { readUserField } from '../utils/secureData'
import * as artworkPostApis from "../api/ArtworkPostApis"
import * as tagApis from '../api/TagApis'
import { ARTWORK_CATEGORIES } from '../constants/categories'


export async function listArtistArtworks(artistId) {
  return (await artworkPostApis.getAllArtistPosts(artistId));
}

export async function getArtistArtworkById(artworkId, artistId) {
  const posts = await artworkPostApis.getAllArtistPosts(artistId)
  const post = posts.find((item) => Number(item.id) === Number(artworkId))
  if (!post) return null

  return {
    id: post.id,
    title: post.title,
    description: post.description,
    initialPrice: Number(post.initialPrice ?? 0),
    buyNowPrice: Number(post.buyNowPrice ?? post.buyNewPrice ?? post.initialPrice ?? 0),
    auctionStartTime: post.startDate,
    auctionEndTime: post.endDate,
    category: post.categoryName,
    tags: post.tags || [],
    imageUrl: toDataUrl(post.image),
  }
}


export async function createArtwork(artistId, payload) {
  const selectedCategoryName =
    typeof payload.category === 'string' ? payload.category : payload.category?.name

  const category = ARTWORK_CATEGORIES.find(
    (c) => c.name.toLowerCase() === String(selectedCategoryName).toLowerCase(),
  )
  if (!category) throw new Error(`Category "${selectedCategoryName}" not found.`)

  const allTags = await tagApis.getAllTags()
  const tagIds = (payload.tags || [])
    .map((tagName) => {
      const tag = allTags.find(
        (t) => t.name.toLowerCase() === String(tagName).toLowerCase(),
      )
      return tag?.id
    })
    .filter(Boolean)

  if (tagIds.length === 0) {
    throw new Error('Please enter existing tags from the system.')
  }

  const imageFile = payload.imageFile || dataUrlToFile(payload.imageUrl)
  if (!imageFile) {
    throw new Error('Artwork image is required for create.')
  }

  const body = {
    title: payload.title,
    description: payload.description,
    initialPrice: Number(payload.initialPrice),
    buyNowPrice: Number(payload.buyNowPrice ?? payload.buyNewPrice ?? payload.initialPrice),
    startDate: payload.auctionStartTime,
    endDate: payload.auctionEndTime,
    image: imageFile,
    categoryId: Number(category.id),
    artistId: Number(artistId),
    tagIds,
  }

  return artworkPostApis.createArtworkPost(body)
}


export async function updateArtwork(artworkId, artistId, payload) {
  const category = ARTWORK_CATEGORIES.find(
    (c) => c.name.toLowerCase() === String(payload.category).toLowerCase(),
  )
  if (!category) throw new Error(`Category "${payload.category}" not found.`)

  const allTags = await tagApis.getAllTags()
  const tagIds = (payload.tags || [])
    .map((tagName) => {
      const tag = allTags.find(
        (t) => t.name.toLowerCase() === String(tagName).toLowerCase(),
      )
      return tag?.id
    })
    .filter(Boolean)

  if (tagIds.length === 0) {
    throw new Error('Please enter existing tags from the system.')
  }

  const imageFile = payload.imageFile || dataUrlToFile(payload.imageUrl)
  if (!imageFile) {
    throw new Error('Artwork image is required for update.')
  }

  const body = {
    id: Number(artworkId),
    title: payload.title,
    description: payload.description,
    initialPrice: Number(payload.initialPrice),
    buyNowPrice: Number(payload.buyNowPrice ?? payload.buyNewPrice ?? payload.initialPrice),
    startDate: payload.auctionStartTime,
    endDate: payload.auctionEndTime,
    image: imageFile,
    categoryId: Number(category.id),
    artistId: Number(artistId),
    tagIds,
  }

  return artworkPostApis.updateArtworkPost(body)
}


export async function deleteArtwork(artworkId) {
  const status = await artworkPostApis.deleteArtworkPost(artworkId)
  if (status === false) {
    throw new Error('Artwork not found.')
  }
  return status
}


export async function extendAuction(artworkId, _artistId, newEndTime) {
  const endDate = newEndTime instanceof Date ? newEndTime : new Date(newEndTime)

  if (Number.isNaN(endDate.getTime())) {
    throw new Error('Invalid new end time.')
  }

  const status = await artworkPostApis.changeEndDate(Number(artworkId), endDate)
  if (status === false) {
    throw new Error('Artwork not found.')
  }

  return status
}


function toDataUrl(rawBase64) {
  if (!rawBase64) return ''
  if (rawBase64.startsWith('data:')) return rawBase64
  return `data:image/jpeg;base64,${rawBase64}`
}

function dataUrlToFile(dataUrl, fileName = 'artwork.jpg') {
  if (!dataUrl) return null
  const [meta, content] = dataUrl.split(',')
  if (!meta || !content) return null

  const mimeMatch = meta.match(/data:(.*?);base64/)
  const mimeType = mimeMatch ? mimeMatch[1] : 'image/jpeg'
  const bytes = atob(content)
  const arr = new Uint8Array(bytes.length)
  for (let i = 0; i < bytes.length; i += 1) arr[i] = bytes.charCodeAt(i)

  return new File([arr], fileName, { type: mimeType })
}
