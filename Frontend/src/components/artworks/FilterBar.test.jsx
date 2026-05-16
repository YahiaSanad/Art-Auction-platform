import { fireEvent, render, screen } from '@testing-library/react'
import { describe, expect, it, vi } from 'vitest'
import { FilterBar } from './FilterBar'

vi.mock('../../constants/categories', () => ({
  ARTWORK_CATEGORIES: [
    { id: 1, name: 'Abstract' },
    { id: 2, name: 'Portrait' },
  ],
}))

vi.mock('@tanstack/react-query', () => ({
  useQuery: () => ({
    data: [
      { id: 1, name: 'modern' },
      { id: 2, name: 'city' },
    ],
    isLoading: false,
  }),
}))

describe('FilterBar', () => {
  it('sends checklist filters to callback', () => {
    const onChange = vi.fn()

    render(<FilterBar onChange={onChange} />)

    fireEvent.change(screen.getByPlaceholderText('Search by artist'), {
      target: { value: 'Lina' },
    })
    fireEvent.click(screen.getByRole('button', { name: /select tags/i }))
    fireEvent.click(screen.getByRole('checkbox', { name: 'modern' }))
    fireEvent.click(screen.getByRole('checkbox', { name: 'city' }))

    expect(onChange).toHaveBeenCalled()
    expect(onChange).toHaveBeenLastCalledWith(
      expect.objectContaining({ artistName: 'Lina', tags: ['modern', 'city'] }),
    )
  })
})
