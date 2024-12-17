import request from '@/utils/request'

export interface QuestionBankQuery {
  page?: number
  size?: number
  categoryId?: number
  keyword?: string
}

export interface QuestionBank {
  id: number
  name: string
  description: string
  category: {
    id: number
    name: string
  }
  cover: string
  questionCount: number
  userCount: number
  status: number
  createdAt: string
  updatedAt: string
}

export function getQuestionBankList(params: QuestionBankQuery) {
  return request<{
    content: QuestionBank[]
    totalElements: number
    totalPages: number
  }>({
    url: '/api/banks',
    method: 'get',
    params
  })
}

export function getQuestionBankDetail(id: number) {
  return request<QuestionBank>({
    url: `/api/banks/${id}`,
    method: 'get'
  })
}

export function createQuestionBank(data: Partial<QuestionBank>) {
  return request({
    url: '/api/banks',
    method: 'post',
    data
  })
}

export function updateQuestionBank(id: number, data: Partial<QuestionBank>) {
  return request({
    url: `/api/banks/${id}`,
    method: 'put',
    data
  })
}

export function deleteQuestionBank(id: number) {
  return request({
    url: `/api/banks/${id}`,
    method: 'delete'
  })
} 