import { formatCurrency } from '../../utils/currency'

export function BidHistoryTable({ bids }) {
  if (!bids.length) {
    return (
      <div className="rounded-lg border border-dashed border-stone-300 p-4 text-sm text-stone-600">
        No bids yet.
      </div>
    )
  }

  return (
    <div className="overflow-x-auto rounded-lg border border-[var(--line)]">
      <table className="min-w-full bg-white text-left text-sm">
        <thead className="bg-stone-100 text-xs uppercase tracking-wide text-stone-700">
          <tr>
            <th className="px-4 py-3">Bidder</th>
            <th className="px-4 py-3">Price</th>
            <th className="px-4 py-3">Bid Date</th>
          </tr>
        </thead>
        <tbody>
          {bids.map((bid, index) => (
            <tr key={bid.id ?? `${bid.buyerName}-${index}`} className="border-t border-stone-200">
              <td className="px-4 py-3 font-semibold text-stone-800">{bid.buyerName}</td>
              <td className="px-4 py-3 text-stone-700">{formatCurrency(bid.buyerPrice)}</td>
              <td className="px-4 py-3 text-stone-700">{Intl.DateTimeFormat('en-US', {day: '2-digit',
                month: 'short',
                hour: '2-digit',
                minute: '2-digit',
                hour12: true}).format(new Date(bid.bidTime))}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
