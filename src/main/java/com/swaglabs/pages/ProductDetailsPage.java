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
    private final SelenideElement removeBtn    = $("[data-test='remove']");
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
        addToCartBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Verify Remove button is visible after adding to cart")
    public ProductDetailsPage shouldShowRemoveButton() {
        removeBtn.shouldBe(visible);
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        SelenideElement btn = backButton.shouldBe(visible);
        // JS click — regular click doesn't trigger navigation in headless Chrome
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
}