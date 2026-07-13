package org.example.productjwt.exceptions;

/**
 * Thrown when attempting to order a product but insufficient stock is available.
 * Returns HTTP 409 Conflict
 */
public class InsufficientStockException extends RuntimeException {

    private final Long productId;
    private final int requestedQuantity;
    private final int availableStock;

    public InsufficientStockException(Long productId, int requestedQuantity, int availableStock) {
        super(String.format(
                "Insufficient stock for product ID %d. Requested: %d, Available: %d",
                productId, requestedQuantity, availableStock
        ));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }

    public Long getProductId() {
        return productId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}

