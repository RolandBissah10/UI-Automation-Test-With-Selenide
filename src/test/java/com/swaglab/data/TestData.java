package com.swaglab.data;

/**
 * Central repository for all hardcoded test data used in the Swag Labs test suite.
 * This class consolidates names, product titles, error messages, and sort options.
 */
public class TestData {
    // Standard User Info
    public static final String FIRST_NAME_JOHN = "John";
    public static final String LAST_NAME_DOE = "Doe";
    public static final String ZIP_DEFAULT = "12345";
    
    // Alternative User Info
    public static final String FIRST_NAME_JANE = "Jane";
    public static final String LAST_NAME_SMITH = "Smith";
    public static final String ZIP_BEVERLY_HILLS = "90210";

    // Products
    public static final String PRODUCT_BACKPACK = "Sauce Labs Backpack";
    public static final String PRODUCT_BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String PRODUCT_BOLT_TSHIRT = "Sauce Labs Bolt T-Shirt";
    public static final String PRODUCT_ONESIE = "Sauce Labs Onesie";

    // Checkout Error Messages
    public static final String ERROR_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String ERROR_LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String ERROR_POSTAL_CODE_REQUIRED = "Error: Postal Code is required";
    
    // Login Error Messages
    public static final String ERROR_LOCKED_OUT_USER = "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERROR_INVALID_CREDENTIALS = "Epic sadface: Username and password do not match any user in this service";

    // Sort Options
    public static final String SORT_PRICE_HIGH_TO_LOW = "Price (high to low)";
    public static final String SORT_PRICE_LOW_TO_HIGH = "Price (low to high)";
    public static final String SORT_NAME_A_TO_Z = "Name (unit-A to Z)";
    public static final String SORT_NAME_Z_TO_A = "Name (Z to A)";
}
