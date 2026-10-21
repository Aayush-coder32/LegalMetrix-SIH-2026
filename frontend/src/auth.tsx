import { createContext, useContext, useMemo, useState, type PropsWithChildren } from 'react'
import { api, setAccessToken } from './api'

export type User = { id: string; email: string; fullName: string; phone?: string; status: string; roles: string[] }
type Credentials = { email: string; password: string }
type AuthContextValue = { user: User | null; login: (values: Credentials) => Promise<void>; register: (values: Credentials & { fullName: string; phone?: string }) => Promise<void>; logout: () => void }
const AuthContext = createContext<AuthContextValue | null>(null)
export function AuthProvider({ children }: PropsWithChildren) {
  const [user, setUser] = useState<User | null>(null)
  async function authenticate(path: string, values: Credentials | (Credentials & { fullName: string; phone?: string })) {
    const response = await api.post(path, values)
    setAccessToken(response.data.accessToken)
    setUser(response.data.user)
  }
  const value = useMemo(() => ({ user, login: (v: Credentials) => authenticate('/auth/login', v), register: (v: Credentials & { fullName: string; phone?: string }) => authenticate('/auth/register', v), logout: () => { setAccessToken(null); setUser(null) } }), [user])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
export function useAuth() { const value = useContext(AuthContext); if (!value) throw new Error('useAuth must be used inside AuthProvider'); return value }
