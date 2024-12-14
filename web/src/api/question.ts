import request from '@/utils/request'

export interface Question {
  id: number
  bankId: number
  type: 'SINGLE' | 'MULTIPLE' | 'JUDGE'
  content: string
  options: Option[]
  answer: string[]
  analysis: string
  difficulty: number
  correctRate: number
  createdAt: string
  updatedAt: string
}

export interface Option {
  label: string
  content: string
}

export interface QueryParams {
  page: number
  pageSize: number
  keyword?: string
  type?: string
  bankId: number
}

export function getQuestions(params: QueryParams) {
  return request<{
    content: Question[]
    total: number
  }>({
    url: '/questions',
    method: 'get',
    params
  })
}

export function getQuestion(id: number) {
  return request<Question>({
    url: `/questions/${id}`,
    method: 'get'
  })
}

export interface CreateQuestionData {
  bankId: number
  type: string
  content: string
  options: Option[]
  answer: string[]
  analysis?: string
  difficulty: number
}

export function createQuestion(data: CreateQuestionData) {
  return request<Question>({
    url: '/questions',
    method: 'post',
    data
  })
}

export function updateQuestion(id: number, data: CreateQuestionData) {
  return request<Question>({
    url: `/questions/${id}`,
    method: 'put',
    data
  })
}

export function deleteQuestion(id: number) {
  return request({
    url: `/questions/${id}`,
    method: 'delete'
  })
}

export function importQuestions(bankId: number, file: File) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('bankId', bankId.toString())
  
  return request({
    url: '/questions/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function getQuestionStats(bankId: number) {
  return request<{
    total: number
    singleCount: number
    multipleCount: number
    judgeCount: number
    avgCorrectRate: number
    totalAnswers: number
    todayAnswers: number
    weekAnswers: number
  }>({
    url: `/questions/stats`,
    method: 'get',
    params: { bankId }
  })
}

export function moveQuestions(data: {
  questionIds: number[]
  targetBankId: number
}) {
  return request({
    url: '/questions/move',
    method: 'post',
    data
  })
}

export function deleteQuestions(questionIds: number[]) {
  return request({
    url: '/questions/batch',
    method: 'delete',
    data: { questionIds }
  })
}

export function getQuestionAnalysis(params: {
  bankId: number
  startDate?: string
  endDate?: string
}) {
  return request<{
    trends: {
      dates: string[]
      counts: number[]
      correctRates: number[]
    }
    typeDistribution: {
      single: number
      multiple: number
      judge: number
    }
    difficultyDistribution: Record<number, number>
    errorRanking: Question[]
  }>({
    url: '/questions/analysis',
    method: 'get',
    params
  })
}

export function exportErrorQuestionList(bankId: number) {
  return request({
    url: '/questions/error/export',
    method: 'get',
    params: { bankId },
    responseType: 'blob'
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.download = '错题列表.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  })
} 