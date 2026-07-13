import { Injectable } from '@angular/core';
import { JwtPayload } from '../models/auth.models';

@Injectable({ providedIn: 'root' })
export class Token {
	private readonly tokenKey = 'auth_token';

	saveToken(token: string): void {
		localStorage.setItem(this.tokenKey, token);
	}

	getToken(): string | null {
		return localStorage.getItem(this.tokenKey);
	}

	clearToken(): void {
		localStorage.removeItem(this.tokenKey);
	}

	getPayload(): JwtPayload | null {
		const token = this.getToken();
		if (!token) {
			return null;
		}

		try {
			const parts = token.split('.');
			if (parts.length < 2) {
				return null;
			}

			const payload = parts[1]
				.replace(/-/g, '+')
				.replace(/_/g, '/');

			const paddedPayload = payload.padEnd(Math.ceil(payload.length / 4) * 4, '=');
			const decoded = atob(paddedPayload);
			return JSON.parse(decoded) as JwtPayload;
		} catch {
			return null;
		}
	}

	getUsername(): string {
		const payload = this.getPayload();
		return payload?.sub ?? '';
	}

	getRoles(): string[] {
		const payload = this.getPayload();
		return payload?.roles ?? [];
	}

	hasRole(role: string): boolean {
		return this.getRoles().includes(role);
	}

	isExpired(): boolean {
		const payload = this.getPayload();
		if (!payload?.exp) {
			return true;
		}

		const nowInSeconds = Math.floor(Date.now() / 1000);
		return payload.exp <= nowInSeconds;
	}

	isAuthenticated(): boolean {
		const token = this.getToken();
		return !!token && !this.isExpired();
	}
}
