package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProductDetailsPage {

    // data-test attributes on details page
    private final SelenideElement productName  = $("[data-test='inventory-item-name']");
    private final SelenideElement productDesc  = $("[data-test='inventory-item-desc']");
    private final SelenideElement productPrice = $("[data-test='inventory-item-price']");
    // On the details page the button has data-test="add-to-cart" (no slug suffix)
    private final SelenideElement addToCartBtn = $("[data-test='add-to-cart']");
    private final SelenideElement removeBtn    = $("[data-test='remove']");
    private final SelenideElement backButton   = $("[data-test='back-to-products']");

    @Step("Verify product details page is loaded")
    public ProductDetailsPage shouldBeLoaded() {
        webdriver().shouldHave(urlContaining("inventory-item"));
        productName.shouldBe(visible);
        productDesc.shouldBe(visible);
        productPrice.shouldBe(visible);
        return this;
    }

    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        addToCartBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Verify 'Remove' button is visible after adding to cart")
    public ProductDetailsPage shouldShowRemoveButton() {
        // After clicking add-to-cart, Swag Labs replaces the button with
        // a separate Remove button (data-test="remove"), not the same element
        removeBtn.shouldBe(visible);
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        backButton.shouldBe(visible).click();
        webdriver().shouldHave(urlContaining("inventory.html"));
        return new ProductsPage();
    }

    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }
}