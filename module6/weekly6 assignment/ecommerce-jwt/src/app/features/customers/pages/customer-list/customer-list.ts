import { Component, OnInit, inject, signal } from '@angular/core';
import { Customer } from '../../services/customer';
import { CustomerResponse } from '../../models/customer.models';

@Component({
  selector: 'app-customer-list',
  imports: [],
  templateUrl: './customer-list.html',
  styleUrl: './customer-list.css',
})
export class CustomerList implements OnInit {
  private readonly customerService = inject(Customer);

  readonly customers = signal<CustomerResponse[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal('');

  ngOnInit(): void {
    this.fetchCustomers();
  }

  fetchCustomers(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.customerService.getAllCustomers().subscribe({
      next: (data) => {
        this.customers.set(data ?? []);
        this.loading.set(false);
      },
      error: (error: unknown) => {
        const message =
          error && typeof error === 'object' && 'message' in error
            ? String((error as { message?: string }).message)
            : 'Failed to load customers.';
        this.errorMessage.set(message);
        this.loading.set(false);
      },
    });
  }
}