import { defineStore } from 'pinia'
import { login, getInfo, logout } from '@/api/user'
import type { UserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null as UserInfo | null
  }),

  actions: {
    async login(username: string, password: string) {
      try {
        const { data } = await login({ username, password })
        this.token = data.token
        localStorage.setItem('token', data.token)
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },

    async getUserInfo() {
      try {
        const { data } = await getInfo()
        this.userInfo = data
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },

    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('Logout failed:', error)
      } finally {
        this.token = ''
        this.userInfo = null
        localStorage.removeItem('token')
      }
    }
  }
}) 