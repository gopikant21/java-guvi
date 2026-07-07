import { Component, Input, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import TodoDTO from '../../dto/TodoDTO';
import { TodoService } from '../../services/todo-service';

@Component({
  selector: 'app-todo-item',
  imports: [FormsModule],
  templateUrl: './todo-item.html',
  styleUrls: ['./todo-item.css'],
})
export class TodoItem {
  @Input() todo!: TodoDTO;
  
  private todoService = inject(TodoService);

  isEditMode = false;
  editData: TodoDTO = { id: 0, title: '', description: '', completed: false };

  startEdit() {
    this.isEditMode = true;
    this.editData = { ...this.todo };
  }

  saveEdit() {
    if (this.editData.title.trim()) {
      this.todoService.updateTodo(this.editData);
      this.isEditMode = false;
    }
  }

  cancelEdit() {
    this.isEditMode = false;
  }

  deleteTodo() {
    if (confirm('Are you sure you want to delete this todo?')) {
      this.todoService.deleteTodo(this.todo.id);
    }
  }

  toggleStatus() {
    const updated = { ...this.todo, completed: !this.todo.completed };
    this.todoService.updateTodo(updated);
  }
}
