import { Component, inject } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { Auth } from '../../features/auth/services/auth';
import { Token } from '../../features/auth/services/token';

@Component({
  selector: 'app-admin-layout',
  imports: [RouterOutlet, RouterModule],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout {
  private readonly authService = inject(Auth);
  private readonly tokenService = inject(Token);

  get username(): string {
    return this.tokenService.getUsername() || 'Admin';
  }

  get roles(): string {
    return this.tokenService.getRoles().join(', ') || 'ROLE_ADMIN';
  }

  logout(): void {
    this.authService.logout(true);
  }
}
