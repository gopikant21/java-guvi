import { CustomerProfileDto, LoanDto, LoanStatus, RepaymentDto } from './customer.dto';

export interface AdminDashboardDto {
  totalCustomers: number;
  totalLoans: number;
  pendingLoans: number;
  approvedLoans: number;
  rejectedLoans: number;
  disbursedLoans: number;
  closedLoans: number;
  totalAmountDisbursed: number;
}

export interface CustomerDetailsDto {
  customer: CustomerProfileDto;
  loans: Pick<LoanDto, 'id' | 'loanNumber' | 'amount' | 'status' | 'remainingAmount'>[];
}

export interface AdminLoanDetailsDto {
  customer: Pick<CustomerProfileDto, 'id' | 'name' | 'email' | 'phone' | 'role'>;
  loan: LoanDto;
  repayments: RepaymentDto[];
}

export interface RejectLoanRequestDto {
  reason: string;
}

export interface LoanQueryParamsDto {
  status?: LoanStatus;
  customerId?: number;
}
