export interface LoginRequest {
  username: string;
  password: string;
}

export interface JwtPayload {
  sub?: string;
  roles?: string[];
  exp?: number;
  iat?: number;
}

export interface AuthSession {
  token: string;
  username: string;
  roles: string[];
  redirectUrl: string;
}
