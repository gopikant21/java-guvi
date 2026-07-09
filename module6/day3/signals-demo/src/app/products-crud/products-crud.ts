import { Component, computed, signal, WritableSignal } from '@angular/core';

interface Product {
  id: number;
  name: string;
  price: number;
}

@Component({
  selector: 'app-products-crud',
  templateUrl: './products-crud.html',
  styleUrl: './products-crud.css'
})
export class ProductsCrudComponent {
  readonly products: WritableSignal<Product[]> = signal([
    { id: 1, name: 'Keyboard', price: 1200 },
    { id: 2, name: 'Mouse', price: 700 }
  ]);

  readonly nameInput: WritableSignal<string> = signal('');
  readonly priceInput: WritableSignal<string> = signal('');
  readonly editingId: WritableSignal<number | null> = signal(null);

  readonly totalProducts = computed(() => this.products().length);

  onNameInput(value: string): void {
    this.nameInput.set(value);
  }

  onPriceInput(value: string): void {
    this.priceInput.set(value);
  }

  createProduct(): void {
    const name = this.nameInput().trim();
    const price = Number(this.priceInput());

    if (!name || Number.isNaN(price) || price <= 0) {
      return;
    }

    this.products.update((items) => {
      const nextId = items.length > 0 ? Math.max(...items.map((p) => p.id)) + 1 : 1;
      return [...items, { id: nextId, name, price }];
    });

    this.resetForm();
  }

  startEdit(product: Product): void {
    this.editingId.set(product.id);
    this.nameInput.set(product.name);
    this.priceInput.set(String(product.price));
  }

  updateProduct(): void {
    const id = this.editingId();
    const name = this.nameInput().trim();
    const price = Number(this.priceInput());

    if (id === null || !name || Number.isNaN(price) || price <= 0) {
      return;
    }

    this.products.update((items) =>
      items.map((item) => (item.id === id ? { ...item, name, price } : item))
    );

    this.resetForm();
  }

  deleteProduct(id: number): void {
    this.products.update((items) => items.filter((item) => item.id !== id));

    if (this.editingId() === id) {
      this.resetForm();
    }
  }

  cancelEdit(): void {
    this.resetForm();
  }

  private resetForm(): void {
    this.editingId.set(null);
    this.nameInput.set('');
    this.priceInput.set('');
  }
}
