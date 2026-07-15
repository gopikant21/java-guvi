import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
	OrderItemRequest,
	OrderItemResponse,
	OrderResponse,
	OrderTotalResponse,
} from '../models/order.models';

@Injectable({ providedIn: 'root' })
export class Order {
	private readonly http = inject(HttpClient);
	private readonly baseUrl = 'http://localhost:8080';

	createOrderForCustomer(customerId: number): Observable<OrderResponse> {
		return this.http.post<OrderResponse>(`${this.baseUrl}/api/orders/customer/${customerId}`, {});
	}

	getAllOrders(): Observable<OrderResponse[]> {
		return this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders`);
	}

	getOrderById(orderId: number): Observable<OrderResponse> {
		return this.http.get<OrderResponse>(`${this.baseUrl}/api/orders/${orderId}`);
	}

	getOrdersByCustomer(customerId: number): Observable<OrderResponse[]> {
		return this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders/customer/${customerId}`);
	}

	getOrdersByCustomerNewest(customerId: number): Observable<OrderResponse[]> {
		return this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders/customer/${customerId}/newest`);
	}

	addItemToOrder(orderId: number, payload: OrderItemRequest): Observable<OrderItemResponse> {
		return this.http.post<OrderItemResponse>(`${this.baseUrl}/api/orders/${orderId}/items`, payload);
	}

	getOrderItems(orderId: number): Observable<OrderItemResponse[]> {
		return this.http.get<OrderItemResponse[]>(`${this.baseUrl}/api/orders/${orderId}/items`);
	}

	updateOrderItemQuantity(orderItemId: number, quantity: number): Observable<OrderItemResponse> {
		return this.http.put<OrderItemResponse>(
			`${this.baseUrl}/api/orders/items/${orderItemId}?quantity=${quantity}`,
			{}
		);
	}

	removeOrderItem(orderItemId: number): Observable<string> {
		return this.http.delete(`${this.baseUrl}/api/orders/items/${orderItemId}`, {
			responseType: 'text',
		});
	}

	getOrderTotal(orderId: number): Observable<OrderTotalResponse> {
		return this.http.get<OrderTotalResponse>(`${this.baseUrl}/api/orders/${orderId}/total`);
	}

	cancelOrder(orderId: number): Observable<string> {
		return this.http.delete(`${this.baseUrl}/api/orders/${orderId}`, {
			responseType: 'text',
		});
	}
}
