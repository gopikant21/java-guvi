import { inject } from '@angular/core';
import { CanActivateFn, Router, Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { adminGuard } from './core/guards/admin-guard';
import { userGuard } from './core/guards/user-guard';
import { Token } from './features/auth/services/token';
import { AuthLayout } from './layouts/auth-layout/auth-layout';
import { CustomerLayout } from './layouts/customer-layout/customer-layout';
import { AdminLayout } from './layouts/admin-layout/admin-layout';
import { Login } from './features/auth/pages/login/login';
import { CustomerDashboard } from './features/dashboard/pages/customer-dashboard/customer-dashboard';
import { AdminDashboard } from './features/dashboard/pages/admin-dashboard/admin-dashboard';
import { ProductList } from './features/products/pages/product-list/product-list';
import { ProductDetails } from './features/products/pages/product-details/product-details';
import { AddProduct } from './features/products/pages/add-product/add-product';
import { EditProduct } from './features/products/pages/edit-product/edit-product';
import { Inventory } from './features/products/pages/inventory/inventory';
import { OrderList } from './features/orders/pages/order-list/order-list';
import { OrderDetails } from './features/orders/pages/order-details/order-details';
import { CreateOrder } from './features/orders/pages/create-order/create-order';
import { RegisterCustomer } from './features/customers/pages/register-customer/register-customer';
import { CustomerList } from './features/customers/pages/customer-list/customer-list';
import { CustomerDetails } from './features/customers/pages/customer-details/customer-details';
import { EditCustomer } from './features/customers/pages/edit-customer/edit-customer';
import { Profile } from './features/profile/pages/profile/profile';
import { Analytics } from './features/admin/pages/analytics/analytics';
import { Reports } from './features/admin/pages/reports/reports';
import { LowStock } from './features/admin/pages/low-stock/low-stock';
import { BulkUpdate } from './features/admin/pages/bulk-update/bulk-update';

const productDetailsEntryGuard: CanActivateFn = (route) => {
	const tokenService = inject(Token);
	const router = inject(Router);

	if (tokenService.hasRole('ROLE_ADMIN')) {
		return router.createUrlTree(['/admin/products/details'], {
			queryParams: route.queryParams
		});
	}

	return router.createUrlTree(['/customer/products/details'], {
		queryParams: route.queryParams
	});
};

export const routes: Routes = [
	{
		path: '',
		pathMatch: 'full',
		redirectTo: 'login'
	},
	{
		path: '',
		component: AuthLayout,
		children: [
			{
				path: 'login',
				component: Login
			}
		]
	},
	{
		path: 'products/details',
		canActivate: [authGuard, productDetailsEntryGuard],
		component: ProductDetails
	},
	{
		path: '',
		component: CustomerLayout,
		canActivate: [authGuard, userGuard],
		children: [
			{
				path: 'dashboard/customer',
				component: CustomerDashboard
			},
			{
				path: 'products',
				component: ProductList
			},
			{
				path: 'customer/products/details',
				component: ProductDetails
			},
			{
				path: 'orders',
				component: OrderList
			},
			{
				path: 'orders/details',
				component: OrderDetails
			},
			{
				path: 'orders/create',
				component: CreateOrder
			},
			{
				path: 'customers/register',
				component: RegisterCustomer
			},
			{
				path: 'customers/details',
				component: CustomerDetails
			},
			{
				path: 'customers/edit',
				component: EditCustomer
			},
			{
				path: 'profile',
				component: Profile
			}
		]
	},
	{
		path: '',
		component: AdminLayout,
		canActivate: [authGuard, adminGuard],
		children: [
			{
				path: 'dashboard/admin',
				component: AdminDashboard
			},
			{
				path: 'admin/products',
				component: ProductList
			},
			{
				path: 'admin/products/add',
				component: AddProduct
			},
			{
				path: 'admin/products/details',
				component: ProductDetails
			},
			{
				path: 'admin/products/edit',
				redirectTo: 'admin/products',
				pathMatch: 'full'
			},
			{
				path: 'admin/products/edit/:id',
				component: EditProduct
			},
			{
				path: 'admin/products/inventory',
				component: Inventory
			},
			{
				path: 'admin/orders',
				component: OrderList
			},
			{
				path: 'admin/orders/create',
				component: CreateOrder
			},
			{
				path: 'admin/orders/details',
				component: OrderDetails
			},
			{
				path: 'admin/customers',
				component: CustomerList
			},
			{
				path: 'admin/customers/details',
				component: CustomerDetails
			},
			{
				path: 'admin/customers/edit',
				component: EditCustomer
			},
			{
				path: 'admin/analytics',
				component: Analytics
			},
			{
				path: 'admin/reports',
				component: Reports
			},
			{
				path: 'admin/low-stock',
				component: LowStock
			},
			{
				path: 'admin/bulk-update',
				component: BulkUpdate
			}
		]
	},
	{
		path: '**',
		redirectTo: 'login'
	}
];
