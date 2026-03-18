package com.swaglab.regression;

import com.swaglab.base.BaseTest;
import com.swaglabs.pages.CartPage;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@Epic("Shopping Cart")
@Feature("Cart Functionality")
public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void login() {
        productsPage = new LoginPage()
                .loginAs(CONFIG.standardUser(), CONFIG.password());
    }

    @Test
    @DisplayName("Add single item to cart")
    @Story("User can add items to cart")
    @Severity(SeverityLevel.CRITICAL)
    void addSingleItemToCartTest() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .shouldHaveCartCount(1)
                .openCart()
                .shouldBeLoaded()
                .shouldHaveItems(1)
                .shouldContainItem("Sauce Labs Backpack");
    }

    @Test
    @DisplayName("Add multiple items to cart")
    @Severity(SeverityLevel.CRITICAL)
    void addMultipleItemsToCartTest() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light")
                .shouldHaveCartCount(2)
                .openCart()
                .shouldHaveItems(2)
                .shouldContainItem("Sauce Labs Backpack")
                .shouldContainItem("Sauce Labs Bike Light");
    }

    @Test
    @DisplayName("Remove item from cart")
    @Story("User can remove items from cart")
    @Severity(SeverityLevel.CRITICAL)
    void removeItemFromCartTest() {
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
    @DisplayName("Cart is empty by default")
    @Severity(SeverityLevel.NORMAL)
    void cartIsEmptyByDefaultTest() {
        productsPage
                .openCart()
                .shouldBeLoaded()
                .shouldBeEmpty();
    }

    @Test
    @DisplayName("Continue shopping from cart returns to products")
    @Severity(SeverityLevel.NORMAL)
    void continueShoppingTest() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .shouldHaveItems(1)
                .continueShopping()
                .shouldBeLoaded();
    }

    @Test
    @DisplayName("Cart persists items after navigating away")
    @Severity(SeverityLevel.NORMAL)
    void cartPersistsAfterNavigationTest() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openProduct("Sauce Labs Bike Light")
                .goBackToProducts()
                .openCart()
                .shouldHaveItems(1)
                .shouldContainItem("Sauce Labs Backpack");
    }
}
