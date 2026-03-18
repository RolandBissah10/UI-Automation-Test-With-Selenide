package com.swaglab.regression;

import com.swaglab.base.BaseTest;
import com.swaglabs.pages.CheckoutPage;
import com.swaglabs.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("regression")
@Epic("Checkout")
@Feature("Checkout Flow")
public class CheckoutTest extends BaseTest {

    @BeforeEach
    void loginAndAddItem() {
        new LoginPage()
                .loginAs(CONFIG.standardUser(), CONFIG.password())
                .addProductToCart("Sauce Labs Backpack")
                .addProductToCart("Sauce Labs Bike Light");
    }

    @Test
    @DisplayName("Complete full checkout flow")
    @Story("User can complete a purchase")
    @Severity(SeverityLevel.BLOCKER)
    void completeCheckoutTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .shouldBeOnStepOne()
                .fillInfo("John", "Doe", "12345")
                .continueToOverview()
                .shouldBeOnStepTwo()
                .finishCheckout()
                .shouldShowConfirmation();
    }

    @Test
    @DisplayName("Checkout step one shows error for missing first name")
    @Story("Checkout validates required fields")
    @Severity(SeverityLevel.NORMAL)
    void missingFirstNameValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("", "Doe", "12345")
                .continueToOverview()
                .shouldHaveError("Error: First Name is required");
    }

    @Test
    @DisplayName("Checkout step one shows error for missing last name")
    @Severity(SeverityLevel.NORMAL)
    void missingLastNameValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("John", "", "12345")
                .continueToOverview()
                .shouldHaveError("Error: Last Name is required");
    }

    @Test
    @DisplayName("Checkout step one shows error for missing postal code")
    @Severity(SeverityLevel.NORMAL)
    void missingPostalCodeValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("John", "Doe", "")
                .continueToOverview()
                .shouldHaveError("Error: Postal Code is required");
    }

    @Test
    @DisplayName("Cancel checkout returns to cart")
    @Severity(SeverityLevel.NORMAL)
    void cancelCheckoutTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .cancelCheckout()
                .shouldBeLoaded()
                .shouldHaveItems(2);
    }

    @Test
    @DisplayName("Checkout confirmation leads back to products")
    @Severity(SeverityLevel.NORMAL)
    void backToProductsAfterCheckoutTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("Jane", "Smith", "90210")
                .continueToOverview()
                .finishCheckout()
                .shouldShowConfirmation()
                .goBackHome()
                .shouldBeLoaded();
    }
}
