import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrderItemResponse } from '../../models/order.models';

@Component({
  selector: 'app-order-item',
  imports: [CommonModule, FormsModule],
  templateUrl: './order-item.html',
  styleUrl: './order-item.css',
})
export class OrderItem implements OnChanges {
  @Input() item!: OrderItemResponse;
  @Input() canEdit = true;
  @Input() busy = false;

  @Output() updateQuantity = new EventEmitter<{ itemId: number; quantity: number }>();
  @Output() remove = new EventEmitter<number>();

  quantityDraft = 1;

  ngOnChanges(): void {
    this.quantityDraft = this.item?.quantity ?? 1;
  }

  onUpdate(): void {
    if (!this.item || this.quantityDraft < 1) {
      return;
    }

    this.updateQuantity.emit({
      itemId: this.item.id,
      quantity: this.quantityDraft,
    });
  }

  onRemove(): void {
    if (!this.item) {
      return;
    }

    this.remove.emit(this.item.id);
  }
}
