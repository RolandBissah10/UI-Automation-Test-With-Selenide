package com.swaglabs.pages;
// Defines the package where this page object resides (page classes)

import com.codeborne.selenide.SelenideElement;
// Import SelenideElement to represent web elements
import io.qameta.allure.Step;
// Import Allure @Step annotation for reporting steps

// Static imports for Selenide conditions, selectors, and URL checks
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProductDetailsPage {

    // ---------- Page elements ----------
    private final SelenideElement productName  = $("[data-test='inventory-item-name']");
    // Product name on details page
    private final SelenideElement productDesc  = $("[data-test='inventory-item-desc']");
    // Product description
    private final SelenideElement productPrice = $("[data-test='inventory-item-price']");
    // Product price
    private final SelenideElement addToCartBtn = $("[data-test='add-to-cart']");
    // Add to Cart button
    private final SelenideElement removeBtn    = $("[data-test^='remove']");
    // Remove button (uses prefix selector to match any product-specific remove button)
    private final SelenideElement backButton   = $("[data-test='back-to-products']");
    // Button to go back to product listing

    // ---------- Verification / Actions ----------

    @Step("Verify product details page is loaded")
    public ProductDetailsPage shouldBeLoaded() {
        productName.shouldBe(visible); // Check product name is visible
        productDesc.shouldBe(visible); // Check product description is visible
        productPrice.shouldBe(visible); // Check product price is visible
        return this; // Return current page object for method chaining
    }

    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        addToCartBtn.shouldBe(visible); // Ensure Add to Cart button is visible
        executeJavaScript("arguments[0].click()", addToCartBtn);
        // Use JS click to handle flaky clicks in CI or headless mode
        return this;
    }

    @Step("Verify Remove button is visible after adding to cart")
    public ProductDetailsPage shouldShowRemoveButton() {
        removeBtn.shouldBe(visible);
        // After adding to cart, a remove button should appear
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        SelenideElement btn = backButton.shouldBe(visible);
        // Ensure back button is visible
        executeJavaScript("arguments[0].click()", btn);
        // Click back button using JS to avoid flaky clicks
        webdriver().shouldHave(urlContaining("inventory.html"));
        // Verify the URL contains "inventory.html" indicating the product list page
        return new ProductsPage();
        // Return new instance of ProductsPage for further interactions
    }


    @Step("Verify product name is: {expectedName}")
    public ProductDetailsPage shouldShowProductName(String expectedName) {
        productName.shouldBe(visible).shouldHave(text(expectedName));
        // Verify product name matches expected
        return this;
    }


}