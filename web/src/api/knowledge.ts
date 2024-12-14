import request from '@/utils/request'

export interface KnowledgePoint {
  id: number
  name: string
  description?: string
  parentId?: number
  level: number
  questionCount: number
  children?: KnowledgePoint[]
  createdAt: string
  updatedAt: string
}

export function getKnowledgePoints(params?: {
  keyword?: string
  parentId?: number
}) {
  return request<KnowledgePoint[]>({
    url: '/knowledge-points',
    method: 'get',
    params
  })
}

export function createKnowledgePoint(data: {
  name: string
  description?: string
  parentId?: number
}) {
  return request<KnowledgePoint>({
    url: '/knowledge-points',
    method: 'post',
    data
  })
}

export function updateKnowledgePoint(id: number, data: {
  name: string
  description?: string
  parentId?: number
}) {
  return request<KnowledgePoint>({
    url: `/knowledge-points/${id}`,
    method: 'put',
    data
  })
}

export function deleteKnowledgePoint(id: number) {
  return request({
    url: `/knowledge-points/${id}`,
    method: 'delete'
  })
}

export function getKnowledgePointQuestions(id: number, params: {
  page: number
  pageSize: number
}) {
  return request<{
    content: Question[]
    total: number
  }>({
    url: `/knowledge-points/${id}/questions`,
    method: 'get',
    params
  })
}

export function associateQuestions(id: number, questionIds: number[]) {
  return request({
    url: `/knowledge-points/${id}/questions`,
    method: 'post',
    data: { questionIds }
  })
}

export function dissociateQuestions(id: number, questionIds: number[]) {
  return request({
    url: `/knowledge-points/${id}/questions`,
    method: 'delete',
    data: { questionIds }
  })
} 