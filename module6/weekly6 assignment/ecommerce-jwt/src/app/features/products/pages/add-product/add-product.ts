// src/app/features/products/pages/add-product/add-product.ts
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Product } from '../../services/product';
import { ProductCategory } from '../../models/product.models';

@Component({
  selector: 'app-add-product',
  imports: [ReactiveFormsModule],
  templateUrl: './add-product.html',
  styleUrl: './add-product.css',
})
export class AddProduct {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(Product);
  private readonly router = inject(Router);

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

  readonly submitting = signal(false);
  readonly errorMessage = signal('');

  readonly form = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    description: ['', [Validators.required]],
    brand: ['', [Validators.required]],
    category: ['OTHER' as ProductCategory, [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
    stocks: [0, [Validators.required, Validators.min(0)]],
  });

  onSubmit(): void {
    if (this.form.invalid || this.submitting()) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting.set(true);
    this.errorMessage.set('');

    this.productService.createProduct(this.form.getRawValue()).subscribe({
      next: () => {
        this.submitting.set(false);
        void this.router.navigate(['/admin/products']);
      },
      error: () => {
        this.submitting.set(false);
        this.errorMessage.set('Failed to create product.');
      },
    });
  }
}