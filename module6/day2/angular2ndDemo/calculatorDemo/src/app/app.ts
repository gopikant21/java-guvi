import { Component, signal } from '@angular/core';
import { ShowCount } from "./components/show-count/show-count";

@Component({
  selector: 'app-root',
  imports: [ShowCount],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('calculatorDemo');
}
