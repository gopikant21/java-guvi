import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { ProductsComponent } from './features/products/products.component';
import { BooksComponent } from './features/books/books.component';

export const routes: Routes = [
	{ path: '', component: HomeComponent, pathMatch: 'full' },
	{ path: 'products', component: ProductsComponent },
	{ path: 'books', component: BooksComponent },
	{ path: '**', redirectTo: '' },
];
