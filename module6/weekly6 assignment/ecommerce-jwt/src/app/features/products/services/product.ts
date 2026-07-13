import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductRequest, ProductResponse } from '../models/product.models';

@Injectable({ providedIn: 'root' })
export class Product {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = 'http://localhost:8080';

  getAllProducts(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(`${this.baseUrl}/api/products`);
  }

  getProductById(id: number): Observable<ProductResponse> {
    return this.http.get<ProductResponse>(`${this.baseUrl}/api/products/${id}`);
  }

  createProduct(payload: ProductRequest): Observable<ProductResponse> {
    return this.http.post<ProductResponse>(`${this.baseUrl}/api/products`, payload);
  }

  updateProduct(id: number, payload: ProductRequest): Observable<ProductResponse> {
    return this.http.put<ProductResponse>(`${this.baseUrl}/api/products/${id}`, payload);
  }

  deleteProduct(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/api/products/${id}`, {
      responseType: 'text',
    });
  }

  getLowStockProducts(threshold = 10): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(
      `${this.baseUrl}/api/products/low-stock/${threshold}`
    );
  }
}