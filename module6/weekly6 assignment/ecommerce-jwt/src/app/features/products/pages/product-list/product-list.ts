// src/app/features/products/pages/product-list/product-list.ts
import { Component, OnInit, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../../services/product';
import { ProductResponse } from '../../models/product.models';
import { ProductTable } from '../../components/product-table/product-table';
import { Token } from '../../../auth/services/token';

@Component({
  selector: 'app-product-list',
  imports: [ProductTable],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductList implements OnInit {
  private readonly productService = inject(Product);
  private readonly router = inject(Router);
  private readonly tokenService = inject(Token);

  readonly products = signal<ProductResponse[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly isAdmin = signal(false);

  ngOnInit(): void {
    this.isAdmin.set(this.tokenService.hasRole('ROLE_ADMIN'));
    this.fetchProducts();
  }

  fetchProducts(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products.set(data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load products.');
        this.loading.set(false);
      },
    });
  }

  onView(id: number): void {
    void this.router.navigate(['/products/details'], { queryParams: { id } });
  }

  onEdit(id: number): void {
    void this.router.navigate(['/admin/products/edit', id]);
  }

  onDelete(id: number): void {
    if (!confirm('Delete this product?')) {
      return;
    }

    this.productService.deleteProduct(id).subscribe({
      next: () => this.fetchProducts(),
      error: () => this.errorMessage.set('Failed to delete product.'),
    });
  }

  goToAdd(): void {
    void this.router.navigate(['/admin/products/add']);
  }

  goToInventory(): void {
    void this.router.navigate(['/admin/products/inventory']);
  }
}