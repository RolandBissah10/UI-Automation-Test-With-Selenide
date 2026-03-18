package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ProductDetailsPage {

    private final SelenideElement productName  = $(".inventory_details_name");
    private final SelenideElement productDesc  = $(".inventory_details_desc");
    private final SelenideElement productPrice = $(".inventory_details_price");
    // The add-to-cart button on the details page has a specific data-test like
    // "add-to-cart-sauce-labs-backpack" — use a broader selector
    private final SelenideElement addToCartBtn = $(".btn_inventory");
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

    @Step("Verify 'Remove' button is visible after adding to cart")
    public ProductDetailsPage shouldShowRemoveButton() {
        // After clicking add-to-cart the button text changes to "Remove"
        addToCartBtn.shouldBe(visible).shouldHave(text("Remove"));
        return this;
    }

    @Step("Go back to products")
    public ProductsPage goBackToProducts() {
        backButton.shouldBe(visible).click();
        // Wait for products page title to confirm navigation completed
        $("[data-test='title']").shouldBe(visible);
        return new ProductsPage();
    }

    public String getProductName() {
        return productName.getText();
    }

    public String getProductPrice() {
        return productPrice.getText();
    }
}