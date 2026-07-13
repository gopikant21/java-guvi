import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  AdminDashboardDto,
  AdminLoanDetailsDto,
  CustomerDetailsDto,
  LoanQueryParamsDto,
  RejectLoanRequestDto
} from '../models/dto/admin.dto';
import { CustomerProfileDto, LoanDto, RepaymentDto } from '../models/dto/customer.dto';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = 'http://localhost:8080/api/admin';

  getDashboard(): Observable<AdminDashboardDto> {
    return this.http.get<AdminDashboardDto>(`${this.apiBaseUrl}/dashboard`);
  }

  getCustomers(): Observable<CustomerProfileDto[]> {
    return this.http.get<CustomerProfileDto[]>(`${this.apiBaseUrl}/customers`);
  }

  getCustomerDetails(customerId: number): Observable<CustomerDetailsDto> {
    return this.http.get<CustomerDetailsDto>(`${this.apiBaseUrl}/customers/${customerId}`);
  }

  getLoans(params?: LoanQueryParamsDto): Observable<LoanDto[]> {
    let httpParams = new HttpParams();

    if (params?.status) {
      httpParams = httpParams.set('status', params.status);
    }

    if (params?.customerId) {
      httpParams = httpParams.set('customerId', params.customerId);
    }

    return this.http.get<LoanDto[]>(`${this.apiBaseUrl}/loans`, { params: httpParams });
  }

  getLoanDetails(loanId: number): Observable<AdminLoanDetailsDto> {
    return this.http.get<AdminLoanDetailsDto>(`${this.apiBaseUrl}/loans/${loanId}`);
  }

  approveLoan(loanId: number): Observable<LoanDto> {
    return this.http.put<LoanDto>(`${this.apiBaseUrl}/loans/${loanId}/approve`, {});
  }

  rejectLoan(loanId: number, payload: RejectLoanRequestDto): Observable<LoanDto> {
    return this.http.put<LoanDto>(`${this.apiBaseUrl}/loans/${loanId}/reject`, payload);
  }

  disburseLoan(loanId: number): Observable<LoanDto> {
    return this.http.put<LoanDto>(`${this.apiBaseUrl}/loans/${loanId}/disburse`, {});
  }

  getLoanRepayments(loanId: number): Observable<RepaymentDto[]> {
    return this.http.get<RepaymentDto[]>(`${this.apiBaseUrl}/loans/${loanId}/repayments`);
  }
}
