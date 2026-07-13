import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  DiscountedProductsQuery,
  GroupedCountResponse,
  PriceRangeQuery,
  Product,
  ProductCreateRequest,
  ProductUpdateRequest,
  QuantityActionQuery,
  TopRatedQuery,
} from '../../shared/models/catalog.models';
import { API_BASE_URL, API_PATHS } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class ProductsService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${API_BASE_URL}${API_PATHS.products}`;

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.baseUrl);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  create(payload: ProductCreateRequest): Observable<string> {
    return this.http.post(this.baseUrl, payload, {
      responseType: 'text',
    });
  }

  update(id: number, payload: ProductUpdateRequest): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}`, payload, {
      responseType: 'text',
    });
  }

  remove(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, {
      responseType: 'text',
    });
  }

  searchByName(name: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/search`, {
      params: new HttpParams().set('name', name),
    });
  }

  searchByKeyword(keyword: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/search/keyword`, {
      params: new HttpParams().set('keyword', keyword),
    });
  }

  getByCategory(category: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/category/${encodeURIComponent(category)}`);
  }

  getByBrand(brand: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/brand/${encodeURIComponent(brand)}`);
  }

  getAbovePrice(price: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/price/above/${price}`);
  }

  getBelowPrice(price: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/price/below/${price}`);
  }

  getByPriceRange(query: PriceRangeQuery): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/price/range`, {
      params: new HttpParams().set('min', query.min).set('max', query.max),
    });
  }

  getInStock(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/stock/in`);
  }

  getOutOfStock(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/stock/out`);
  }

  buy(id: number, query: QuantityActionQuery): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}/buy`, null, {
      params: new HttpParams().set('quantity', query.quantity),
      responseType: 'text',
    });
  }

  restock(id: number, query: QuantityActionQuery): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}/restock`, null, {
      params: new HttpParams().set('quantity', query.quantity),
      responseType: 'text',
    });
  }

  sortByPriceAsc(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/sort/price/asc`);
  }

  sortByPriceDesc(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/sort/price/desc`);
  }

  sortByRatingDesc(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/sort/rating`);
  }

  sortByName(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/sort/name`);
  }

  getTopRated(query: TopRatedQuery): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/top-rated`, {
      params: new HttpParams().set('limit', query.limit),
    });
  }

  getDiscountedByMinRating(query: DiscountedProductsQuery): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/discounted`, {
      params: new HttpParams().set('minRating', query.minRating),
    });
  }

  getAveragePrice(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/average-price`);
  }

  getMaxPrice(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/max-price`);
  }

  getMinPrice(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/min-price`);
  }

  getTotalCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/count`);
  }

  getInventoryValue(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/inventory-value`);
  }

  getCategoryCount(): Observable<GroupedCountResponse> {
    return this.http.get<GroupedCountResponse>(`${this.baseUrl}/stats/category-count`);
  }

  getBrandCount(): Observable<GroupedCountResponse> {
    return this.http.get<GroupedCountResponse>(`${this.baseUrl}/stats/brand-count`);
  }
}