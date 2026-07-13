import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { ProductsService } from '../../core/services/products.service';
import { AppModalComponent } from '../../shared/components/app-modal/app-modal.component';
import { ProductsFilterComponent } from './components/products-filter/products-filter.component';
import type {
  GroupedCountResponse,
  Product,
  ProductCreateRequest,
  ProductUpdateRequest,
} from '../../shared/models/catalog.models';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule, AppModalComponent, ProductsFilterComponent],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {
  private readonly productsService = inject(ProductsService);
  private readonly defaultCategoryOptions = [
    'Electronics',
    'Fashion',
    'Home',
    'Kitchen',
    'Beauty',
    'Sports',
    'Books',
    'Grocery',
  ];
  private readonly defaultBrandOptions = [
    'Apple',
    'Samsung',
    'Sony',
    'Lenovo',
    'Nike',
    'Adidas',
    'Puma',
    'LG',
  ];

  protected readonly products = signal<Product[]>([]);
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
    inventoryValue: number;
    categoryCount: GroupedCountResponse;
    brandCount: GroupedCountResponse;
  } | null>(null);

  protected draft: ProductCreateRequest = this.emptyDraft();
  protected updateDraft: ProductUpdateRequest = this.emptyDraft();

  protected productById = 0;
  protected searchName = '';
  protected searchKeyword = '';
  protected searchCategory = '';
  protected searchBrand = '';
  protected singlePrice = 0;
  protected rangeMin = 0;
  protected rangeMax = 0;
  protected topRatedLimit = 5;
  protected discountedMinRating = 4;
  protected actionProductId = 0;
  protected actionQuantity = 1;
  protected updateProductId = 0;

  ngOnInit(): void {
    this.loadProducts();
  }

  protected loadProducts(): void {
    this.runListQuery(
      this.productsService.getAll(),
      'Could not load products. Check backend and CORS settings.',
    );
  }

  protected createProduct(): void {
    this.loading.set(true);
    this.clearError();

    this.productsService.create(this.draft).subscribe({
      next: (responseText) => {
        const created = { ...this.draft };
        this.addCreatedProductToList(created, this.extractIdFromText(responseText));
        this.draft = this.emptyDraft();
        this.showAddForm.set(false);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not create product. Validate input data.');
        this.loading.set(false);
      },
    });
  }

  protected deleteProduct(id?: number): void {
    if (!id) {
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.productsService.remove(id).subscribe({
      next: () => {
        this.removeProductFromList(id);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not delete product.');
        this.loading.set(false);
      },
    });
  }

  protected getProductById(): void {
    if (!this.productById) {
      this.error.set('Enter a valid product id.');
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.productsService.getById(this.productById).subscribe({
      next: (item) => {
        this.products.set([item]);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not fetch product by id.');
        this.loading.set(false);
      },
    });
  }

  protected updateProduct(): void {
    if (!this.updateProductId) {
      this.error.set('Enter a valid update product id.');
      return;
    }

    this.loading.set(true);
    this.clearError();

    this.productsService.update(this.updateProductId, this.updateDraft).subscribe({
      next: () => {
        this.replaceProductInList(this.updateProductId, this.updateDraft);
        this.showUpdateForm.set(false);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not update product.');
        this.loading.set(false);
      },
    });
  }

  protected searchProductsByName(): void {
    this.runListQuery(this.productsService.searchByName(this.searchName), 'Name search failed.');
  }

  protected searchProductsByKeyword(): void {
    this.runListQuery(
      this.productsService.searchByKeyword(this.searchKeyword),
      'Keyword search failed.',
    );
  }

  protected filterProductsByCategory(): void {
    this.runListQuery(
      this.productsService.getByCategory(this.searchCategory),
      'Category filter failed.',
    );
  }

  protected filterProductsByBrand(): void {
    this.runListQuery(this.productsService.getByBrand(this.searchBrand), 'Brand filter failed.');
  }

  protected priceAbove(): void {
    this.runListQuery(this.productsService.getAbovePrice(this.singlePrice), 'Price filter failed.');
  }

  protected priceBelow(): void {
    this.runListQuery(this.productsService.getBelowPrice(this.singlePrice), 'Price filter failed.');
  }

  protected priceRange(): void {
    if (this.rangeMin > this.rangeMax) {
      this.error.set('Price range is invalid. Min price cannot be greater than max price.');
      this.errorDetails.set([]);
      return;
    }

    this.runListQuery(
      this.productsService.getByPriceRange({ min: this.rangeMin, max: this.rangeMax }),
      'Price range filter failed.',
    );
  }

  protected inStock(): void {
    this.runListQuery(this.productsService.getInStock(), 'In-stock filter failed.');
  }

  protected outOfStock(): void {
    this.runListQuery(this.productsService.getOutOfStock(), 'Out-of-stock filter failed.');
  }

  protected sortByName(): void {
    this.runListQuery(this.productsService.sortByName(), 'Sort by name failed.');
  }

  protected sortByPriceAsc(): void {
    this.runListQuery(this.productsService.sortByPriceAsc(), 'Sort by price asc failed.');
  }

  protected sortByPriceDesc(): void {
    this.runListQuery(this.productsService.sortByPriceDesc(), 'Sort by price desc failed.');
  }

  protected sortByRatingDesc(): void {
    this.runListQuery(this.productsService.sortByRatingDesc(), 'Sort by rating failed.');
  }

  protected topRated(): void {
    this.runListQuery(
      this.productsService.getTopRated({ limit: this.topRatedLimit }),
      'Top-rated query failed.',
    );
  }

  protected discountedByMinRating(): void {
    this.runListQuery(
      this.productsService.getDiscountedByMinRating({ minRating: this.discountedMinRating }),
      'Discounted query failed.',
    );
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

    this.productsService.getAll().subscribe({
      next: (list) => {
        const filtered = list.filter((item) => this.matchesCombinedFilters(item));
        this.products.set(filtered);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not apply filters.');
        this.loading.set(false);
      },
    });
  }

  protected buyNow(): void {
    this.runStockAction('buy');
  }

  protected restockNow(): void {
    this.runStockAction('restock');
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

  protected openUpdateFormForItem(item: Product): void {
    if (!item.id) {
      this.error.set('Product id is required to update this record.');
      return;
    }

    this.updateProductId = item.id;
    this.updateDraft = {
      name: item.name,
      description: item.description ?? '',
      price: item.price,
      category: item.category,
      brand: item.brand,
      stockQuantity: item.stockQuantity,
      rating: item.rating,
      discountPercentage: item.discountPercentage,
      taxPercentage: item.taxPercentage,
      reviewCount: item.reviewCount,
      active: item.active,
      sku: item.sku,
    };
    this.showUpdateForm.set(true);
  }

  protected closeUpdateForm(): void {
    this.showUpdateForm.set(false);
  }

  protected loadStats(): void {
    this.statsLoading.set(true);
    this.clearError();

    forkJoin({
      count: this.productsService.getTotalCount(),
      averagePrice: this.productsService.getAveragePrice(),
      maxPrice: this.productsService.getMaxPrice(),
      minPrice: this.productsService.getMinPrice(),
      inventoryValue: this.productsService.getInventoryValue(),
      categoryCount: this.productsService.getCategoryCount(),
      brandCount: this.productsService.getBrandCount(),
    }).subscribe({
      next: (value) => {
        this.stats.set(value);
        this.statsLoading.set(false);
      },
      error: (err) => {
        this.setApiError(err, 'Could not load product stats.');
        this.statsLoading.set(false);
      },
    });
  }

  protected objectEntries(value?: GroupedCountResponse): Array<[string, number]> {
    return Object.entries(value ?? {});
  }

  protected categoryOptions(): string[] {
    return this.mergeOptions(
      this.defaultCategoryOptions,
      this.products().map((item) => item.category),
      this.searchCategory,
      this.draft.category,
      this.updateDraft.category,
    );
  }

  protected brandOptions(): string[] {
    return this.mergeOptions(
      this.defaultBrandOptions,
      this.products().map((item) => item.brand),
      this.searchBrand,
      this.draft.brand,
      this.updateDraft.brand,
    );
  }

  private runStockAction(mode: 'buy' | 'restock'): void {
    if (!this.actionProductId || this.actionQuantity <= 0) {
      this.error.set('Enter valid product id and quantity.');
      return;
    }

    this.loading.set(true);
    this.clearError();

    const request =
      mode === 'buy'
        ? this.productsService.buy(this.actionProductId, { quantity: this.actionQuantity })
        : this.productsService.restock(this.actionProductId, { quantity: this.actionQuantity });

    request.subscribe({
      next: () => this.loadProducts(),
      error: (err) => {
        this.setApiError(err, `Could not ${mode} product stock.`);
        this.loading.set(false);
      },
    });
  }

  private runListQuery(request$: Observable<Product[]>, failureMessage: string): void {
    this.loading.set(true);
    this.clearError();

    request$.subscribe({
      next: (list) => {
        this.products.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.setApiError(err, failureMessage);
        this.loading.set(false);
      },
    });
  }

  private matchesCombinedFilters(item: Product): boolean {
    const keyword = this.searchKeyword.trim().toLowerCase();
    const category = this.searchCategory.trim().toLowerCase();
    const brand = this.searchBrand.trim().toLowerCase();
    const minPriceFromRange = this.rangeMin > 0 ? this.rangeMin : 0;
    const minPrice = this.singlePrice > 0 ? Math.max(this.singlePrice, minPriceFromRange) : minPriceFromRange;
    const maxPrice = this.rangeMax > 0 ? this.rangeMax : Number.POSITIVE_INFINITY;

    if (this.productById > 0 && item.id !== this.productById) {
      return false;
    }

    if (keyword) {
      const name = item.name?.toLowerCase() ?? '';
      const description = item.description?.toLowerCase() ?? '';
      if (!name.includes(keyword) && !description.includes(keyword)) {
        return false;
      }
    }

    if (category && item.category.toLowerCase() !== category) {
      return false;
    }

    if (brand && item.brand.toLowerCase() !== brand) {
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

  private addCreatedProductToList(payload: ProductCreateRequest, idFromServer?: number): void {
    const created: Product = {
      id: idFromServer ?? this.nextLocalId(),
      ...payload,
    };

    this.products.update((list) => [created, ...list]);
  }

  private replaceProductInList(id: number, payload: ProductUpdateRequest): void {
    this.products.update((list) =>
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

  private removeProductFromList(id: number): void {
    this.products.update((list) => list.filter((item) => item.id !== id));
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
    const ids = this.products()
      .map((item) => item.id ?? 0)
      .filter((id) => Number.isFinite(id));
    const minId = ids.length ? Math.min(...ids) : 0;
    return minId <= 0 ? minId - 1 : -1;
  }

  private emptyDraft(): ProductCreateRequest {
    return {
      name: '',
      description: '',
      price: 0,
      category: '',
      brand: '',
      stockQuantity: 0,
      rating: 0,
    };
  }

  private mergeOptions(base: string[], dynamic: Array<string | undefined>, ...extras: Array<string | undefined>): string[] {
    const options = new Set<string>();

    const addOption = (value?: string): void => {
      if (!value) {
        return;
      }

      const normalized = value.trim();
      if (normalized) {
        options.add(normalized);
      }
    };

    base.forEach(addOption);
    dynamic.forEach(addOption);
    extras.forEach(addOption);

    return [...options].sort((a, b) => a.localeCompare(b));
  }
}