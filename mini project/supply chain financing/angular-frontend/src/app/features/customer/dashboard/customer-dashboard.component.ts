import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../../core/services/customer.service';
import { CustomerProfileDto, LoanDto, PaymentMode } from '../../../core/models/dto/customer.dto';
import { toErrorMessage } from '../../../core/utils/http-error.util';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-dashboard.component.html',
  styleUrl: './customer-dashboard.component.css'
})
export class CustomerDashboardComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly formBuilder = inject(FormBuilder);

  readonly profile = signal<CustomerProfileDto | null>(null);
  readonly loans = signal<LoanDto[]>([]);
  readonly errorMessage = signal('');
  readonly loading = signal(false);

  readonly paymentModes: PaymentMode[] = ['UPI', 'NEFT', 'IMPS', 'RTGS', 'CASH', 'CARD'];

  readonly applyLoanForm = this.formBuilder.group({
    amount: [500000, [Validators.required, Validators.min(1)]],
    interestRate: [12.5, [Validators.required, Validators.min(0.1)]],
    tenureMonths: [24, [Validators.required, Validators.min(1)]],
    purpose: ['Inventory Purchase', [Validators.required]]
  });

  readonly repayLoanForm = this.formBuilder.group({
    loanId: [0, [Validators.required, Validators.min(1)]],
    amount: [1000, [Validators.required, Validators.min(1)]],
    paymentMode: ['NEFT' as PaymentMode, [Validators.required]],
    remarks: ['Monthly installment', [Validators.required]]
  });

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.errorMessage.set('');
    this.loading.set(true);

    this.customerService.getProfile().subscribe({
      next: (profile) => this.profile.set(profile),
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });

    this.customerService.getMyLoans().subscribe({
      next: (loans) => this.loans.set(loans),
      error: (error) => this.errorMessage.set(toErrorMessage(error)),
      complete: () => this.loading.set(false)
    });
  }

  applyLoan(): void {
    if (this.applyLoanForm.invalid) {
      this.applyLoanForm.markAllAsTouched();
      return;
    }

    this.errorMessage.set('');

    this.customerService.applyLoan(this.applyLoanForm.getRawValue() as {
      amount: number;
      interestRate: number;
      tenureMonths: number;
      purpose: string;
    }).subscribe({
      next: () => {
        this.applyLoanForm.patchValue({ purpose: '' });
        this.refresh();
      },
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });
  }

  repayLoan(): void {
    if (this.repayLoanForm.invalid) {
      this.repayLoanForm.markAllAsTouched();
      return;
    }

    this.errorMessage.set('');
    const { loanId, amount, paymentMode, remarks } = this.repayLoanForm.getRawValue();

    this.customerService.repayLoan(loanId ?? 0, {
      amount: amount ?? 0,
      paymentMode: paymentMode ?? 'NEFT',
      remarks: remarks ?? ''
    }).subscribe({
      next: () => {
        this.repayLoanForm.patchValue({ amount: 1000, remarks: 'Monthly installment' });
        this.refresh();
      },
      error: (error) => this.errorMessage.set(toErrorMessage(error))
    });
  }
}
