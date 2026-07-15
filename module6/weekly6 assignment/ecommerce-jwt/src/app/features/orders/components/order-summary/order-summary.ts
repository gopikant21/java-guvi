import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { OrderResponse } from '../../models/order.models';

@Component({
  selector: 'app-order-summary',
  imports: [CommonModule],
  templateUrl: './order-summary.html',
  styleUrl: './order-summary.css',
})
export class OrderSummary {
  @Input() order: OrderResponse | null = null;
}
