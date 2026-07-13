import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-books-filter',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './books-filter.component.html',
  styleUrl: './books-filter.component.css',
})
export class BooksFilterComponent {
  @Input() loading = false;

  @Input() bookById = 0;
  @Output() bookByIdChange = new EventEmitter<number>();

  @Input() searchTitle = '';
  @Output() searchTitleChange = new EventEmitter<string>();

  @Input() searchAuthor = '';
  @Output() searchAuthorChange = new EventEmitter<string>();

  @Input() searchPublisher = '';
  @Output() searchPublisherChange = new EventEmitter<string>();

  @Input() singlePrice = 0;
  @Output() singlePriceChange = new EventEmitter<number>();

  @Input() rangeMin = 0;
  @Output() rangeMinChange = new EventEmitter<number>();

  @Input() rangeMax = 0;
  @Output() rangeMaxChange = new EventEmitter<number>();

  @Output() reset = new EventEmitter<void>();
  @Output() applyFilters = new EventEmitter<void>();
  @Output() getById = new EventEmitter<void>();
  @Output() byTitle = new EventEmitter<void>();
  @Output() byAuthor = new EventEmitter<void>();
  @Output() byPublisher = new EventEmitter<void>();
  @Output() priceAbove = new EventEmitter<void>();
  @Output() priceBelow = new EventEmitter<void>();
  @Output() priceRange = new EventEmitter<void>();
  @Output() sortTitle = new EventEmitter<void>();
  @Output() sortAuthor = new EventEmitter<void>();
  @Output() sortPriceAsc = new EventEmitter<void>();
  @Output() sortPriceDesc = new EventEmitter<void>();
}
