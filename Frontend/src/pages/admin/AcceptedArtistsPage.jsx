import { useQuery } from '@tanstack/react-query'
import { getApprovedArtists } from '../../services/adminService'

export function AcceptedArtistsPage() {
  const { data: artists = [], isLoading } = useQuery({
    queryKey: ['admin', 'approved-artists'],
    queryFn: getApprovedArtists,
  })

  return (
    <section className="space-y-4">
      <h1 className="text-2xl font-extrabold text-stone-900">Accepted artists</h1>

      {isLoading ? <p className="text-sm text-stone-600">Loading accepted artists...</p> : null}

      {!isLoading && artists.length === 0 ? (
        <p className="rounded-lg border border-dashed border-stone-300 p-4 text-sm text-stone-600">
          No accepted artists found.
        </p>
      ) : null}

      {artists.length > 0 ? (
        <div className="overflow-x-auto rounded-lg border border-[var(--line)]">
          <table className="min-w-full bg-white text-left text-sm">
            <thead className="bg-stone-100 text-xs uppercase tracking-wide text-stone-700">
              <tr>
                <th className="px-4 py-3">Artist</th>
                <th className="px-4 py-3">Email</th>
                <th className="px-4 py-3">Artworks</th>
              </tr>
            </thead>
            <tbody>
              {artists.map((artist) => (
                <tr key={artist.id} className="border-t border-stone-200">
                  <td className="px-4 py-3 font-semibold text-stone-800">{artist.name}</td>
                  <td className="px-4 py-3 text-stone-700">{artist.email}</td>
                  <td className="px-4 py-3 text-stone-700">{artist.artworksCount}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : null}
    </section>
  )
}
