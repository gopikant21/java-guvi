import { Component, computed, signal, WritableSignal } from '@angular/core';

interface Book {
  id: number;
  title: string;
  author: string;
}

@Component({
  selector: 'app-books-crud',
  templateUrl: './books-crud.html',
  styleUrl: './books-crud.css'
})
export class BooksCrudComponent {
  readonly books: WritableSignal<Book[]> = signal([
    { id: 1, title: 'Atomic Habits', author: 'James Clear' },
    { id: 2, title: 'Clean Code', author: 'Robert C. Martin' }
  ]);

  readonly titleInput: WritableSignal<string> = signal('');
  readonly authorInput: WritableSignal<string> = signal('');
  readonly editingId: WritableSignal<number | null> = signal(null);

  readonly totalBooks = computed(() => this.books().length);

  onTitleInput(value: string): void {
    this.titleInput.set(value);
  }

  onAuthorInput(value: string): void {
    this.authorInput.set(value);
  }

  createBook(): void {
    const title = this.titleInput().trim();
    const author = this.authorInput().trim();

    if (!title || !author) {
      return;
    }

    this.books.update((items) => {
      const nextId = items.length > 0 ? Math.max(...items.map((b) => b.id)) + 1 : 1;
      return [...items, { id: nextId, title, author }];
    });

    this.resetForm();
  }

  startEdit(book: Book): void {
    this.editingId.set(book.id);
    this.titleInput.set(book.title);
    this.authorInput.set(book.author);
  }

  updateBook(): void {
    const id = this.editingId();
    const title = this.titleInput().trim();
    const author = this.authorInput().trim();

    if (id === null || !title || !author) {
      return;
    }

    this.books.update((items) =>
      items.map((item) => (item.id === id ? { ...item, title, author } : item))
    );

    this.resetForm();
  }

  deleteBook(id: number): void {
    this.books.update((items) => items.filter((item) => item.id !== id));

    if (this.editingId() === id) {
      this.resetForm();
    }
  }

  cancelEdit(): void {
    this.resetForm();
  }

  private resetForm(): void {
    this.editingId.set(null);
    this.titleInput.set('');
    this.authorInput.set('');
  }
}
