import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { guestGuard } from './core/guards/guest.guard';
import { roleGuard } from './core/guards/role.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { AdminDashboardComponent } from './features/admin/dashboard/admin-dashboard.component';
import { CustomerDashboardComponent } from './features/customer/dashboard/customer-dashboard.component';
import { ForbiddenComponent } from './features/shared/forbidden/forbidden.component';
import { HomeRedirectComponent } from './features/shared/home-redirect/home-redirect.component';
import { ShellComponent } from './features/shared/shell/shell.component';

export const routes: Routes = [
	{
		path: '',
		pathMatch: 'full',
		canActivate: [authGuard],
		component: HomeRedirectComponent
	},
	{
		path: 'login',
		canActivate: [guestGuard],
		component: LoginComponent
	},
	{
		path: 'register',
		canActivate: [guestGuard],
		component: RegisterComponent
	},
	{
		path: 'forbidden',
		component: ForbiddenComponent
	},
	{
		path: '',
		canActivate: [authGuard],
		component: ShellComponent,
		children: [
			{
				path: 'customer',
				canActivate: [roleGuard],
				data: { roles: ['CUSTOMER'] },
				component: CustomerDashboardComponent
			},
			{
				path: 'admin',
				canActivate: [roleGuard],
				data: { roles: ['ADMIN'] },
				component: AdminDashboardComponent
			}
		]
	},
	{
		path: '**',
		redirectTo: ''
	}
];
