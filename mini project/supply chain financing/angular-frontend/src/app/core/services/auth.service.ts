import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import {
  JwtPayloadDto,
  LoginRequestDto,
  LoginResponseDto,
  MessageResponseDto,
  RegisterRequestDto,
  Role
} from '../models/dto/auth.dto';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly tokenStorageKey = 'authToken';
  private readonly apiBaseUrl = 'http://localhost:8080/api';

  private readonly tokenValue = signal<string | null>(localStorage.getItem(this.tokenStorageKey));
  readonly isAuthenticated = computed(() => !!this.tokenValue());
  readonly role = computed<Role | null>(() => this.getRoleFromToken(this.tokenValue()));

  login(payload: LoginRequestDto): Observable<Role | null> {
    return this.http.post<LoginResponseDto>(`${this.apiBaseUrl}/auth/login`, payload).pipe(
      tap((response) => this.storeToken(response.token)),
      map(() => this.role())
    );
  }

  register(payload: RegisterRequestDto): Observable<MessageResponseDto> {
    return this.http.post<MessageResponseDto>(`${this.apiBaseUrl}/auth/register`, payload);
  }

  logout(): void {
    this.storeToken(null);
  }

  getToken(): string | null {
    return this.tokenValue();
  }

  hasAnyRole(roles: Role[]): boolean {
    const currentRole = this.role();
    return !!currentRole && roles.includes(currentRole);
  }

  private storeToken(token: string | null): void {
    if (token) {
      localStorage.setItem(this.tokenStorageKey, token);
    } else {
      localStorage.removeItem(this.tokenStorageKey);
    }

    this.tokenValue.set(token);
  }

  private getRoleFromToken(token: string | null): Role | null {
    if (!token) {
      return null;
    }

    const payload = this.parsePayload(token);
    return payload?.role ?? null;
  }

  private parsePayload(token: string): JwtPayloadDto | null {
    try {
      const payloadPart = token.split('.')[1];
      if (!payloadPart) {
        return null;
      }

      const padded = payloadPart.padEnd(payloadPart.length + ((4 - (payloadPart.length % 4)) % 4), '=');
      const json = atob(padded.replace(/-/g, '+').replace(/_/g, '/'));
      return JSON.parse(json) as JwtPayloadDto;
    } catch {
      return null;
    }
  }
}
