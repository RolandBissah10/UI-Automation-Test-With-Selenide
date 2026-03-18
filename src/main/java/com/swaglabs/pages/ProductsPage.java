package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProductsPage {

    private final SelenideElement pageTitle        = $("span.title");
    private final SelenideElement menuButton       = $("#react-burger-menu-btn");
    private final SelenideElement logoutLink       = $("#logout_sidebar_link");
    private final SelenideElement sortDropdown     = $("[data-test='product-sort-container']");
    private final SelenideElement cartIcon         = $(".shopping_cart_link");
    private final SelenideElement cartBadge        = $(".shopping_cart_badge");
    private final ElementsCollection productCards  = $$(".inventory_item");
    private final ElementsCollection productNames  = $$(".inventory_item_name");
    private final ElementsCollection addToCartBtns = $$("[data-test^='add-to-cart']");

    @Step("Verify products page is loaded")
    public ProductsPage shouldBeLoaded() {
        pageTitle.shouldBe(visible).shouldHave(text("Products"));
        productCards.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Add product to cart: {productName}")
    public ProductsPage addProductToCart(String productName) {
        String dataTest = "add-to-cart-" + productName.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        $("[data-test='" + dataTest + "']").shouldBe(visible).click();
        return this;
    }

    @Step("Add first product to cart")
    public ProductsPage addFirstProductToCart() {
        addToCartBtns.first().shouldBe(visible).click();
        return this;
    }

    @Step("Open product details: {productName}")
    public ProductDetailsPage openProduct(String productName) {
        productNames.findBy(text(productName)).click();
        return new ProductDetailsPage();
    }

    @Step("Sort products by: {sortOption}")
    public ProductsPage sortBy(String sortOption) {
        sortDropdown.selectOptionContainingText(sortOption);
        return this;
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartIcon.click();
        return new CartPage();
    }

    @Step("Logout")
    public LoginPage logout() {
        // Use JavaScript click — the burger menu button has pointer-events
        // issues in headless Chrome that prevent normal Selenide clicks
        executeJavaScript("document.getElementById('react-burger-menu-btn').click()");
        // Wait for the logout link to become visible in the DOM (not aria-based)
        logoutLink.shouldBe(visible);
        logoutLink.click();
        return new LoginPage();
    }

    @Step("Verify cart badge shows {count} item(s)")
    public ProductsPage shouldHaveCartCount(int count) {
        cartBadge.shouldBe(visible).shouldHave(text(String.valueOf(count)));
        return this;
    }

    @Step("Verify product count is {count}")
    public ProductsPage shouldHaveProductCount(int count) {
        productCards.shouldHave(size(count));
        return this;
    }

    public int getProductCount() {
        return productCards.size();
    }

    public String getFirstProductName() {
        return productNames.first().getText();
    }
}