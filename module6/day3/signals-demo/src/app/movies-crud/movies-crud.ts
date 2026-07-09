import { Component, computed, signal, WritableSignal } from '@angular/core';

interface Movie {
  id: number;
  title: string;
  year: number;
}

@Component({
  selector: 'app-movies-crud',
  templateUrl: './movies-crud.html',
  styleUrl: './movies-crud.css'
})
export class MoviesCrudComponent {
  readonly movies: WritableSignal<Movie[]> = signal([
    { id: 1, title: 'Inception', year: 2010 },
    { id: 2, title: 'Interstellar', year: 2014 }
  ]);

  readonly titleInput: WritableSignal<string> = signal('');
  readonly yearInput: WritableSignal<string> = signal('');
  readonly editingId: WritableSignal<number | null> = signal(null);

  readonly totalMovies = computed(() => this.movies().length);

  onTitleInput(value: string): void {
    this.titleInput.set(value);
  }

  onYearInput(value: string): void {
    this.yearInput.set(value);
  }

  createMovie(): void {
    const title = this.titleInput().trim();
    const year = Number(this.yearInput());

    if (!title || Number.isNaN(year) || year <= 0) {
      return;
    }

    this.movies.update((items) => {
      const nextId = items.length > 0 ? Math.max(...items.map((m) => m.id)) + 1 : 1;
      return [...items, { id: nextId, title, year }];
    });

    this.resetForm();
  }

  startEdit(movie: Movie): void {
    this.editingId.set(movie.id);
    this.titleInput.set(movie.title);
    this.yearInput.set(String(movie.year));
  }

  updateMovie(): void {
    const id = this.editingId();
    const title = this.titleInput().trim();
    const year = Number(this.yearInput());

    if (id === null || !title || Number.isNaN(year) || year <= 0) {
      return;
    }

    this.movies.update((items) =>
      items.map((item) => (item.id === id ? { ...item, title, year } : item))
    );

    this.resetForm();
  }

  deleteMovie(id: number): void {
    this.movies.update((items) => items.filter((item) => item.id !== id));

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
    this.yearInput.set('');
  }
}
