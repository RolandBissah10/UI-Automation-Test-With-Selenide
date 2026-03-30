package com.swaglab.smoke;

import com.swaglab.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductDetailsPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
@Epic("Products")
@Feature("Product Listing")
public class ProductsPageTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void login() {
        productsPage = new LoginPage()
                .loginAs(CONFIG.standardUser(), CONFIG.password());
    }

     @Test
    @DisplayName("Products page loads with items")
    @Severity(SeverityLevel.BLOCKER)
    void productsPageLoadsTest() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

     @Test
    @DisplayName("Sort products by price low to high")
    @Story("User can sort products")
    @Severity(SeverityLevel.NORMAL)
    void sortByPriceLowToHighTest() {
        productsPage
                .shouldBeLoaded()
                .sortBy("Price (low to high)")
                .shouldBeLoaded(); // Products still visible after sort
    }

     @Test
    @DisplayName("Sort products by name Z to A")
    @Severity(SeverityLevel.MINOR)
    void sortByNameZToATest() {
        productsPage
                .shouldBeLoaded()
                .sortBy("Name (Z to A)")
                .shouldBeLoaded();
    }

     @Test
    @DisplayName("Open product details page")
    @Story("User can view product details")
    @Severity(SeverityLevel.CRITICAL)
    void openProductDetailsTest() {
        productsPage
                .shouldBeLoaded()
                .openProduct("Sauce Labs Backpack")
                .shouldBeLoaded();
    }

     @Test
    @DisplayName("Add product to cart from details page")
    @Severity(SeverityLevel.CRITICAL)
    void addToCartFromDetailsTest() {
        productsPage
                .openProduct("Sauce Labs Backpack")
                .addToCart()
                .shouldShowRemoveButton();
    }

     @Test
    @DisplayName("Navigate back to products from details page")
    @Severity(SeverityLevel.NORMAL)
    void navigateBackFromDetailsTest() {
        productsPage
                .openProduct("Sauce Labs Backpack")
                .goBackToProducts()
                .shouldBeLoaded();
    }
}
