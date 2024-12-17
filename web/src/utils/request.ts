import axios from 'axios'
import type { AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'

interface RequestConfig extends AxiosRequestConfig {
  headers?: Record<string, string>
}

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL || 'http://localhost:8080',
  timeout: 5000,
  withCredentials: false
})

// 请求拦截器
service.interceptors.request.use(
  (config: RequestConfig) => {
    const token = localStorage.getItem('token')
    if (token) {
      if (!config.headers) {
        config.headers = {}
      }
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error) => {
    ElMessage.error(error.response?.data?.error || '请求失败')
    return Promise.reject(error)
  }
)

export default service 