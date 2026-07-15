import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { CustomerResponse } from '../../features/customers/models/customer.models';
import { OrderResponse } from '../../features/orders/models/order.models';
import { ProductResponse } from '../../features/products/models/product.models';

@Injectable({ providedIn: 'root' })
export class Dashboard {
	private readonly http = inject(HttpClient);
	private readonly baseUrl = 'http://localhost:8080';

	getAdminOverview(
		lowStockThreshold = 10,
		highValueThreshold = 1000
	): Observable<AdminDashboardOverview> {
		return forkJoin({
			products: this.http.get<ProductResponse[]>(`${this.baseUrl}/api/products`),
			customers: this.http.get<CustomerResponse[]>(`${this.baseUrl}/api/customers`),
			orders: this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders`),
			lowStockProducts: this.http.get<ProductResponse[]>(
				`${this.baseUrl}/api/products/low-stock/${lowStockThreshold}`
			),
			emptyOrders: this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders/empty`),
			highValueOrders: this.http.get<OrderResponse[]>(
				`${this.baseUrl}/api/orders/high-value/${highValueThreshold}`
			),
		}).pipe(
			map(({ products, customers, orders, lowStockProducts, emptyOrders, highValueOrders }) => {
				const topHighValueOrders = [...highValueOrders]
					.sort((a, b) => (b.total ?? 0) - (a.total ?? 0))
					.slice(0, 10);

				const pendingOrders = orders.filter((order) => order.status === 'PENDING').length;
				const processingOrders = orders.filter((order) => order.status === 'PROCESSING').length;
				const shippedOrders = orders.filter((order) => order.status === 'SHIPPED').length;
				const deliveredOrders = orders.filter((order) => order.status === 'DELIVERED').length;
				const cancelledOrders = orders.filter((order) => order.status === 'CANCELLED').length;

				const revenue = orders
					.filter((order) => order.status !== 'CANCELLED')
					.reduce((sum, order) => sum + (order.total ?? 0), 0);

				return {
					totalProducts: products.length,
					totalCustomers: customers.length,
					totalOrders: orders.length,
					revenue,
					pendingOrders,
					processingOrders,
					shippedOrders,
					deliveredOrders,
					cancelledOrders,
					lowStockProducts,
					emptyOrders,
					highValueOrders: topHighValueOrders,
				};
			})
		);
	}

	getCustomerOverview(customerId: number): Observable<CustomerDashboardOverview> {
		return forkJoin({
			orders: this.http.get<OrderResponse[]>(`${this.baseUrl}/api/orders/customer/${customerId}/newest`),
			availableProducts: this.http.get<ProductResponse[]>(`${this.baseUrl}/api/products/available`),
		}).pipe(
			map(({ orders, availableProducts }) => {
				const activeOrders = orders.filter(
					(order) => order.status === 'PENDING' || order.status === 'PROCESSING' || order.status === 'SHIPPED'
				);

				const deliveredOrders = orders.filter((order) => order.status === 'DELIVERED');
				const cancelledOrders = orders.filter((order) => order.status === 'CANCELLED');

				const totalSpent = deliveredOrders.reduce((sum, order) => sum + (order.total ?? 0), 0);

				return {
					totalOrders: orders.length,
					activeOrders: activeOrders.length,
					deliveredOrders: deliveredOrders.length,
					cancelledOrders: cancelledOrders.length,
					totalSpent,
					recentOrders: orders.slice(0, 5),
					availableProductCount: availableProducts.length,
					featuredProducts: availableProducts.slice(0, 6),
				};
			})
		);
	}
}

export interface AdminDashboardOverview {
	totalProducts: number;
	totalCustomers: number;
	totalOrders: number;
	revenue: number;
	pendingOrders: number;
	processingOrders: number;
	shippedOrders: number;
	deliveredOrders: number;
	cancelledOrders: number;
	lowStockProducts: ProductResponse[];
	emptyOrders: OrderResponse[];
	highValueOrders: OrderResponse[];
}

export interface CustomerDashboardOverview {
	totalOrders: number;
	activeOrders: number;
	deliveredOrders: number;
	cancelledOrders: number;
	totalSpent: number;
	recentOrders: OrderResponse[];
	availableProductCount: number;
	featuredProducts: ProductResponse[];
}
