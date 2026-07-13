// src/app/features/products/pages/edit-product/edit-product.ts
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize } from 'rxjs';
import { Product } from '../../services/product';
import { ProductCategory, ProductRequest } from '../../models/product.models';

@Component({
  selector: 'app-edit-product',
  imports: [ReactiveFormsModule],
  templateUrl: './edit-product.html',
  styleUrl: './edit-product.css',
})
export class EditProduct implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly productService = inject(Product);

  readonly categories: ProductCategory[] = [
    'ELECTRONICS',
    'CLOTHING',
    'BOOKS',
    'HOME_AND_KITCHEN',
    'SPORTS_AND_OUTDOORS',
    'TOYS_AND_GAMES',
    'BEAUTY_AND_PERSONAL_CARE',
    'GROCERIES',
    'OTHER',
  ];

  readonly loading = signal(false);
  readonly submitting = signal(false);
  readonly errorMessage = signal('');
  readonly productId = signal<number | null>(null);
  readonly productLoaded = signal(false);

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
    brand: ['', [Validators.required]],
    category: ['OTHER' as ProductCategory, [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
    stocks: [0, [Validators.required, Validators.min(0)]],
  });

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!Number.isFinite(id) || id <= 0) {
      this.errorMessage.set('Invalid product id.');
      this.productLoaded.set(false);
      return;
    }

    this.productId.set(id);
    this.productLoaded.set(false);
    this.errorMessage.set('');
    this.loading.set(true);

    this.productService
      .getProductById(id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
      next: (p) => {
        if (!p) {
          this.errorMessage.set('Product data is empty.');
          this.productLoaded.set(false);
          return;
        }

        const payload: ProductRequest = {
          name: p.name ?? '',
          description: p.description ?? '',
          brand: p.brand ?? '',
          category: p.category,
          price: p.price ?? 0,
          stocks: p.stocks ?? 0,
        };
        this.form.reset(payload);
        this.productLoaded.set(true);
      },
      error: () => {
        this.errorMessage.set('Failed to load product.');
        this.productLoaded.set(false);
      },
    });
  }

  onSubmit(): void {
    const id = this.productId();
    if (!id || this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set('');

    this.productService.updateProduct(id, this.form.getRawValue()).subscribe({
      next: () => {
        this.submitting.set(false);
        void this.router.navigate(['/admin/products']);
      },
      error: () => {
        this.submitting.set(false);
        this.errorMessage.set('Failed to update product.');
      },
    });
  }
}