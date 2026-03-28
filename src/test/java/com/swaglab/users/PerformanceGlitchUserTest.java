package com.swaglab.users;

import com.codeborne.selenide.Configuration;
import com.swaglab.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for performance_glitch_user:
 * User who can login but experiences significant page load delays.
 * Tests timeout handling, page responsiveness, and performance requirements.
 */
@Tag("user-performance")
@Tag("performance")
@Epic("User Scenarios")
@Feature("Performance Glitch User")
@Story("Performance glitch user experiences slow page loads")
public class PerformanceGlitchUserTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void loginAsPerformanceGlitchUser() {
        // Increase timeout for this user to accommodate slow loads
        long originalTimeout = Configuration.timeout;
        Configuration.timeout = 30_000; // 30 second timeout for slow user
        
        productsPage = new LoginPage()
                .shouldBeLoaded()
                .loginAs(CONFIG.performanceGlitchUser(), CONFIG.password());
    }

    @Test
    @DisplayName("Performance glitch user can login (with delays)")
    @Severity(SeverityLevel.BLOCKER)
    void performanceGlitchUserCanLogin() {
        productsPage.shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Performance glitch user sees all products despite slow load")
    @Severity(SeverityLevel.CRITICAL)
    void performanceGlitchUserCanViewAllProducts() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Performance glitch user can add to cart despite delays")
    @Severity(SeverityLevel.CRITICAL)
    void performanceGlitchUserCanAddToCart() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .shouldHaveCartCount(1);
    }

    @Test
    @DisplayName("Performance glitch user cart loads slowly")
    @Severity(SeverityLevel.NORMAL)
    void performanceGlitchUserCartLoadsWithDelay() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .shouldBeLoaded()
                .shouldHaveItems(1);
    }

    @Test
    @DisplayName("Performance glitch user product details load slowly")
    @Severity(SeverityLevel.NORMAL)
    void performanceGlitchUserProductDetailsLoadSlowly() {
        productsPage
                .openProduct("Sauce Labs Backpack")
                .shouldBeLoaded()
                .shouldShowProductName("Sauce Labs Backpack");
    }

    @Test
    @DisplayName("Performance glitch user can complete checkout despite delays")
    @Severity(SeverityLevel.BLOCKER)
    void performanceGlitchUserCanCheckout() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .shouldHaveItems(1)
                .proceedToCheckout()
                .shouldBeOnStepOne()
                .fillInfo("John", "Doe", "12345")
                .continueToOverview()
                .shouldBeOnStepTwo()
                .finishCheckout()
                .shouldShowConfirmation();
    }

    @Test
    @DisplayName("Performance glitch user checkout pages load with delays")
    @Severity(SeverityLevel.CRITICAL)
    void performanceGlitchUserCheckoutLoadsSlow() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openCart()
                .proceedToCheckout()
                .shouldBeOnStepOne()
                .fillInfo("Jane", "Smith", "67890")
                .continueToOverview()
                .shouldBeOnStepTwo()
                .verifyOrderTotalVisible();
    }

    @Test
    @DisplayName("Performance glitch user can sort products (with delays)")
    @Severity(SeverityLevel.NORMAL)
    void performanceGlitchUserCanSortProducts() {
        productsPage
                .shouldBeLoaded()
                .sortBy("Price (low to high)")
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Performance glitch user can handle multiple actions sequentially")
    @Severity(SeverityLevel.NORMAL)
    void performanceGlitchUserHandlesMultipleActions() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light")
                .sortBy("Name (A to Z)")
                .shouldHaveProductCount(6)
                .shouldHaveCartCount(2);
    }

    @Test
    @DisplayName("Performance glitch user backward navigation works")
    @Severity(SeverityLevel.NORMAL)
    void performanceGlitchUserBackwardNavigation() {
        productsPage
                .addProductToCart("Sauce Labs Backpack")
                .openProduct("Sauce Labs Bike Light")
                .goBackToProducts()
                .shouldBeLoaded()
                .shouldHaveProductCount(6)
                .shouldHaveCartCount(1);
    }

    @Test
    @DisplayName("Performance glitch user logout works despite delays")
    @Severity(SeverityLevel.CRITICAL)
    void performanceGlitchUserCanLogout() {
        productsPage
                .openMenu()
                .clickLogout();
        new LoginPage().shouldBeLoaded();
    }
}
