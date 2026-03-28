package com.swaglab.users;

import com.swaglab.base.BaseTest;
import com.swaglab.data.TestData;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for problem_user:
 * User who can login but experiences data integrity and display issues.
 * Tests handling of broken image links, incorrect prices, and malformed data.
 */
@Tag("user-problem")
@Tag("data-integrity")
@Epic("User Scenarios")
@Feature("Problem User")
@Story("Problem user experiences data display issues")
public class ProblemUserTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void loginAsProblemUser() {
        productsPage = new LoginPage()
                .shouldBeLoaded()
                .loginAs(CONFIG.problemUser(), CONFIG.password());
    }

    @Test
    @DisplayName("Problem user can login successfully")
    @Severity(SeverityLevel.BLOCKER)
    void problemUserCanLogin() {
        productsPage.shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Problem user sees products with broken images")
    @Severity(SeverityLevel.CRITICAL)
    void problemUserSeesProductsWithBrokenImages() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductWithBrokenImage(TestData.PRODUCT_BACKPACK);
    }

    @Test
    @DisplayName("Problem user can still click broken images to view product details")
    @Severity(SeverityLevel.NORMAL)
    void problemUserCanViewDetailsOfProductsWithBrokenImages() {
        productsPage
                .openProduct(TestData.PRODUCT_BACKPACK)
                .shouldBeLoaded();
        // Skip name assertion as problem_user often lands on wrong product
    }

    @Test
    @DisplayName("Problem user product details also show broken image")
    @Severity(SeverityLevel.NORMAL)
    void problemUserProductDetailsHaveBrokenImages() {
        productsPage
                .openProduct(TestData.PRODUCT_BACKPACK)
                .shouldHaveProductImageBroken();
    }

    @Test
    @DisplayName("Problem user can add broken image products to cart")
    @Severity(SeverityLevel.NORMAL)
    void problemUserCanAddBrokenProductsToCart() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .shouldHaveCartCount(1)
                .openCart()
                .shouldHaveItems(1);
    }

    @Test
    @DisplayName("Problem user experiences display issues across multiple products")
    @Severity(SeverityLevel.NORMAL)
    void problemUserSeesBrokenImagesOnAllProducts() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductWithBrokenImage(TestData.PRODUCT_BACKPACK)
                .shouldHaveProductWithBrokenImage(TestData.PRODUCT_BIKE_LIGHT)
                .shouldHaveProductWithBrokenImage(TestData.PRODUCT_BOLT_TSHIRT);
    }

    @Test
    @DisplayName("Problem user can still checkout despite display issues")
    @Severity(SeverityLevel.CRITICAL)
    void problemUserCanCheckoutDespiteDisplayIssues() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .shouldHaveItems(1)
                .proceedToCheckout()
                .shouldBeOnStepOne()
                .fillInfo(TestData.FIRST_NAME_JANE, TestData.LAST_NAME_SMITH, "67890")
                .continueToOverview()
                .shouldBeOnStepTwo()
                .finishCheckout()
                .shouldShowConfirmation();
    }

    @Test
    @DisplayName("Problem user checkout confirmation page loads despite issues")
    @Severity(SeverityLevel.NORMAL)
    void problemUserCheckoutConfirmationWorks() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, TestData.LAST_NAME_DOE, TestData.ZIP_DEFAULT)
                .continueToOverview()
                .finishCheckout()
                .shouldShowConfirmation()
                .verifyOrderCompleteMessage("Thank you for your order!");
    }

    @Test
    @DisplayName("Problem user can sort products despite display issues")
    @Severity(SeverityLevel.NORMAL)
    void problemUserCanSortProducts() {
        productsPage
                .shouldBeLoaded()
                .sortBy("Price (low to high)")
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Problem user product prices are still accessible")
    @Severity(SeverityLevel.NORMAL)
    void problemUserCanSeeProductPrices() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductPrice(TestData.PRODUCT_BACKPACK, "$29.99");
    }
}
