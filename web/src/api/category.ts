import request from '@/utils/request'

export interface Category {
  id: number
  name: string
  createdAt: string
  updatedAt: string
}

export function getCategories() {
  return request<Category[]>({
    url: '/categories',
    method: 'get'
  })
} 