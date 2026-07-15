import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Dashboard, CustomerDashboardOverview } from '../../../../core/services/dashboard';
import { Token } from '../../../auth/services/token';

@Component({
  selector: 'app-customer-dashboard',
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-dashboard.html',
  styleUrl: './customer-dashboard.css',
})
export class CustomerDashboard implements OnInit {
  private readonly dashboardService = inject(Dashboard);
  private readonly tokenService = inject(Token);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly overview = signal<CustomerDashboardOverview | null>(null);

  readonly username = signal('');
  readonly lockedCustomerId = signal<number | null>(null);

  readonly effectiveCustomerId = computed(() => this.lockedCustomerId() ?? this.parseCustomerId(this.customerIdInput));

  customerIdInput = '';

  ngOnInit(): void {
    this.username.set(this.tokenService.getUsername());

    const tokenCustomerId = this.parseCustomerId(this.tokenService.getUsername());
    const queryCustomerId = this.parseCustomerId(this.route.snapshot.queryParamMap.get('customerId'));

    if (tokenCustomerId !== null) {
      this.lockedCustomerId.set(tokenCustomerId);
      this.customerIdInput = String(tokenCustomerId);
      this.loadOverview(tokenCustomerId);
      return;
    }

    if (queryCustomerId !== null) {
      this.customerIdInput = String(queryCustomerId);
      this.loadOverview(queryCustomerId);
    }
  }

  onLoadDashboard(): void {
    const customerId = this.effectiveCustomerId();
    if (customerId === null) {
      this.errorMessage.set('Enter a valid customer ID to load your dashboard.');
      return;
    }

    this.loadOverview(customerId);
  }

  goToOrders(): void {
    const customerId = this.effectiveCustomerId();
    void this.router.navigate(['/orders'], {
      queryParams: customerId !== null ? { customerId } : {},
    });
  }

  goToCreateOrder(): void {
    const customerId = this.effectiveCustomerId();
    void this.router.navigate(['/orders/create'], {
      queryParams: customerId !== null ? { customerId } : {},
    });
  }

  goToProducts(): void {
    void this.router.navigate(['/products']);
  }

  private loadOverview(customerId: number): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.dashboardService.getCustomerOverview(customerId).subscribe({
      next: (data) => {
        this.overview.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load customer dashboard data. Verify your customer ID.');
        this.loading.set(false);
      },
    });
  }

  private parseCustomerId(value: string | null): number | null {
    if (!value) {
      return null;
    }

    // Try direct parsing first (for query params)
    const directParse = Number(value);
    if (Number.isInteger(directParse) && directParse > 0) {
      return directParse;
    }

    // Extract numeric part from username like "customer005" → 5
    const match = value.match(/\d+/);
    if (match) {
      const id = Number(match[0]);
      if (id > 0) {
        return id;
      }
    }

    return null;
  }
}
