export type Role = 'ADMIN' | 'CUSTOMER';

export interface RegisterRequestDto {
  name: string;
  email: string;
  password: string;
  phone: string;
}

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface LoginResponseDto {
  token: string;
}

export interface MessageResponseDto {
  message: string;
}

export interface JwtPayloadDto {
  sub: string;
  role: Role;
  iat: number;
  exp: number;
}

export interface ApiErrorDto {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  errors?: Record<string, string>;
}
