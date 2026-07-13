import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ErrorState } from './core/services/error-state';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly errorState = inject(ErrorState);

  dismissError(): void {
    this.errorState.clear();
  }
}
