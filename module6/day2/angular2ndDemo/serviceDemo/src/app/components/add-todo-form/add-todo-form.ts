import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import TodoDTO from '../../dto/TodoDTO';
import { TodoService } from '../../services/todo-service';

@Component({
  selector: 'app-add-todo-form',
  imports: [FormsModule],
  templateUrl: './add-todo-form.html',
  styleUrl: './add-todo-form.css',
})
export class AddTodoForm {
  private todoService = inject(TodoService);

  newTodo: TodoDTO = { id: 0, title: '', description: '', completed: false };

  addTodo() {
    if (this.newTodo.title.trim() && this.newTodo.description.trim()) {
      this.todoService.addTodo(this.newTodo);
      this.newTodo = { id: 0, title: '', description: '', completed: false };
    }
  }
}
