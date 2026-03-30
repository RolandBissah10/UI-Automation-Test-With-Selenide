package com.swaglab.regression;

import com.swaglab.base.BaseTest;
import com.swaglab.data.TestData;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

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
    void missingFirstNameValidationTest() {
        new ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo("", TestData.LAST_NAME_DOE, TestData.ZIP_DEFAULT)
                .continueToOverviewExpectingFailure()
                .shouldHaveError(TestData.ERROR_FIRST_NAME_REQUIRED);
    }

    @Test
    @DisplayName("Checkout step one shows error for missing last name")
    void missingLastNameValidationTest() {
        new ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, "", TestData.ZIP_DEFAULT)
                .continueToOverviewExpectingFailure()
                .shouldHaveError(TestData.ERROR_LAST_NAME_REQUIRED);
    }

    @Test
    @DisplayName("Checkout step one shows error for missing postal code")
    void missingPostalCodeValidationTest() {
        new ProductsPage()
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, TestData.LAST_NAME_DOE, "")
                .continueToOverviewExpectingFailure()
                .shouldHaveError(TestData.ERROR_POSTAL_CODE_REQUIRED);
    }

    @Test
    @DisplayName("Cancel checkout returns to cart")
    void cancelCheckoutTest() {
        new ProductsPage()
                .openCart()
                .proceedToCheckout()
                .cancelCheckout()
                .shouldBeLoaded()
                .shouldHaveItems(2);
    }
}