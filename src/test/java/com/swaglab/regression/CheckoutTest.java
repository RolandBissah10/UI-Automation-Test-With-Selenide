package com.swaglab.regression;

import com.swaglab.base.BaseTest;
import com.swaglab.data.TestData;
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
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .addProductToCart(TestData.PRODUCT_BIKE_LIGHT);
    }

     @Test
    @DisplayName("Checkout step one shows error for missing first name")
    @Story("Checkout validates required fields")
    @Severity(SeverityLevel.NORMAL)
    void missingFirstNameValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("", TestData.LAST_NAME_DOE, TestData.ZIP_DEFAULT)
                .continueToOverview()
                .shouldHaveError(TestData.ERROR_FIRST_NAME_REQUIRED);
    }

     @Test
    @DisplayName("Checkout step one shows error for missing last name")
    @Severity(SeverityLevel.NORMAL)
    void missingLastNameValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, "", TestData.ZIP_DEFAULT)
                .continueToOverview()
                .shouldHaveError(TestData.ERROR_LAST_NAME_REQUIRED);
    }

     @Test
    @DisplayName("Checkout step one shows error for missing postal code")
    @Severity(SeverityLevel.NORMAL)
    void missingPostalCodeValidationTest() {
        new com.swaglabs.pages.ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, TestData.LAST_NAME_DOE, "")
                .continueToOverview()
                .shouldHaveError(TestData.ERROR_POSTAL_CODE_REQUIRED);
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

}
