// src/app/features/products/pages/inventory/inventory.ts
import { Component, OnInit, inject, signal } from '@angular/core';
import { Product } from '../../services/product';
import { ProductResponse } from '../../models/product.models';

@Component({
  selector: 'app-inventory',
  imports: [],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css',
})
export class Inventory implements OnInit {
  private readonly productService = inject(Product);

  readonly threshold = signal(10);
  readonly products = signal<ProductResponse[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal('');

  ngOnInit(): void {
    this.fetchLowStock();
  }

  fetchLowStock(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.productService.getLowStockProducts(this.threshold()).subscribe({
      next: (data) => {
        this.products.set(data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load low stock products.');
        this.loading.set(false);
      },
    });
  }

  setThreshold(value: string): void {
    const parsed = Number(value);
    if (Number.isFinite(parsed) && parsed >= 0) {
      this.threshold.set(parsed);
    }
  }
}