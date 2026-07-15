import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Token } from '../../../auth/services/token';
import { OrderItem } from '../../components/order-item/order-item';
import { OrderSummary } from '../../components/order-summary/order-summary';
import { OrderItemResponse, OrderResponse } from '../../models/order.models';
import { Order } from '../../services/order';

@Component({
  selector: 'app-order-details',
  imports: [CommonModule, ReactiveFormsModule, OrderItem, OrderSummary],
  templateUrl: './order-details.html',
  styleUrl: './order-details.css',
})
export class OrderDetails implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly orderService = inject(Order);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly tokenService = inject(Token);

  readonly order = signal<OrderResponse | null>(null);
  readonly loading = signal(false);
  readonly itemBusy = signal(false);
  readonly errorMessage = signal('');
  readonly successMessage = signal('');
  readonly isAdmin = signal(false);

  readonly canEdit = computed(() => {
    const current = this.order();
    if (!current) {
      return false;
    }

    return current.status !== 'DELIVERED' && current.status !== 'CANCELLED';
  });

  readonly addItemForm = this.fb.nonNullable.group({
    productId: [0, [Validators.required, Validators.min(1)]],
    quantity: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    this.isAdmin.set(this.tokenService.hasRole('ROLE_ADMIN'));
    const orderId = this.parseOrderId(this.route.snapshot.queryParamMap.get('id'));

    if (orderId === null) {
      this.errorMessage.set('Order id query parameter is required.');
      return;
    }

    this.loadOrder(orderId);
  }

  loadOrder(orderId: number): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.getOrderById(orderId).subscribe({
      next: (data) => {
        this.order.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load order details.');
        this.loading.set(false);
      },
    });
  }

  onAddItem(): void {
    const current = this.order();
    if (!current || this.addItemForm.invalid || !this.canEdit()) {
      this.errorMessage.set('Cannot add item. Validate the form and order state.');
      return;
    }

    this.itemBusy.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService
      .addItemToOrder(current.orderId, {
        productId: this.addItemForm.controls.productId.getRawValue(),
        quantity: this.addItemForm.controls.quantity.getRawValue(),
      })
      .subscribe({
        next: () => {
          this.successMessage.set('Item added successfully.');
          this.addItemForm.controls.quantity.setValue(1);
          this.loadOrder(current.orderId);
          this.itemBusy.set(false);
        },
        error: () => {
          this.errorMessage.set('Failed to add item.');
          this.itemBusy.set(false);
        },
      });
  }

  onUpdateItem(event: { itemId: number; quantity: number }): void {
    const current = this.order();
    if (!current || !this.canEdit()) {
      return;
    }

    this.itemBusy.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.updateOrderItemQuantity(event.itemId, event.quantity).subscribe({
      next: () => {
        this.successMessage.set('Item quantity updated.');
        this.loadOrder(current.orderId);
        this.itemBusy.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to update item quantity.');
        this.itemBusy.set(false);
      },
    });
  }

  onRemoveItem(orderItemId: number): void {
    const current = this.order();
    if (!current || !this.canEdit()) {
      return;
    }

    if (!confirm('Remove this item from order?')) {
      return;
    }

    this.itemBusy.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.removeOrderItem(orderItemId).subscribe({
      next: () => {
        this.successMessage.set('Item removed from order.');
        this.loadOrder(current.orderId);
        this.itemBusy.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to remove order item.');
        this.itemBusy.set(false);
      },
    });
  }

  onCancelOrder(): void {
    const current = this.order();
    if (!current || !this.canEdit()) {
      return;
    }

    if (!confirm(`Cancel order #${current.orderId}?`)) {
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.cancelOrder(current.orderId).subscribe({
      next: () => {
        this.successMessage.set('Order cancelled.');
        this.loadOrder(current.orderId);
      },
      error: () => {
        this.errorMessage.set('Failed to cancel order.');
        this.loading.set(false);
      },
    });
  }

  onBack(): void {
    const current = this.order();
    const routePath = this.isAdmin() ? '/admin/orders' : '/orders';

    void this.router.navigate([routePath], {
      queryParams: current ? { customerId: current.customerId } : {},
    });
  }

  trackItem(_: number, item: OrderItemResponse): number {
    return item.id;
  }

  private parseOrderId(value: string | null): number | null {
    if (!value) {
      return null;
    }

    const parsed = Number(value);
    return Number.isInteger(parsed) && parsed > 0 ? parsed : null;
  }
}
