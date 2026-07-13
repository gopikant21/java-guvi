import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { BooksService } from '../../core/services/books.service';
import { AppModalComponent } from '../../shared/components/app-modal/app-modal.component';
import { BooksFilterComponent } from './components/books-filter/books-filter.component';
import type {
  Book,
  BookCreateRequest,
  BookUpdateRequest,
  GroupedCountResponse,
} from '../../shared/models/catalog.models';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [CommonModule, FormsModule, AppModalComponent, BooksFilterComponent],
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css'],
})
export class BooksComponent implements OnInit {
  private readonly booksService = inject(BooksService);

  protected readonly books = signal<Book[]>([]);
  protected readonly loading = signal(false);
  protected readonly error = signal('');
  protected readonly errorDetails = signal<string[]>([]);
  protected readonly showAddForm = signal(false);
  protected readonly showUpdateForm = signal(false);
  protected readonly statsLoading = signal(false);
  protected readonly stats = signal<{
    count: number;
    averagePrice: number;
    maxPrice: number;
    minPrice: number;
    authorCount: GroupedCountResponse;
    publisherCount: GroupedCountResponse;
  } | null>(null);

  protected draft: BookCreateRequest = this.emptyDraft();
  protected updateDraft: BookUpdateRequest = this.emptyDraft();

  protected bookById = 0;
  protected searchTitle = '';
  protected searchAuthor = '';
  protected searchPublisher = '';
  protected singlePrice = 0;
  protected rangeMin = 0;
  protected rangeMax = 0;
  protected updateBookId = 0;

  ngOnInit(): void {
    this.loadBooks();
  }

  protected loadBooks(): void {
    this.runListQuery(
      this.booksService.getAll(),
      'Could not load books. Check backend and CORS settings.',
    );
  }

  protected createBook(): void {
    this.loading.set(true);
    this.clearError();

    this.booksService.create(this.draft).subscribe({
      next: (responseText) => {
        const created = { ...this.draft };
        this.addCreatedBookToList(created, this.extractIdFromText(responseText));
        this.draft = this.emptyDraft();
        this.showAddForm.set(false);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not create book. Validate input data.');
        this.loading.set(false);
      },
    });
  }

