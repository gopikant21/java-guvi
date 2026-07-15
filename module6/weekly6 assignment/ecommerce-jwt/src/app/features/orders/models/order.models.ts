export type OrderStatus = 'PENDING' | 'PROCESSING' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface OrderItemResponse {
  id: number;
  productId: number;
  productName: string;
  productPrice: number;
  quantity: number;
  lineTotal: number;
}

export interface OrderResponse {
  orderId: number;
  customerId: number;
  status: OrderStatus;
  orderDate: string;
  items: OrderItemResponse[];
  total: number;
}

export interface OrderTotalResponse {
  orderId: number;
  total: number;
}