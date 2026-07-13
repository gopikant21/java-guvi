import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { AdminService } from '../../../core/services/admin.service';
import { AdminDashboardDto, AdminLoanDetailsDto } from '../../../core/models/dto/admin.dto';
import { LoanDto, LoanStatus } from '../../../core/models/dto/customer.dto';
import { toErrorMessage } from '../../../core/utils/http-error.util';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  private readonly adminService = inject(AdminService);
  private readonly formBuilder = inject(FormBuilder);

  readonly dashboard = signal<AdminDashboardDto | null>(null);
  readonly loans = signal<LoanDto[]>([]);
  readonly loading = signal(false);
  readonly detailLoading = signal(false);
  readonly errorMessage = signal('');
  readonly detailErrorMessage = signal('');
  readonly selectedLoanDetails = signal<AdminLoanDetailsDto | null>(null);
  readonly statuses: LoanStatus[] = ['PENDING', 'APPROVED', 'REJECTED', 'DISBURSED', 'PARTIALLY_PAID', 'FULLY_PAID', 'CLOSED'];

  readonly filterForm = this.formBuilder.group({
    status: ['' as LoanStatus | ''],
    customerId: [null as number | null]
  });

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.errorMessage.set('');
    this.loading.set(true);

    this.adminService.getDashboard().subscribe({
      next: (dashboard) => this.dashboard.set(dashboard),
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });

    this.loadLoans();
  }

  loadLoans(): void {
    const { status, customerId } = this.filterForm.getRawValue();

    this.adminService.getLoans({
      status: status || undefined,
      customerId: customerId ?? undefined
    }).subscribe({
      next: (loans) => this.loans.set(loans),
      error: (error) => this.errorMessage.set(toErrorMessage(error)),
      complete: () => this.loading.set(false)
    });
  }

  approve(loanId: number): void {
    this.adminService.approveLoan(loanId).subscribe({
      next: () => this.refresh(),
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });
  }

  reject(loanId: number): void {
    const reason = window.prompt('Rejection reason');
    if (!reason) {
      return;
    }

    this.adminService.rejectLoan(loanId, { reason }).subscribe({
      next: () => this.refresh(),
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });
  }

  disburse(loanId: number): void {
    this.adminService.disburseLoan(loanId).subscribe({
      next: () => this.refresh(),
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });
  }

  openLoanDetails(loanId: number): void {
    this.detailErrorMessage.set('');
    this.detailLoading.set(true);
    this.selectedLoanDetails.set(null);

    this.adminService.getLoanDetails(loanId).subscribe({
      next: (details) => this.selectedLoanDetails.set(details),
      error: (error) => this.detailErrorMessage.set(toErrorMessage(error)),
      complete: () => this.detailLoading.set(false)
    });
  }

  closeLoanDetails(): void {
    this.selectedLoanDetails.set(null);
    this.detailErrorMessage.set('');
    this.detailLoading.set(false);
  }
}
