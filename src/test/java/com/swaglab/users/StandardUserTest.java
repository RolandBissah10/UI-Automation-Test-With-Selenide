package com.swaglab.users;

import com.swaglab.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for standard_user:
 * Standard user with full access and normal behavior.
 * Covers the complete happy path user flow.
 */
@Tag("user-standard")
@Tag("smoke")
@Epic("User Scenarios")
@Feature("Standard User")
@Story("Standard user can perform all normal operations")
public class StandardUserTest extends BaseTest {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeEach
    void setupAndLogin() {
        loginPage = new LoginPage().shouldBeLoaded();
        productsPage = loginPage.loginAs(CONFIG.standardUser(), CONFIG.password());
    }

    @Test
    @DisplayName("Standard user can log in successfully")
    @Severity(SeverityLevel.BLOCKER)
    void loginSuccessfully() {
        productsPage.shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Standard user can view all products")
    @Severity(SeverityLevel.CRITICAL)
    void viewAllProducts() {
        productsPage.shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Standard user can add products to cart")
    @Severity(SeverityLevel.CRITICAL)
    void addProductsToCart() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light")
                .shouldHaveCartCount(2);
    }

    @Test
    @DisplayName("Standard user can view product details")
    @Severity(SeverityLevel.NORMAL)
    void viewProductDetails() {
        productsPage
                .openProduct("Sauce Labs Backpack")
                .shouldBeLoaded()
                .shouldShowProductName("Sauce Labs Backpack");
    }

    @Test
    @DisplayName("Standard user can sort products")
    @Severity(SeverityLevel.NORMAL)
    void sortProducts() {
        productsPage
                .sortBy("Price (low to high)")
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }



    @Test
    @DisplayName("Standard user can remove items from cart")
    @Severity(SeverityLevel.NORMAL)
    void removeItemFromCart() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light")
                .openCart()
                .shouldHaveItems(2)
                .removeItem("Sauce Labs Backpack")
                .shouldHaveItems(1)
                .shouldContainItem("Sauce Labs Bike Light");
    }



    @Test
    @DisplayName("Standard user can continue shopping from cart")
    @Severity(SeverityLevel.NORMAL)
    void continueShoppingFromCart() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .shouldHaveItems(1)
                .continueShopping()
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }
}
