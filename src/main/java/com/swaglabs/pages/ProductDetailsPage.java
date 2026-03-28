package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProductDetailsPage {

    private final SelenideElement productName  = $("[data-test='inventory-item-name']");
    private final SelenideElement productDesc  = $("[data-test='inventory-item-desc']");
    private final SelenideElement productPrice = $("[data-test='inventory-item-price']");
    private final SelenideElement addToCartBtn = $("[data-test='add-to-cart']");
    // Remove button on details page uses same slug pattern: remove-sauce-labs-backpack
    // Use prefix selector to match any remove button regardless of product slug
    private final SelenideElement removeBtn    = $("[data-test^='remove']");
    private final SelenideElement backButton   = $("[data-test='back-to-products']");

    @Step("Verify product details page is loaded")
    public ProductDetailsPage shouldBeLoaded() {
        productName.shouldBe(visible);
        productDesc.shouldBe(visible);
        productPrice.shouldBe(visible);
        return this;
    }

    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        addToCartBtn.shouldBe(visible);
        executeJavaScript("arguments[0].click()", addToCartBtn);
        return this;
    }

    @Step("Verify Remove button is visible after adding to cart")
    public ProductDetailsPage shouldShowRemoveButton() {
        // After add-to-cart, a remove button with data-test starting with 'remove' appears
        removeBtn.shouldBe(visible);
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        SelenideElement btn = backButton.shouldBe(visible);
        executeJavaScript("arguments[0].click()", btn);
        webdriver().shouldHave(urlContaining("inventory.html"));
        return new ProductsPage();
    }

    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }

    @Step("Verify product name is: {expectedName}")
    public ProductDetailsPage shouldShowProductName(String expectedName) {
        productName.shouldBe(visible).shouldHave(text(expectedName));
        return this;
    }

    @Step("Verify product description is visible")
    public ProductDetailsPage shouldShowProductDescription() {
        productDesc.shouldBe(visible);
        return this;
    }

    @Step("Verify product image is visible")
    public ProductDetailsPage shouldHaveProductImage() {
        $(".inventory_details_img").shouldBe(visible);
        return this;
    }

    @Step("Verify product image is broken")
    public ProductDetailsPage shouldHaveProductImageBroken() {
        SelenideElement img = $(".inventory_details_img");
        img.shouldBe(exist);
        return this;
    }

    @Step("Verify product price is visible")
    public ProductDetailsPage shouldHaveProductPrice() {
        productPrice.shouldBe(visible);
        return this;
    }

    @Step("Verify Add to Cart button is visible")
    public ProductDetailsPage shouldHaveAddToCartButton() {
        addToCartBtn.shouldBe(visible);
        return this;
    }

    @Step("Verify Back to Products button is visible")
    public ProductDetailsPage shouldHaveBackButton() {
        backButton.shouldBe(visible);
        return this;
    }
}