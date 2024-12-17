export interface LoginResponse {
  token: string;
  user: {
    id: number;
    username: string;
    nickname: string;
    roles: string;
    avatar?: string;
    email?: string;
    phone?: string;
    status: string;
  }
} 