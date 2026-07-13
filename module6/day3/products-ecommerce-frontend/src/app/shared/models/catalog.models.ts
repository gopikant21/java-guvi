export interface Product {
  id?: number;
  name: string;
  description?: string;
  price: number;
  discountPercentage?: number;
  taxPercentage?: number;
  category: string;
  brand: string;
  stockQuantity: number;
  rating: number;
  reviewCount?: number;
  active?: boolean;
  sku?: string;
}

export type ProductCreateRequest = Omit<Product, 'id'>;
export type ProductUpdateRequest = Omit<Product, 'id'>;

export interface PriceRangeQuery {
  min: number;
  max: number;
}

export interface TopRatedQuery {
  limit: number;
}

export interface DiscountedProductsQuery {
  minRating: number;
}

export interface QuantityActionQuery {
  quantity: number;
}

export type GroupedCountResponse = Record<string, number>;

export interface Book {
  id?: number;
  title: string;
  author: string;
  publisher: string;
  price: number;
}

export type BookCreateRequest = Omit<Book, 'id'>;
export type BookUpdateRequest = Omit<Book, 'id'>;