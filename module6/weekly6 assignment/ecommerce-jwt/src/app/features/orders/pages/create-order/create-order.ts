import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Token } from '../../../auth/services/token';
import { OrderResponse } from '../../models/order.models';
import { Order } from '../../services/order';

@Component({
  selector: 'app-create-order',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-order.html',
  styleUrl: './create-order.css',
})
export class CreateOrder implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly orderService = inject(Order);
  private readonly tokenService = inject(Token);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly isAdmin = signal(false);
  readonly loading = signal(false);
  readonly currentOrder = signal<OrderResponse | null>(null);
  readonly message = signal('');
  readonly errorMessage = signal('');

  readonly createForm = this.fb.nonNullable.group({
    customerId: [0, [Validators.required, Validators.min(1)]],
  });

  readonly itemForm = this.fb.nonNullable.group({
    productId: [0, [Validators.required, Validators.min(1)]],
    quantity: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    this.isAdmin.set(this.tokenService.hasRole('ROLE_ADMIN'));

    const queryCustomerId = this.parseId(this.route.snapshot.queryParamMap.get('customerId'));
    if (queryCustomerId !== null) {
      this.createForm.controls.customerId.setValue(queryCustomerId);
      return;
    }

    const usernameBasedId = this.parseId(this.tokenService.getUsername());
    if (usernameBasedId !== null) {
      this.createForm.controls.customerId.setValue(usernameBasedId);
    }
  }

  onCreateOrder(): void {
    if (this.createForm.invalid) {
      this.errorMessage.set('Customer ID is required.');
      return;
    }

    const customerId = this.createForm.controls.customerId.getRawValue();
    this.loading.set(true);
    this.message.set('');
    this.errorMessage.set('');

    this.orderService.createOrderForCustomer(customerId).subscribe({
      next: (order) => {
        this.currentOrder.set(order);
        this.message.set(`Order #${order.orderId} created.`);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Failed to create order.');
        this.loading.set(false);
      },
    });
  }

  onAddItem(): void {
    if (!this.currentOrder()) {
      this.errorMessage.set('Create an order before adding items.');
      return;
    }

    if (this.itemForm.invalid) {
      this.errorMessage.set('Provide valid product and quantity values.');
      return;
    }

    const order = this.currentOrder();
    if (!order) {
      return;
    }

    this.loading.set(true);
    this.message.set('');
    this.errorMessage.set('');

    this.orderService
      .addItemToOrder(order.orderId, {
        productId: this.itemForm.controls.productId.getRawValue(),
        quantity: this.itemForm.controls.quantity.getRawValue(),
      })
      .subscribe({
        next: () => {
          this.itemForm.controls.quantity.setValue(1);
          this.message.set('Item added to order.');
          this.refreshOrder(order.orderId);
        },
        error: () => {
          this.errorMessage.set('Failed to add item to order.');
          this.loading.set(false);
        },
      });
  }

  onViewDetails(): void {
    const order = this.currentOrder();
    if (!order) {
      return;
    }

    const path = this.isAdmin() ? '/admin/orders/details' : '/orders/details';
    void this.router.navigate([path], { queryParams: { id: order.orderId } });
  }

  onBackToList(): void {
    const path = this.isAdmin() ? '/admin/orders' : '/orders';
    const customerId = this.createForm.controls.customerId.getRawValue();
    void this.router.navigate([path], {
      queryParams: customerId > 0 ? { customerId } : {},
    });
  }

  private refreshOrder(orderId: number): void {
    this.orderService.getOrderById(orderId).subscribe({
      next: (order) => {
        this.currentOrder.set(order);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Item was added, but order refresh failed.');
        this.loading.set(false);
      },
    });
  }

  private parseId(value: string | null): number | null {
    if (!value) {
      return null;
    }

    const parsed = Number(value);
    return Number.isInteger(parsed) && parsed > 0 ? parsed : null;
  }
}
