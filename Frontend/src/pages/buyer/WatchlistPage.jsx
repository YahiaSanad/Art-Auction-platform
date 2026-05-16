import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useAuthStore } from '../../store/authStore'
import { listWatchlist, removeFromWatchlist } from '../../services/watchlistService'
import { ArtworkCard } from '../../components/artworks/ArtworkCard'

export function WatchlistPage() {
  const user = useAuthStore((state) => state.user)
  const queryClient = useQueryClient()

  const { data: artworks = [], isLoading } = useQuery({
    queryKey: ['watchlist', user?.id],
    queryFn: () => listWatchlist(user.id),
    enabled: Boolean(user?.id),
  })

  const removeMutation = useMutation({
    mutationFn: (artworkId) => removeFromWatchlist(user.id, artworkId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['watchlist', user.id] })
    },
  })

  return (
    <section className="space-y-4">
      <h1 className="text-2xl font-extrabold text-stone-900">Watchlist</h1>

      {isLoading ? <p className="text-sm text-stone-600">Loading watchlist...</p> : null}

      {!isLoading && artworks.length === 0 ? (
        <p className="rounded-lg border border-dashed border-stone-300 p-4 text-sm text-stone-600">
          No artworks in your watchlist yet.
        </p>
      ) : null}

      <div className="grid gap-4 lg:grid-cols-2">
        {artworks.map((artwork) => (
          <ArtworkCard
            key={artwork.id}
            artwork={artwork}
            showWatchlistAction
            isInWatchlist
            isUpdatingWatchlist={
              removeMutation.isPending && String(removeMutation.variables) === String(artwork.id)
            }
            onToggleWatchlist={() => removeMutation.mutate(artwork.id)}
          />
        ))}
      </div>
    </section>
  )
}
