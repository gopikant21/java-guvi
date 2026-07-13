import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, map, Observable, throwError } from 'rxjs';
import { AuthSession, LoginRequest } from '../models/auth.models';
import { Token } from './token';

@Injectable({ providedIn: 'root' })
export class Auth {
	private readonly http = inject(HttpClient);
	private readonly tokenService = inject(Token);
	private readonly router = inject(Router);
	private readonly baseUrl = 'http://localhost:8080';

	login(payload: LoginRequest): Observable<AuthSession> {
		return this.http
			.post(`${this.baseUrl}/auth/login`, payload, {
				responseType: 'text',
				headers: { 'x-skip-global-error': 'true' }
			})
			.pipe(
				map((token) => {
					const jwt = token?.trim();
					if (!jwt) {
						throw new Error('Login failed. Empty token received from server.');
					}

					this.tokenService.saveToken(jwt);
					const roles = this.tokenService.getRoles();
					const session: AuthSession = {
						token: jwt,
						username: this.tokenService.getUsername(),
						roles,
						redirectUrl: this.resolveRedirectUrl(roles)
					};

					return session;
				}),
				catchError((error: unknown) => {
					const message = this.extractErrorMessage(error);
					this.tokenService.clearToken();
					return throwError(() => new Error(message));
				})
			);
	}

	logout(redirectToLogin = true): void {
		this.tokenService.clearToken();
		if (redirectToLogin) {
			void this.router.navigate(['/login']);
		}
	}

	resolveRedirectUrl(roles: string[] = this.tokenService.getRoles()): string {
		if (roles.includes('ROLE_ADMIN')) {
			return '/dashboard/admin';
		}

		return '/dashboard/customer';
	}

	private extractErrorMessage(error: unknown): string {
		if (!(error instanceof HttpErrorResponse)) {
			return 'Unexpected error occurred. Please try again.';
		}

		if (error.status === 0) {
			return 'Server unavailable. Please check backend connection.';
		}

		if (error.status === 401) {
			return getAuthErrorMessage(error, 'Invalid username or password.');
		}

		return getAuthErrorMessage(error, `Login failed with status ${error.status}.`);
	}
}

interface ApiErrorBody {
	error?: string;
	message?: string;
}

function getAuthErrorMessage(error: HttpErrorResponse, fallback: string): string {
	const body = error.error as ApiErrorBody | string | null;

	if (typeof body === 'string' && body.trim().length > 0) {
		try {
			const parsed = JSON.parse(body) as ApiErrorBody;
			if (typeof parsed.message === 'string' && parsed.message.trim().length > 0) {
				return parsed.message;
			}
			if (typeof parsed.error === 'string' && parsed.error.trim().length > 0) {
				return parsed.error;
			}
		} catch {
			return body;
		}

		return body;
	}

	if (body && typeof body === 'object') {
		if (typeof body.message === 'string' && body.message.trim().length > 0) {
			return body.message;
		}

		if (typeof body.error === 'string' && body.error.trim().length > 0) {
			return body.error;
		}
	}

	return fallback;
}
