import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-products-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products-filter.component.html',
  styleUrl: './products-filter.component.css',
})
export class ProductsFilterComponent {
  @Input() loading = false;
  @Input() categoryOptions: string[] = [];
  @Input() brandOptions: string[] = [];

  @Input() productById = 0;
  @Output() productByIdChange = new EventEmitter<number>();

  @Input() searchName = '';
  @Output() searchNameChange = new EventEmitter<string>();

  @Input() searchKeyword = '';
  @Output() searchKeywordChange = new EventEmitter<string>();

  @Input() searchCategory = '';
  @Output() searchCategoryChange = new EventEmitter<string>();

  @Input() searchBrand = '';
  @Output() searchBrandChange = new EventEmitter<string>();

  @Input() singlePrice = 0;
  @Output() singlePriceChange = new EventEmitter<number>();

  @Input() rangeMin = 0;
  @Output() rangeMinChange = new EventEmitter<number>();

  @Input() rangeMax = 0;
  @Output() rangeMaxChange = new EventEmitter<number>();

  @Input() topRatedLimit = 5;
  @Output() topRatedLimitChange = new EventEmitter<number>();

  @Input() discountedMinRating = 4;
  @Output() discountedMinRatingChange = new EventEmitter<number>();

  @Input() actionProductId = 0;
  @Output() actionProductIdChange = new EventEmitter<number>();

  @Input() actionQuantity = 1;
  @Output() actionQuantityChange = new EventEmitter<number>();

  @Output() reset = new EventEmitter<void>();
  @Output() applyFilters = new EventEmitter<void>();
  @Output() getById = new EventEmitter<void>();
  @Output() byName = new EventEmitter<void>();
  @Output() byKeyword = new EventEmitter<void>();
  @Output() byCategory = new EventEmitter<void>();
  @Output() byBrand = new EventEmitter<void>();
  @Output() priceAbove = new EventEmitter<void>();
  @Output() priceBelow = new EventEmitter<void>();
  @Output() priceRange = new EventEmitter<void>();
  @Output() inStock = new EventEmitter<void>();
  @Output() outOfStock = new EventEmitter<void>();
  @Output() sortName = new EventEmitter<void>();
  @Output() sortRating = new EventEmitter<void>();
  @Output() sortPriceAsc = new EventEmitter<void>();
  @Output() sortPriceDesc = new EventEmitter<void>();
  @Output() topRated = new EventEmitter<void>();
  @Output() discounted = new EventEmitter<void>();
  @Output() buy = new EventEmitter<void>();
  @Output() restock = new EventEmitter<void>();
}
