import request from '@/utils/request'
import type { User } from './user'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
}

// 登录
export function login(data: LoginParams) {
  return request<LoginResult>({
    url: '/auth/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return request<User>({
    url: '/auth/info',
    method: 'get'
  })
}

// 登出
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
} 