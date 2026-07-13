import { Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ErrorState {
  readonly message = signal<string | null>(null);

  set(message: string): void {
    this.message.set(message);
  }

  clear(): void {
    this.message.set(null);
  }
}
