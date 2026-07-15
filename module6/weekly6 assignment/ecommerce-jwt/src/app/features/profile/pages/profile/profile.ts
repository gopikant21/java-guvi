import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs';
import { Token } from '../../../auth/services/token';
import { Customer } from '../../../customers/services/customer';
import { CustomerResponse } from '../../../customers/models/customer.models';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly tokenService = inject(Token);
  private readonly customerService = inject(Customer);

  readonly profile = signal<CustomerResponse | null>(null);
  readonly loading = signal(false);
  readonly updating = signal(false);
  readonly errorMessage = signal('');
  readonly successMessage = signal('');
  readonly username = this.tokenService.getUsername();

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.minLength(10)]],
    address: ['']
  });

  ngOnInit(): void {
    this.fetchProfile();
  }

  fetchProfile(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const customerId = this.extractCustomerId(this.username);
    if (customerId === null) {
      this.errorMessage.set('Unable to extract customer ID from your account.');
      this.loading.set(false);
      return;
    }

    this.customerService.getCustomerById(customerId).subscribe({
      next: (data: CustomerResponse) => {
        this.profile.set(data ?? null);
        this.form.patchValue(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load profile. Please try again.');
        this.loading.set(false);
      }
    });
  }

  private extractCustomerId(username: string): number | null {
    if (!username) {
      return null;
    }

    // Extract numeric part from username like "customer005" → 5
    const match = username.match(/\d+/);
    if (match) {
      const id = Number(match[0]);
      if (id > 0) {
        return id;
      }
    }

    return null;
  }

  onSubmit(): void {
    if (this.form.invalid || this.updating() || !this.profile()) {
      this.form.markAllAsTouched();
      return;
    }

    this.errorMessage.set('');
    this.successMessage.set('');
    this.updating.set(true);

    this.customerService.updateCustomer(this.profile()!.id, this.form.getRawValue())
      .pipe(finalize(() => this.updating.set(false)))
      .subscribe({
        next: (updated: CustomerResponse) => {
          this.profile.set(updated);
          this.successMessage.set('Profile updated successfully!');
          setTimeout(() => this.successMessage.set(''), 3000);
        },
        error: () => {
          this.errorMessage.set('Failed to update profile. Please try again.');
        }
      });
  }
}
