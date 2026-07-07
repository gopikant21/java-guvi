import { Component, inject } from '@angular/core';
import { TodoService } from '../../services/todo-service';
import { TodoItem } from '../todo-item/todo-item';
import { AddTodoForm } from '../add-todo-form/add-todo-form';

@Component({
  selector: 'app-todo-list',
  imports: [TodoItem, AddTodoForm],
  templateUrl: './todo-list.html',
  styleUrls: ['./todo-list.css'],
})
export class TodoList {
  todoService = inject(TodoService);

  get todos() {
    return this.todoService.getTodos();
  }
}
