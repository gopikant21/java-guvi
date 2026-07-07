import { Injectable } from '@angular/core';
import TodoDTO from '../dto/TodoDTO';

@Injectable({
  providedIn: 'root'
})
export class TodoService {
    private todos: TodoDTO[] = [
        { id: 1, title: 'Buy groceries', description: 'Milk, Bread, Eggs', completed: false },
        { id: 2, title: 'Clean the house', description: 'Living room and kitchen', completed: true },
        { id: 3, title: 'Finish project', description: 'Complete the Angular project', completed: false }
    ];

    getTodos(): TodoDTO[] {
        return this.todos;
    }

    addTodo(todo: TodoDTO): void {
        const newId = Math.max(...this.todos.map(t => t.id), 0) + 1;
        this.todos.push({ ...todo, id: newId });
    }

    updateTodo(updatedTodo: TodoDTO): void {
        const index = this.todos.findIndex(todo => todo.id === updatedTodo.id);
        if (index !== -1) {
            this.todos[index] = updatedTodo;
        }
    }

    deleteTodo(id: number): void {
        this.todos = this.todos.filter(todo => todo.id !== id);
    }
}