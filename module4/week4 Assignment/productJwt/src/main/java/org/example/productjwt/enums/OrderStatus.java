package org.example.productjwt.enums;

/**
 * Enum representing the lifecycle status of an order.
 * Transitions: PENDING -> PROCESSING -> SHIPPED -> DELIVERED
 * or CANCELLED at any stage.
 */
public enum OrderStatus {
    PENDING("Pending", "Order created but not yet processed"),
    PROCESSING("Processing", "Order is being processed"),
    SHIPPED("Shipped", "Order has been shipped"),
    DELIVERED("Delivered", "Order has been delivered"),
    CANCELLED("Cancelled", "Order has been cancelled");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if status can transition to another status
     * @param nextStatus the target status
     * @return true if transition is valid
     */
    public boolean canTransitionTo(OrderStatus nextStatus) {
        if (this == CANCELLED) {
            return false; // Cancelled orders cannot transition
        }

        return switch (this) {
            case PENDING -> nextStatus == PROCESSING || nextStatus == CANCELLED;
            case PROCESSING -> nextStatus == SHIPPED || nextStatus == CANCELLED;
            case SHIPPED -> nextStatus == DELIVERED;
            case DELIVERED -> false; // Delivered orders cannot transition
            case CANCELLED -> false;
        };
    }
}

