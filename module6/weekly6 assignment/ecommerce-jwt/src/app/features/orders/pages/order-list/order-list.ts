import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Token } from '../../../auth/services/token';
import { Order } from '../../services/order';
import { OrderResponse } from '../../models/order.models';

@Component({
  selector: 'app-order-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './order-list.html',
  styleUrl: './order-list.css',
})
export class OrderList implements OnInit {
  private readonly orderService = inject(Order);
  private readonly tokenService = inject(Token);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly orders = signal<OrderResponse[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal('');
  readonly successMessage = signal('');
  readonly isAdmin = signal(false);

  customerIdInput = '';

  ngOnInit(): void {
    this.isAdmin.set(this.tokenService.hasRole('ROLE_ADMIN'));

    const queryCustomerId = this.parseCustomerId(this.route.snapshot.queryParamMap.get('customerId'));
    if (queryCustomerId !== null) {
      this.customerIdInput = String(queryCustomerId);
    }

    if (this.isAdmin()) {
      this.fetchAllOrders();
      return;
    }

    if (queryCustomerId !== null) {
      this.fetchOrdersForCustomer(queryCustomerId);
      return;
    }

    const usernameBasedId = this.parseCustomerId(this.tokenService.getUsername());
    if (usernameBasedId !== null) {
      this.customerIdInput = String(usernameBasedId);
      this.fetchOrdersForCustomer(usernameBasedId);
      return;
    }

    this.errorMessage.set('Provide your customer ID to load orders.');
  }

  fetchAllOrders(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.getAllOrders().subscribe({
      next: (data) => {
        this.orders.set(data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load orders.');
        this.loading.set(false);
      },
    });
  }

  fetchOrdersForCustomer(customerId: number): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.orderService.getOrdersByCustomerNewest(customerId).subscribe({
      next: (data) => {
        this.orders.set(data ?? []);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to load customer orders. Check the customer ID.');
        this.loading.set(false);
      },
    });
  }

  onApplyCustomerFilter(): void {
    const customerId = this.parseCustomerId(this.customerIdInput);
    if (customerId === null) {
      this.errorMessage.set('Customer ID must be a positive number.');
      return;
    }

    this.fetchOrdersForCustomer(customerId);
  }

  onCreateOrder(): void {
    const routePath = this.isAdmin() ? '/admin/orders/create' : '/orders/create';
    const customerId = this.parseCustomerId(this.customerIdInput);

    void this.router.navigate([routePath], {
      queryParams: customerId !== null ? { customerId } : {},
    });
  }

  onView(orderId: number): void {
    const routePath = this.isAdmin() ? '/admin/orders/details' : '/orders/details';
    void this.router.navigate([routePath], { queryParams: { id: orderId } });
  }

  onCancel(orderId: number): void {
    if (!confirm(`Cancel order #${orderId}?`)) {
      return;
    }

    this.successMessage.set('');
    this.errorMessage.set('');

    this.orderService.cancelOrder(orderId).subscribe({
      next: () => {
        this.successMessage.set(`Order #${orderId} cancelled.`);
        if (this.isAdmin()) {
          this.fetchAllOrders();
        } else {
          this.onApplyCustomerFilter();
        }
      },
      error: () => {
        this.errorMessage.set('Failed to cancel order.');
      },
    });
  }

  private parseCustomerId(value: string | null): number | null {
    if (!value) {
      return null;
    }

    // Try direct parsing first (for query params)
    const directParse = Number(value);
    if (Number.isInteger(directParse) && directParse > 0) {
      return directParse;
    }

    // Extract numeric part from username like "customer005" → 5
    const match = value.match(/\d+/);
    if (match) {
      const id = Number(match[0]);
      if (id > 0) {
        return id;
      }
    }

    return null;
  }
}
