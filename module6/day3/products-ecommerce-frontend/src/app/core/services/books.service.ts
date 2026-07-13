import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  Book,
  BookCreateRequest,
  BookUpdateRequest,
  GroupedCountResponse,
  PriceRangeQuery,
} from '../../shared/models/catalog.models';
import { API_BASE_URL, API_PATHS } from '../config/api.config';

@Injectable({ providedIn: 'root' })
export class BooksService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${API_BASE_URL}${API_PATHS.books}`;

  getAll(): Observable<Book[]> {
    return this.http.get<Book[]>(this.baseUrl);
  }

  getById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.baseUrl}/${id}`);
  }

  create(payload: BookCreateRequest): Observable<string> {
    return this.http.post(this.baseUrl, payload, {
      responseType: 'text',
    });
  }

  update(id: number, payload: BookUpdateRequest): Observable<string> {
    return this.http.put(`${this.baseUrl}/${id}`, payload, {
      responseType: 'text',
    });
  }

  remove(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${id}`, {
      responseType: 'text',
    });
  }

  searchByTitle(title: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/title/${encodeURIComponent(title)}`);
  }

  searchByAuthor(author: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/author/${encodeURIComponent(author)}`);
  }

  searchByPublisher(publisher: string): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/publisher/${encodeURIComponent(publisher)}`);
  }

  getAbovePrice(price: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/price/above/${price}`);
  }

  getBelowPrice(price: number): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/price/below/${price}`);
  }

  getByPriceRange(query: PriceRangeQuery): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/price-range`, {
      params: new HttpParams().set('min', query.min).set('max', query.max),
    });
  }

  sortByTitle(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/sort/title`);
  }

  sortByAuthor(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/sort/author`);
  }

  sortByPriceAsc(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/sort/price`);
  }

  sortByPriceDesc(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.baseUrl}/sort/price-desc`);
  }

  getTotalCount(): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/stats/count`);
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

  getAuthorCount(): Observable<GroupedCountResponse> {
    return this.http.get<GroupedCountResponse>(`${this.baseUrl}/stats/author-count`);
  }

  getPublisherCount(): Observable<GroupedCountResponse> {
    return this.http.get<GroupedCountResponse>(`${this.baseUrl}/stats/publisher-count`);
  }
}