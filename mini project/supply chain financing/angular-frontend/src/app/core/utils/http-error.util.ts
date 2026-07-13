import { HttpErrorResponse } from '@angular/common/http';
import { ApiErrorDto } from '../models/dto/auth.dto';

export function toErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    const apiError = error.error as ApiErrorDto | undefined;

    if (apiError?.errors) {
      return Object.values(apiError.errors).join(', ');
    }

    if (apiError?.message) {
      return apiError.message;
    }

    if (typeof error.error === 'string' && error.error) {
      return error.error;
    }

    return `Request failed with status ${error.status}`;
  }

  return 'Unexpected error. Please try again.';
}
