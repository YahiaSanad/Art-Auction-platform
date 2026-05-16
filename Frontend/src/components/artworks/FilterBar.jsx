import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { ARTWORK_CATEGORIES } from '../../constants/categories'
import { getAllTags } from '../../api/TagApis'

const defaultFilters = {
  artistName: '',
  category: '',
  tags: [],
}

export function FilterBar({ onChange }) {
  const [values, setValues] = useState(defaultFilters)
  const [isTagsOpen, setIsTagsOpen] = useState(false)
  const { data: tagsOptions = [], isLoading: isLoadingTags } = useQuery({
    queryKey: ['tags'],
    queryFn: getAllTags,
  })

  const handleFieldChange = (event) => {
    const { name, value } = event.target

    const nextValues = {
      ...values,
      [name]: value,
    }

    setValues(nextValues)
    onChange(nextValues)
  }

  const handleTagToggle = (tagName) => {
    const nextTags = values.tags.includes(tagName)
      ? values.tags.filter((tag) => tag !== tagName)
      : [...values.tags, tagName]

    const nextValues = {
      ...values,
      tags: nextTags,
    }

    setValues(nextValues)
    onChange(nextValues)
  }

  const selectedTagsCount = values.tags.length

  return (
    <div className="grid gap-3 rounded-xl border border-[var(--line)] bg-white p-4 sm:grid-cols-3">
      <label className="text-sm font-semibold text-stone-700">
        Artist
        <input
          name="artistName"
          value={values.artistName}
          onChange={handleFieldChange}
          className="mt-1 w-full rounded-lg border border-stone-300 px-3 py-2"
          placeholder="Search by artist"
        />
      </label>

      <label className="text-sm font-semibold text-stone-700">
        Category
        <select
          name="category"
          value={values.category}
          onChange={handleFieldChange}
          className="mt-1 w-full rounded-lg border border-stone-300 px-3 py-2"
        >
          <option value="">All categories</option>
          {ARTWORK_CATEGORIES.map((category) => (
            <option key={category.id} value={category.name}>
              {category.name}
            </option>
          ))}
        </select>
      </label>

      <div className="text-sm font-semibold text-stone-700">
        <p>Tags</p>
        <div className="relative mt-1">
          <button
            type="button"
            onClick={() => setIsTagsOpen((prev) => !prev)}
            className="flex w-full items-center justify-between rounded-lg border border-stone-300 bg-white px-3 py-2 text-left font-normal text-stone-700"
          >
            <span>
              {selectedTagsCount === 0
                ? 'Select tags'
                : `${selectedTagsCount} tag${selectedTagsCount > 1 ? 's' : ''} selected`}
            </span>
            <span className="text-xs text-stone-500">{isTagsOpen ? '▲' : '▼'}</span>
          </button>

          {isTagsOpen ? (
            <div className="absolute z-20 mt-1 max-h-48 w-full overflow-auto rounded-lg border border-stone-200 bg-white p-2 shadow-lg">
              {isLoadingTags ? (
                <p className="px-2 py-1 text-xs font-normal text-stone-500">Loading tags...</p>
              ) : null}
              {!isLoadingTags && tagsOptions.length === 0 ? (
                <p className="px-2 py-1 text-xs font-normal text-stone-500">No tags available.</p>
              ) : null}
              {!isLoadingTags && tagsOptions.length > 0 ? (
                <div className="space-y-2">
                  {tagsOptions.map((tag) => (
                    <label
                      key={tag.id}
                      className="flex items-center gap-2 rounded-lg border border-stone-200 px-3 py-2 font-normal text-stone-700"
                    >
                      <input
                        type="checkbox"
                        checked={values.tags.includes(tag.name)}
                        onChange={() => handleTagToggle(tag.name)}
                      />
                      <span>{tag.name}</span>
                    </label>
                  ))}
                </div>
              ) : null}
            </div>
          ) : null}
        </div>
      </div>
    </div>
  )
}
