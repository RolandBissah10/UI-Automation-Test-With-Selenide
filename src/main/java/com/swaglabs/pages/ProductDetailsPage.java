package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ProductDetailsPage {

    private final SelenideElement productName    = $(".inventory_details_name");
    private final SelenideElement productDesc    = $(".inventory_details_desc");
    private final SelenideElement productPrice   = $(".inventory_details_price");
    private final SelenideElement addToCartBtn   = $("[data-test^='add-to-cart']");
    private final SelenideElement removeBtn      = $("[data-test^='remove']");
    private final SelenideElement backButton     = $("[data-test='back-to-products']");

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

    @Step("Verify 'Remove' button is visible")
    public ProductDetailsPage shouldShowRemoveButton() {
        removeBtn.shouldBe(visible);
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        backButton.click();
        return new ProductsPage();
    }

    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }
}
