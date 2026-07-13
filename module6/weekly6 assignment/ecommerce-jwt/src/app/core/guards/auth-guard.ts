import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Token } from '../../features/auth/services/token';

export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(Token);
  const router = inject(Router);

  if (tokenService.isAuthenticated()) {
    return true;
  }

  return router.createUrlTree(['/login'], {
    queryParams: { redirectTo: state.url }
  });
};
