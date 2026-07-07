import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TodoList } from './components/todo-list/todo-list';

@Component({
  selector: 'app-root',
  imports: [TodoList],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('serviceDemo');
}
