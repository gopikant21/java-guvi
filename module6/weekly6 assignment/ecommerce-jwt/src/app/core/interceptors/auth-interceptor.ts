import { HttpInterceptorFn } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { Token } from '../../features/auth/services/token';
import { ErrorState } from '../services/error-state';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(Token);
  const router = inject(Router);
  const errorState = inject(ErrorState);

  const token = tokenService.getToken();
  const authReq = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(authReq).pipe(
    catchError((error: unknown) => {
      if (error instanceof HttpErrorResponse) {
        const isLoginCall = req.url.includes('/auth/login');

        if (error.status === 401 && !isLoginCall) {
          tokenService.clearToken();
          errorState.set('Your session has expired. Please login again.');
          void router.navigate(['/login'], {
            queryParams: { reason: 'session_expired' }
          });
        }

        if (error.status === 403) {
          errorState.set('You do not have permission to access this resource.');
          if (tokenService.hasRole('ROLE_ADMIN')) {
            void router.navigate(['/dashboard/admin']);
          } else if (tokenService.isAuthenticated()) {
            void router.navigate(['/dashboard/customer']);
          } else {
            void router.navigate(['/login']);
          }
        }
      }

      return throwError(() => error);
    })
  );
};
