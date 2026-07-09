import { Component, signal, WritableSignal } from '@angular/core';
import { ProductsCrudComponent } from './products-crud/products-crud';
import { BooksCrudComponent } from './books-crud/books-crud';
import { MoviesCrudComponent } from './movies-crud/movies-crud';

@Component({
  selector: 'app-root',
  imports: [ProductsCrudComponent, BooksCrudComponent, MoviesCrudComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  readonly activeCrud: WritableSignal<'products' | 'books' | 'movies'> = signal('products');

  readonly counter: WritableSignal<number> = signal(0);
  readonly name: WritableSignal<string> = signal('Gopi');
  readonly fruits: WritableSignal<string[]> = signal(['Apple', 'Banana']);
  readonly user: WritableSignal<{ id: number; name: string; age: number }> = signal({
    id: 1,
    name: 'Gopi',
    age: 22
  });

  readonly logs: WritableSignal<string[]> = signal([]);

  setActiveCrud(view: 'products' | 'books' | 'movies'): void {
    this.activeCrud.set(view);
  }

  private addLog(message: string): void {
    this.logs.update((items) => [message, ...items].slice(0, 10));
  }

  readCounter(): void {
    this.addLog(`counter(): ${this.counter()}`);
  }

  setCounterToTen(): void {
    this.counter.set(10);
  }

  incrementCounter(): void {
    this.counter.update((value) => value + 1);
  }

  decrementCounter(): void {
    this.counter.update((value) => value - 1);
  }

  readName(): void {
    this.addLog(`name(): ${this.name()}`);
  }

  setNameToRahul(): void {
    this.name.set('Rahul');
  }

  uppercaseName(): void {
    this.name.update((value) => value.toUpperCase());
  }

  readFruits(): void {
    this.addLog(`fruits(): ${JSON.stringify(this.fruits())}`);
  }

  replaceFruits(): void {
    this.fruits.set(['Orange', 'Mango']);
  }

  addGrapes(): void {
    this.fruits.update((arr) => [...arr, 'Grapes']);
  }

  removeBanana(): void {
    this.fruits.update((arr) => arr.filter((item) => item !== 'Banana'));
  }

  replaceAppleWithKiwi(): void {
    this.fruits.update((arr) => arr.map((item) => (item === 'Apple' ? 'Kiwi' : item)));
  }

  readUser(): void {
    this.addLog(`user(): ${JSON.stringify(this.user())}`);
  }

  replaceUser(): void {
    this.user.set({
      id: 2,
      name: 'Rahul',
      age: 25
    });
  }

  incrementAge(): void {
    this.user.update((user) => ({
      ...user,
      age: user.age + 1
    }));
  }

  updateUserNameAndAge(): void {
    this.user.update((user) => ({
      ...user,
      name: 'Amit',
      age: 30
    }));
  }
}
