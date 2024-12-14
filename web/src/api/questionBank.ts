import request from '@/utils/request'

export interface QuestionBank {
  id: number
  name: string
  description: string
  category: {
    id: number
    name: string
  }
  questionCount: number
  userCount: number
  createdAt: string
  updatedAt: string
}

export interface QueryParams {
  page: number
  pageSize: number
  keyword?: string
  categoryId?: number
}

export function getQuestionBanks(params: QueryParams) {
  return request<{
    content: QuestionBank[]
    total: number
  }>({
    url: '/banks',
    method: 'get',
    params
  })
}

export function getQuestionBank(id: number) {
  return request<QuestionBank>({
    url: `/banks/${id}`,
    method: 'get'
  })
}

export interface CreateQuestionBankData {
  name: string
  categoryId: number
  description?: string
}

export function createQuestionBank(data: CreateQuestionBankData) {
  return request<QuestionBank>({
    url: '/banks',
    method: 'post',
    data
  })
}

export function updateQuestionBank(id: number, data: CreateQuestionBankData) {
  return request<QuestionBank>({
    url: `/banks/${id}`,
    method: 'put',
    data
  })
}

export function deleteQuestionBank(id: number) {
  return request({
    url: `/banks/${id}`,
    method: 'delete'
  })
} 