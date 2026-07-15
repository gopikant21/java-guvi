import { Injectable, inject } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { Customer } from '../../customers/services/customer';
import { CustomerResponse } from '../../customers/models/customer.models';
import { Token } from '../../auth/services/token';

@Injectable({ providedIn: 'root' })
export class Profile {
  private readonly customerService = inject(Customer);
  private readonly tokenService = inject(Token);

  getCurrentUserProfile(): Observable<CustomerResponse> {
    const customerId = this.extractCustomerId(this.tokenService.getUsername());
    if (customerId === null) {
      return throwError(() => new Error('Unable to extract customer ID from username'));
    }
    return this.customerService.getCustomerById(customerId);
  }

  updateCurrentUserProfile(data: Partial<CustomerResponse>): Observable<CustomerResponse> {
    const customerId = this.extractCustomerId(this.tokenService.getUsername());
    if (customerId === null) {
      return throwError(() => new Error('Unable to extract customer ID from username'));
    }
    return this.customerService.updateCustomer(customerId, data);
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
}
