import axios from 'axios'

export const api = axios.create({ baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api', timeout: 12000 })
let token: string | null = null
export function setAccessToken(value: string | null) { token = value }
api.interceptors.request.use((config) => { if (token) config.headers.Authorization = `Bearer ${token}`; return config })
