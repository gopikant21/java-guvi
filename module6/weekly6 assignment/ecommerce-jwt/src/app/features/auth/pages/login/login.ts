import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs';
import { Auth } from '../../services/auth';
import { ErrorState } from '../../../../core/services/error-state';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(Auth);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly errorState = inject(ErrorState);

  readonly form = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]]
  });

  readonly isSubmitting = signal(false);
  readonly errorMessage = signal('');

  constructor() {
    const reason = this.route.snapshot.queryParamMap.get('reason');
    if (reason === 'session_expired') {
      this.errorMessage.set('Your session expired. Please sign in again.');
    }
  }

  onSubmit(): void {
    if (this.form.invalid || this.isSubmitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.errorMessage.set('');
    this.errorState.clear();
    this.isSubmitting.set(true);

    this.authService
      .login(this.form.getRawValue())
      .pipe(finalize(() => this.isSubmitting.set(false)))
      .subscribe({
        next: (session) => {
          this.errorState.clear();
          const requestedRedirect = this.route.snapshot.queryParamMap.get('redirectTo');
          const redirectUrl = requestedRedirect || session.redirectUrl;
          void this.router.navigateByUrl(redirectUrl);
        },
        error: (error: unknown) => {
          this.errorMessage.set(
            error instanceof Error ? error.message : 'Login failed. Please try again.'
          );
        }
      });
  }
}
