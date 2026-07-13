import { Role } from './auth.dto';

export type LoanStatus =
  | 'PENDING'
  | 'APPROVED'
  | 'REJECTED'
  | 'DISBURSED'
  | 'PARTIALLY_PAID'
  | 'FULLY_PAID'
  | 'CLOSED';

export type PaymentMode = 'UPI' | 'NEFT' | 'IMPS' | 'RTGS' | 'CASH' | 'CARD';

export interface CustomerProfileDto {
  id: number;
  name: string;
  email: string;
  phone: string;
  role: Role;
  createdAt: string;
}

export interface UpdateCustomerProfileRequestDto {
  name: string;
  phone: string;
}

export interface CreateLoanRequestDto {
  amount: number;
  interestRate: number;
  tenureMonths: number;
  purpose: string;
}

export interface LoanDto {
  id: number;
  loanNumber: string;
  customerId?: number;
  customerName?: string;
  amount: number;
  interestRate: number;
  tenureMonths?: number;
  purpose?: string;
  status: LoanStatus;
  approvedById?: number | null;
  approvedDate?: string | null;
  disbursedDate?: string | null;
  remainingAmount: number;
  rejectionReason?: string | null;
  createdAt: string;
}

export interface RepaymentDto {
  id: number;
  loanId: number;
  amount: number;
  paymentDate: string;
  paymentMode: PaymentMode;
  remarks: string;
}

export interface RepayLoanRequestDto {
  amount: number;
  paymentMode: PaymentMode;
  remarks: string;
}
