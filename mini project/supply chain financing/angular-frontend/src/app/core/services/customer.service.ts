import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CreateLoanRequestDto,
  CustomerProfileDto,
  LoanDto,
  RepaymentDto,
  RepayLoanRequestDto,
  UpdateCustomerProfileRequestDto
} from '../models/dto/customer.dto';

@Injectable({ providedIn: 'root' })
export class CustomerService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = 'http://localhost:8080/api/customer';

  getProfile(): Observable<CustomerProfileDto> {
    return this.http.get<CustomerProfileDto>(`${this.apiBaseUrl}/profile`);
  }

  updateProfile(payload: UpdateCustomerProfileRequestDto): Observable<CustomerProfileDto> {
    return this.http.put<CustomerProfileDto>(`${this.apiBaseUrl}/profile`, payload);
  }

  applyLoan(payload: CreateLoanRequestDto): Observable<LoanDto> {
    return this.http.post<LoanDto>(`${this.apiBaseUrl}/loans`, payload);
  }

  getMyLoans(): Observable<LoanDto[]> {
    return this.http.get<LoanDto[]>(`${this.apiBaseUrl}/loans`);
  }

  repayLoan(loanId: number, payload: RepayLoanRequestDto): Observable<LoanDto> {
    return this.http.post<LoanDto>(`${this.apiBaseUrl}/loans/${loanId}/repay`, payload);
  }

  getRepaymentHistory(loanId: number): Observable<RepaymentDto[]> {
    return this.http.get<RepaymentDto[]>(`${this.apiBaseUrl}/loans/${loanId}/repayments`);
  }
}
