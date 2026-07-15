import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AdminDashboardOverview, Dashboard } from '../../../../core/services/dashboard';

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {
  private readonly dashboardService = inject(Dashboard);

  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly overview = signal<AdminDashboardOverview | null>(null);

  lowStockThreshold = 10;
  highValueThreshold = 1000;

  ngOnInit(): void {
    this.loadOverview();
  }

  onRefresh(): void {
    this.loadOverview();
  }

  private loadOverview(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.dashboardService
      .getAdminOverview(this.lowStockThreshold, this.highValueThreshold)
      .subscribe({
        next: (data) => {
          this.overview.set(data);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Failed to load admin dashboard data.');
          this.loading.set(false);
        },
      });
  }
}
