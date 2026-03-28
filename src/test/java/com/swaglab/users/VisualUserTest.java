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
 * Test suite for visual_user:
 * User who can login but experiences visual rendering differences.
 * Tests visual elements, styling, layout, and UI consistency.
 */
@Tag("user-visual")
@Tag("visual-regression")
@Epic("User Scenarios")
@Feature("Visual User")
@Story("Visual user experiences UI rendering differences")
public class VisualUserTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void loginAsVisualUser() {
        productsPage = new LoginPage()
                .shouldBeLoaded()
                .loginAs(CONFIG.visualUser(), CONFIG.password());
    }

    @Test
    @DisplayName("Visual user can login successfully")
    @Severity(SeverityLevel.BLOCKER)
    void visualUserCanLogin() {
        productsPage.shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Visual user can view all products")
    @Severity(SeverityLevel.CRITICAL)
    void visualUserCanViewAllProducts() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductCount(6);
    }

    @Test
    @DisplayName("Visual user product page displays all elements")
    @Severity(SeverityLevel.NORMAL)
    void visualUserProductPageHasAllElements() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveProductImages()
                .shouldHaveProductNames()
                .shouldHaveProductPrices()
                .shouldHaveAddToCartButtons();
    }

    @Test
    @DisplayName("Visual user can access product details page")
    @Severity(SeverityLevel.NORMAL)
    void visualUserCanViewProductDetails() {
        productsPage
                .openProduct(TestData.PRODUCT_BACKPACK)
                .shouldBeLoaded()
                .shouldShowProductName(TestData.PRODUCT_BACKPACK)
                .shouldShowProductDescription();
    }

    @Test
    @DisplayName("Visual user product details page layout is correct")
    @Severity(SeverityLevel.NORMAL)
    void visualUserProductDetailsLayoutCorrect() {
        productsPage
                .openProduct(TestData.PRODUCT_BACKPACK)
                .shouldHaveProductImage()
                .shouldHaveProductPrice()
                .shouldHaveAddToCartButton()
                .shouldHaveBackButton();
    }

    @Test
    @DisplayName("Visual user can add products to cart")
    @Severity(SeverityLevel.CRITICAL)
    void visualUserCanAddProductsToCart() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .addProductToCart(TestData.PRODUCT_BIKE_LIGHT)
                .shouldHaveCartCount(2);
    }

    @Test
    @DisplayName("Visual user cart badge displays correctly")
    @Severity(SeverityLevel.NORMAL)
    void visualUserCartBadgeDisplaysCorrectly() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .shouldHaveCartBadge()
                .shouldShowCartCountAs(1);
    }

    @Test
    @DisplayName("Visual user cart page layout is correct")
    @Severity(SeverityLevel.NORMAL)
    void visualUserCartPageLayoutCorrect() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .shouldHaveCartHeader()
                .shouldHaveCartItems()
                .shouldHaveContinueShoppingButton()
                .shouldHaveCheckoutButton();
    }

    @Test
    @DisplayName("Visual user checkout pages display all labels correctly")
    @Severity(SeverityLevel.NORMAL)
    void visualUserCheckoutLabelsDisplayCorrectly() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .proceedToCheckout()
                .shouldShowFirstNameLabel()
                .shouldShowLastNameLabel()
                .shouldShowPostalCodeLabel()
                .shouldHaveContinueButton();
    }

    @Test
    @DisplayName("Visual user can complete full checkout flow")
    @Severity(SeverityLevel.BLOCKER)
    void visualUserCanCheckout() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JOHN, TestData.LAST_NAME_DOE, TestData.ZIP_DEFAULT)
                .continueToOverview()
                .shouldBeOnStepTwo()
                .shouldShowItemSummary()
                .shouldShowOrderTotal()
                .finishCheckout()
                .shouldShowConfirmation();
    }

    @Test
    @DisplayName("Visual user confirmation page displays correctly")
    @Severity(SeverityLevel.NORMAL)
    void visualUserConfirmationPageDisplaysCorrectly() {
        productsPage
                .addProductToCart(TestData.PRODUCT_BACKPACK)
                .openCart()
                .proceedToCheckout()
                .fillInfo(TestData.FIRST_NAME_JANE, TestData.LAST_NAME_SMITH, "67890")
                .continueToOverview()
                .finishCheckout()
                .shouldShowConfirmation()
                .shouldShowThankyouMessage()
                .shouldHaveBackHomeButton();
    }

    @Test
    @DisplayName("Visual user menu displays correctly")
    @Severity(SeverityLevel.NORMAL)
    void visualUserMenuDisplaysCorrectly() {
        productsPage
                .openMenu()
                .shouldShowAllItems()
                .shouldShowAbout()
                .shouldShowLogout()
                .shouldShowResetButton();
    }

    @Test
    @DisplayName("Visual user logout")
    @Severity(SeverityLevel.CRITICAL)
    void visualUserCanLogout() {
        productsPage
                .openMenu()
                .clickLogout();
        new LoginPage().shouldBeLoaded();
    }

    @Test
    @DisplayName("Visual user can sort products with dropdown visible")
    @Severity(SeverityLevel.NORMAL)
    void visualUserSortDropdownDisplaysCorrectly() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveSortDropdown()
                .shouldShowAllSortOptions();
    }

    @Test
    @DisplayName("Visual user footer displays correctly if present")
    @Severity(SeverityLevel.MINOR)
    void visualUserFooterDisplaysCorrectly() {
        productsPage
                .shouldBeLoaded()
                .shouldHaveFooter();
    }
}
