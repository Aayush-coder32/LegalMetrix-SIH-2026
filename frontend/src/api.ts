import axios from 'axios'

const configuredApiUrl = import.meta.env.VITE_API_URL?.trim()
const deployedApiUrl = 'https://legalmetrix-sih-2026-1.onrender.com/api'
const defaultApiUrl = typeof window !== 'undefined' && window.location.hostname.endsWith('.vercel.app')
  ? deployedApiUrl
  : 'http://localhost:8080/api'

export const api = axios.create({
  baseURL: configuredApiUrl || defaultApiUrl,
  timeout: 12000,
})

let token: string | null = null

export function setAccessToken(value: string | null) {
  token = value
}

api.interceptors.request.use((config) => {
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
