// src/app/features/products/components/product-table/product-table.ts
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProductResponse } from '../../models/product.models';

@Component({
  selector: 'app-product-table',
  imports: [CommonModule],
  templateUrl: './product-table.html',
  styleUrls: ['./product-table.css'],
})
export class ProductTable {
  @Input() products: ProductResponse[] = [];
  @Input() loading = false;
  @Input() errorMessage = '';
  @Input() isAdmin = false;

  @Output() view = new EventEmitter<number>();
  @Output() edit = new EventEmitter<number>();
  @Output() remove = new EventEmitter<number>();

  onView(id: number): void {
    this.view.emit(id);
  }

  onEdit(id: number): void {
    this.edit.emit(id);
  }

  onDelete(id: number): void {
    this.remove.emit(id);
  }
}