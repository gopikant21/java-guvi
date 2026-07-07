import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService, Product } from './product.service';

@Component({
  selector: 'app-root',
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App implements OnInit {
  products: Product[] = [];
  newProduct: Product = {
    name: '',
    price: 0,
    description: '',
    quantity: 0
  };

  isLoading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private productService: ProductService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  // Load all products from backend
  loadProducts(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.productService.getAllProducts().subscribe(
      (data: Product[]) => {
        this.products = data;
        this.isLoading = false;
        this.cdr.markForCheck();
      },
      (error: any) => {
        this.isLoading = false;
        this.errorMessage = 'Failed to load products. Please check if the backend is running on http://localhost:8080';
        console.error('Error loading products:', error);
        this.cdr.markForCheck();
      }
    );
  }

  // Add new product
  addProduct(): void {
    if (!this.newProduct.name || this.newProduct.price <= 0) {
      this.errorMessage = 'Please fill in all required fields (Name and Price must be valid)';
      return;
    }

    this.errorMessage = '';
    this.productService.addProduct(this.newProduct).subscribe(
      (response: Product) => {
        this.successMessage = `Product "${response.name}" added successfully!`;
        this.products.push(response);
        this.resetForm();
        this.cdr.markForCheck();

        // Clear success message after 3 seconds
        setTimeout(() => {
          this.successMessage = '';
          this.cdr.markForCheck();
        }, 3000);
      },
      (error: any) => {
        this.errorMessage = 'Failed to add product. ' + (error.error?.message || '');
        console.error('Error adding product:', error);
        this.cdr.markForCheck();
      }
    );
  }

  // Remove product
  removeProduct(id: number | undefined): void {
    if (!id) {
      this.errorMessage = 'Invalid product ID';
      return;
    }

    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe(
        () => {
          this.products = this.products.filter(p => p.id !== id);
          this.successMessage = 'Product deleted successfully!';
          this.cdr.markForCheck();

          // Clear success message after 3 seconds
          setTimeout(() => {
            this.successMessage = '';
            this.cdr.markForCheck();
          }, 3000);
        },
        (error: any) => {
          this.errorMessage = 'Failed to delete product. ' + (error.error?.message || '');
          console.error('Error deleting product:', error);
          this.cdr.markForCheck();
        }
      );
    }
  }

  // Reset form
  private resetForm(): void {
    this.newProduct = {
      name: '',
      price: 0,
      description: '',
      quantity: 0
    };
  }
}
