// src/app/features/products/pages/product-details/product-details.ts
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../../services/product';
import { ProductResponse } from '../../models/product.models';

@Component({
  selector: 'app-product-details',
  imports: [],
  templateUrl: './product-details.html',
  styleUrl: './product-details.css',
})
export class ProductDetails implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly productService = inject(Product);

  readonly product = signal<ProductResponse | null>(null);
  readonly loading = signal(false);
  readonly errorMessage = signal('');

  ngOnInit(): void {
    const id = Number(this.route.snapshot.queryParamMap.get('id'));
    if (!Number.isFinite(id) || id <= 0) {
      this.errorMessage.set('Invalid product id.');
      return;
    }

    this.loading.set(true);

    this.productService.getProductById(id).subscribe({
      next: (data) => {
        this.product.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load product details.');
        this.loading.set(false);
      },
    });
  }
}