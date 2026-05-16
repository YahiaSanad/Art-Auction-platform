import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { ArtworkCard } from '../../components/artworks/ArtworkCard'
import { FilterBar } from '../../components/artworks/FilterBar'
import { usePublicArtworks } from '../../hooks/useArtworks'
import { PageHeader } from '../../components/ui/PageHeader'
import { useAuthStore } from '../../store/authStore'
import { ROLES } from '../../constants/roles'
import { listWatchlist, toggleWatchlist } from '../../services/watchlistService'

export function ArtworksListPage() {
  const [filters, setFilters] = useState({
    artistName: '',
    category: '',
    tags: [],
  })
  const queryClient = useQueryClient()
  const user = useAuthStore((state) => state.user)
  const isBuyer = String(user?.role || '').toLowerCase() === ROLES.BUYER

  const { data: artworks = [], isLoading } = usePublicArtworks(filters)
  const { data: watchlist = [] } = useQuery({
    queryKey: ['watchlist', user?.id],
    queryFn: () => listWatchlist(user.id),
    enabled: Boolean(user?.id && isBuyer),
  })

  const watchlistMutation = useMutation({
    mutationFn: ({ artworkId, isInWatchlist }) => toggleWatchlist(user.id, artworkId, isInWatchlist),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['watchlist', user.id] })
    },
  })

  const watchlistIds = new Set(watchlist.map((artwork) => String(artwork.id)))

  return (
    <section className="space-y-5">
      <PageHeader
        title="All approved auctions"
        description="Filter by artist, category, and tags to find live, upcoming, and ended auctions."
      />

      <FilterBar onChange={setFilters} />

      {isLoading ? <p className="text-sm text-stone-600">Loading auctions...</p> : null}

      {!isLoading && artworks.length === 0 ? (
        <div className="rounded-xl border border-dashed border-stone-300 p-6 text-sm text-stone-600">
          No artworks match your filters.
        </div>
      ) : null}

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {artworks.map((artwork) => (
          <ArtworkCard
            key={artwork.id}
            artwork={artwork}
            showWatchlistAction={isBuyer}
            isInWatchlist={watchlistIds.has(String(artwork.id))}
            isUpdatingWatchlist={
              watchlistMutation.isPending &&
              String(watchlistMutation.variables?.artworkId) === String(artwork.id)
            }
            onToggleWatchlist={() =>
              watchlistMutation.mutate({
                artworkId: artwork.id,
                isInWatchlist: watchlistIds.has(String(artwork.id)),
              })
            }
          />
        ))}
      </div>
    </section>
  )
}
