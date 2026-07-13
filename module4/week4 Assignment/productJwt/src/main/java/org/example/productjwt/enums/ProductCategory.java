package org.example.productjwt.enums;

/**
 * Enum representing product categories in the system.
 * Used for categorization, filtering, and bulk operations.
 */
public enum ProductCategory {
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    BOOKS("Books"),
    HOME_AND_KITCHEN("Home & Kitchen"),
    SPORTS_AND_OUTDOORS("Sports & Outdoors"),
    TOYS_AND_GAMES("Toys & Games"),
    BEAUTY_AND_PERSONAL_CARE("Beauty & Personal Care"),
    GROCERIES("Groceries"),
    OTHER("Other");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get enum from display name (case-insensitive)
     * @param name the display name
     * @return ProductCategory enum
     * @throws IllegalArgumentException if category not found
     */
    public static ProductCategory fromDisplayName(String name) {
        for (ProductCategory category : ProductCategory.values()) {
            if (category.displayName.equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + name);
    }
}

