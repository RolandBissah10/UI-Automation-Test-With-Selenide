package com.swaglabs.utils;

/**
 * Centralised test data constants.
 * For data-driven tests, see parameterised test examples.
 */
public final class TestData {

    private TestData() {}

    // ── Products ──────────────────────────────────────────────────────────────
    public static final String BACKPACK         = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT       = "Sauce Labs Bike Light";
    public static final String BOLT_T_SHIRT     = "Sauce Labs Bolt T-Shirt";
    public static final String FLEECE_JACKET    = "Sauce Labs Fleece Jacket";
    public static final String ONESIE           = "Sauce Labs Onesie";
    public static final String RED_T_SHIRT      = "Test.allTheThings() T-Shirt (Red)";

    public static final int TOTAL_PRODUCT_COUNT = 6;

    // ── Sort options ──────────────────────────────────────────────────────────
    public static final String SORT_AZ          = "Name (A to Z)";
    public static final String SORT_ZA          = "Name (Z to A)";
    public static final String SORT_PRICE_LOW   = "Price (low to high)";
    public static final String SORT_PRICE_HIGH  = "Price (high to low)";

    // ── Checkout ──────────────────────────────────────────────────────────────
    public static final String FIRST_NAME       = "John";
    public static final String LAST_NAME        = "Doe";
    public static final String POSTAL_CODE      = "12345";

    // ── Error messages ────────────────────────────────────────────────────────
    public static final String ERR_LOCKED_USER  =
            "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERR_WRONG_CREDS  =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String ERR_USERNAME_REQ =
            "Epic sadface: Username is required";
    public static final String ERR_PASSWORD_REQ =
            "Epic sadface: Password is required";
    public static final String ERR_FIRST_NAME   = "Error: First Name is required";
    public static final String ERR_LAST_NAME    = "Error: Last Name is required";
    public static final String ERR_POSTAL_CODE  = "Error: Postal Code is required";

    // ── Confirmation ──────────────────────────────────────────────────────────
    public static final String ORDER_CONFIRMED  = "Thank you for your order!";
}
