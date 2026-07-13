import { HttpInterceptorFn } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { ErrorState } from '../services/error-state';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const errorState = inject(ErrorState);
  const skipGlobalError = req.headers.has('x-skip-global-error');
  const requestToSend = skipGlobalError
    ? req.clone({ headers: req.headers.delete('x-skip-global-error') })
    : req;

  return next(requestToSend).pipe(
    catchError((error: unknown) => {
      if (!skipGlobalError && error instanceof HttpErrorResponse) {
        if (error.status === 0) {
          errorState.set('Cannot reach server. Please try again later.');
        } else if (error.status === 503) {
          errorState.set('Service is temporarily unavailable. Please retry in a moment.');
        } else if (error.status === 402) {
          errorState.set(getErrorMessage(error, 'Payment failed. Please verify payment details.'));
        } else if (error.status === 409) {
          errorState.set(getErrorMessage(error, 'Operation could not be completed due to a data conflict.'));
        } else if (error.status >= 500) {
          errorState.set(getErrorMessage(error, 'Server error occurred. Please try again.'));
        } else if (error.status !== 401 && error.status !== 403) {
          errorState.set(getErrorMessage(error));
        }
      }

      return throwError(() => error);
    })
  );
};

interface ApiErrorBody {
  error?: string;
  message?: string;
  field?: string;
}

function getErrorMessage(error: HttpErrorResponse, fallback?: string): string {
  const body = error.error as ApiErrorBody | string | null;

  if (typeof body === 'string' && body.trim().length > 0) {
    try {
      const parsed = JSON.parse(body) as ApiErrorBody;
      if (typeof parsed.message === 'string' && parsed.message.trim().length > 0) {
        return parsed.message;
      }
      if (typeof parsed.error === 'string' && parsed.error.trim().length > 0) {
        return parsed.error;
      }
    } catch {
      return body;
    }

    return body;
  }

  if (body && typeof body === 'object') {
    if (typeof body.message === 'string' && body.message.trim().length > 0) {
      if (body.field && !body.message.includes(body.field)) {
        return `${body.message} (Field: ${body.field})`;
      }
      return body.message;
    }

    if (typeof body.error === 'string' && body.error.trim().length > 0) {
      return body.error;
    }
  }

  return fallback ?? `Request failed with status ${error.status}.`;
}
