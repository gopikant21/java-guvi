import { Component, signal } from '@angular/core';
import { People } from './people/people';

@Component({
  selector: 'app-root',
  imports: [People],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('project2');
}
