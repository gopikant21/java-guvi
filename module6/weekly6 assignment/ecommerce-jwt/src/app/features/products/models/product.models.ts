export type ProductCategory =
  | 'ELECTRONICS'
  | 'CLOTHING'
  | 'BOOKS'
  | 'HOME_AND_KITCHEN'
  | 'SPORTS_AND_OUTDOORS'
  | 'TOYS_AND_GAMES'
  | 'BEAUTY_AND_PERSONAL_CARE'
  | 'GROCERIES'
  | 'OTHER';

export interface ProductResponse {
  id: number;
  name: string;
  description: string;
  brand: string;
  category: ProductCategory;
  price: number;
  stocks: number;
}

export interface ProductRequest {
  name: string;
  description: string;
  brand: string;
  category: ProductCategory;
  price: number;
  stocks: number;
}