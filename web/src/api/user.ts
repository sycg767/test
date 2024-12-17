import request from '@/utils/request'
import type { LoginResponse } from '@/types/api'

export interface LoginData {
  username: string
  password: string
}

export interface ChangePasswordData {
  oldPassword: string
  newPassword: string
}

export function login(data: LoginData) {
  return request<LoginResponse>({
    url: '/admin/login',
    method: 'post',
    data
  })
}

export function changePassword(data: ChangePasswordData) {
  return request({
    url: '/admin/password',
    method: 'put',
    data
  })
}

export function getInfo() {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
} 