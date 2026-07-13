import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerResponse } from '../models/customer.models';

@Injectable({ providedIn: 'root' })
export class Customer {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080';

  getAllCustomers(): Observable<CustomerResponse[]> {
    return this.http.get<CustomerResponse[]>(`${this.baseUrl}/api/customers`);
  }
}