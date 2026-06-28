package org.example.libraryjwt.util;

/**
 * Constants used throughout the application
 */
public class AppConstants {

    // Book Types
    public static final String BOOK_TYPE_FICTION = "FICTION";
    public static final String BOOK_TYPE_ACADEMIC = "ACADEMIC";
    public static final String BOOK_TYPE_REFERENCE = "REFERENCE";

    // Issue Record Status
    public static final String ISSUE_STATUS_ISSUED = "ISSUED";
    public static final String ISSUE_STATUS_RETURNED = "RETURNED";
    public static final String ISSUE_STATUS_OVERDUE = "OVERDUE";

    // Payment Types
    public static final String PAYMENT_TYPE_CASH = "CASH";
    public static final String PAYMENT_TYPE_CARD = "CARD";
    public static final String PAYMENT_TYPE_ONLINE = "ONLINE";

    // User Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_USER = "USER";

    // API Paths
    public static final String API_BASE_PATH = "/api";
    public static final String AUTH_LOGIN = "/auth/login";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String BOOKS_ENDPOINT = "/books";
    public static final String MEMBERS_ENDPOINT = "/members";
    public static final String DASHBOARD_ENDPOINT = "/dashboard";

    // Error Messages
    public static final String MEMBER_NOT_FOUND = "Member not found with ID: ";
    public static final String BOOK_NOT_FOUND = "Book not found with ISBN: ";
    public static final String ISSUE_NOT_FOUND = "Issue record not found with ID: ";

    // Success Messages
    public static final String MEMBER_ADDED_SUCCESS = "Member added successfully";
    public static final String BOOK_ADDED_SUCCESS = "Book added successfully";
    public static final String ISSUE_CREATED_SUCCESS = "Book issued successfully";
    public static final String BOOK_RETURNED_SUCCESS = "Book returned successfully";

    // Fine Calculation Constants
    public static final double DEFAULT_FINE_RATE = 1.0;
    public static final double FINE_RATE_INCREMENT = 0.10; // 10% increase

    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUMBER = 0;

    // Token Expiration (in milliseconds)
    public static final long TOKEN_EXPIRATION_TIME = 3600000; // 1 hour
}