  protected deleteBook(id?: number): void {
    if (!id) {
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.booksService.remove(id).subscribe({
      next: () => {
        this.removeBookFromList(id);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not delete book.');
        this.loading.set(false);
      },
    });
  }

  protected getBookById(): void {
    if (!this.bookById) {
      this.error.set('Enter a valid book id.');
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.booksService.getById(this.bookById).subscribe({
      next: (item) => {
        this.books.set([item]);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not fetch book by id.');
        this.loading.set(false);
      },
    });
  }

  protected updateBook(): void {
    if (!this.updateBookId) {
      this.error.set('Enter a valid update book id.');
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.booksService.update(this.updateBookId, this.updateDraft).subscribe({
      next: () => {
        this.replaceBookInList(this.updateBookId, this.updateDraft);
        this.showUpdateForm.set(false);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not update book.');
        this.loading.set(false);
      },
    });
  }

  protected searchBooksByTitle(): void {
    this.runListQuery(this.booksService.searchByTitle(this.searchTitle), 'Title search failed.');
  }

  protected searchBooksByAuthor(): void {
    this.runListQuery(this.booksService.searchByAuthor(this.searchAuthor), 'Author search failed.');
  }

  protected searchBooksByPublisher(): void {
    this.runListQuery(
      this.booksService.searchByPublisher(this.searchPublisher),
      'Publisher search failed.',
    );
  }

  protected priceAbove(): void {
    this.runListQuery(this.booksService.getAbovePrice(this.singlePrice), 'Price filter failed.');
  }

  protected priceBelow(): void {
    this.runListQuery(this.booksService.getBelowPrice(this.singlePrice), 'Price filter failed.');
  }

  protected priceRange(): void {
    if (this.rangeMin > this.rangeMax) {
      this.error.set('Price range is invalid. Min price cannot be greater than max price.');
      this.errorDetails.set([]);
      return;
    }

    this.runListQuery(
      this.booksService.getByPriceRange({ min: this.rangeMin, max: this.rangeMax }),
      'Price range filter failed.',
    );
  }

  protected sortByTitle(): void {
    this.runListQuery(this.booksService.sortByTitle(), 'Sort by title failed.');
  }

  protected sortByAuthor(): void {
    this.runListQuery(this.booksService.sortByAuthor(), 'Sort by author failed.');
  }

  protected sortByPriceAsc(): void {
    this.runListQuery(this.booksService.sortByPriceAsc(), 'Sort by price asc failed.');
  }

  protected sortByPriceDesc(): void {
    this.runListQuery(this.booksService.sortByPriceDesc(), 'Sort by price desc failed.');
  }

  protected applyCombinedFilters(): void {
    const hasRangeFilter = this.rangeMin > 0 || this.rangeMax > 0;
    if (hasRangeFilter && this.rangeMax > 0 && this.rangeMin > this.rangeMax) {
      this.error.set('Price range is invalid. Min price cannot be greater than max price.');
      this.errorDetails.set([]);
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.booksService.getAll().subscribe({
      next: (list) => {
        const filtered = list.filter((item) => this.matchesCombinedFilters(item));
        this.books.set(filtered);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not apply filters.');
        this.loading.set(false);
      },
    });
  }

  protected loadStats(): void {
    this.statsLoading.set(true);
    this.clearError();

    forkJoin({
      count: this.booksService.getTotalCount(),
      averagePrice: this.booksService.getAveragePrice(),
      maxPrice: this.booksService.getMaxPrice(),
      minPrice: this.booksService.getMinPrice(),
      authorCount: this.booksService.getAuthorCount(),
      publisherCount: this.booksService.getPublisherCount(),
    }).subscribe({
      next: (value) => {
        this.stats.set(value);
        this.statsLoading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not load book stats.');
        this.statsLoading.set(false);
      },
    });
  }

  protected openAddForm(): void {
    this.showAddForm.set(true);
  }

  protected closeAddForm(): void {
    this.showAddForm.set(false);
  }

  protected openUpdateForm(): void {
    this.showUpdateForm.set(true);
  }

  protected openUpdateFormForItem(item: Book): void {
    if (!item.id) {
      this.error.set('Book id is required to update this record.');
      return;
    }

    this.updateBookId = item.id;
    this.updateDraft = {
      title: item.title,
      author: item.author,
      publisher: item.publisher,
      price: item.price,
    };
    this.showUpdateForm.set(true);
  }

  protected closeUpdateForm(): void {
    this.showUpdateForm.set(false);
  }

  protected objectEntries(value?: GroupedCountResponse): Array<[string, number]> {
    return Object.entries(value ?? {});
  }

  private runListQuery(request$: Observable<Book[]>, failureMessage: string): void {
    this.loading.set(true);
    this.clearError();

    request$.subscribe({
      next: (list) => {
        this.books.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, failureMessage);
        this.loading.set(false);
      },
    });
  }

  private matchesCombinedFilters(item: Book): boolean {
    const title = this.searchTitle.trim().toLowerCase();
    const author = this.searchAuthor.trim().toLowerCase();
    const publisher = this.searchPublisher.trim().toLowerCase();
    const minPriceFromRange = this.rangeMin > 0 ? this.rangeMin : 0;
    const minPrice = this.singlePrice > 0 ? Math.max(this.singlePrice, minPriceFromRange) : minPriceFromRange;
    const maxPrice = this.rangeMax > 0 ? this.rangeMax : Number.POSITIVE_INFINITY;

    if (this.bookById > 0 && item.id !== this.bookById) {
      return false;
    }

    if (title && !item.title.toLowerCase().includes(title)) {
      return false;
    }

    if (author && !item.author.toLowerCase().includes(author)) {
      return false;
    }

    if (publisher && !item.publisher.toLowerCase().includes(publisher)) {
      return false;
    }

    if (item.price < minPrice || item.price > maxPrice) {
      return false;
    }

    return true;
  }

  private clearError(): void {
    this.error.set('');
    this.errorDetails.set([]);
  }

  private setApiError(error: unknown, fallbackMessage: string): void {
    if (!(error instanceof HttpErrorResponse)) {
      this.error.set(fallbackMessage);
      this.errorDetails.set([]);
      return;
    }

    if (error.status === 0) {
      this.error.set('Unable to reach server. Verify backend is running and CORS is enabled.');
      this.errorDetails.set([]);
      return;
    }

    const apiError = this.extractApiError(error);
    this.error.set(apiError.message || fallbackMessage);
    this.errorDetails.set(apiError.details);
  }

  private extractApiError(error: HttpErrorResponse): { message: string; details: string[] } {
    const payload = error.error;

    if (typeof payload === 'string') {
      return { message: payload, details: [] };
    }

    if (payload && typeof payload === 'object') {
      const rawMessage = payload['message'];
      const rawDetails = payload['details'];
      const message = typeof rawMessage === 'string' && rawMessage.trim() ? rawMessage : '';
      const details = Array.isArray(rawDetails)
        ? rawDetails.filter((item): item is string => typeof item === 'string' && !!item.trim())
        : [];

      if (message || details.length) {
        return { message, details };
      }
    }

    return { message: error.message, details: [] };
  }

  private addCreatedBookToList(payload: BookCreateRequest, idFromServer?: number): void {
    const created: Book = {
      id: idFromServer ?? this.nextLocalId(),
      ...payload,
    };

    this.books.update((list) => [created, ...list]);
  }

  private replaceBookInList(id: number, payload: BookUpdateRequest): void {
    this.books.update((list) =>
      list.map((item) => {
        if (item.id !== id) {
          return item;
        }

        return {
          ...item,
          ...payload,
          id,
        };
      }),
    );
  }

  private removeBookFromList(id: number): void {
    this.books.update((list) => list.filter((item) => item.id !== id));
  }

  private extractIdFromText(text?: string): number | undefined {
    if (!text) {
      return undefined;
    }

    const match = text.match(/\b\d+\b/);
    const parsed = match ? Number(match[0]) : Number.NaN;
    return Number.isFinite(parsed) && parsed > 0 ? parsed : undefined;
  }

  private nextLocalId(): number {
    const ids = this.books()
      .map((item) => item.id ?? 0)
      .filter((id) => Number.isFinite(id));
    const minId = ids.length ? Math.min(...ids) : 0;
    return minId <= 0 ? minId - 1 : -1;
  }

  private emptyDraft(): BookCreateRequest {
    return {
      title: '',
      author: '',
      publisher: '',
      price: 0,
    };
  }
}