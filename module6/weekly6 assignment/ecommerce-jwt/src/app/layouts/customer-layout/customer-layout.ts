import { Component, inject } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { Auth } from '../../features/auth/services/auth';
import { Token } from '../../features/auth/services/token';

@Component({
  selector: 'app-customer-layout',
  imports: [RouterOutlet, RouterModule],
  templateUrl: './customer-layout.html',
  styleUrl: './customer-layout.css',
})
export class CustomerLayout {
  private readonly authService = inject(Auth);
  private readonly tokenService = inject(Token);

  get username(): string {
    return this.tokenService.getUsername() || 'User';
  }

  get roles(): string {
    return this.tokenService.getRoles().join(', ') || 'ROLE_USER';
  }

  logout(): void {
    this.authService.logout(true);
  }
}
