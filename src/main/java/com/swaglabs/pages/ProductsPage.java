package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

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
        SelenideElement link = productNames.findBy(text(productName)).shouldBe(visible);
        // Use JS click — headless Chrome sometimes swallows regular clicks on anchor-wrapped elements
        executeJavaScript("arguments[0].click()", link);
        webdriver().shouldHave(urlContaining("inventory-item"));
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
        executeJavaScript("document.getElementById('react-burger-menu-btn').click()");
        logoutLink.shouldBe(visible);
        executeJavaScript("document.getElementById('logout_sidebar_link').click()");
        webdriver().shouldHave(urlContaining("saucedemo.com"));
        $("[data-test='login-button']").shouldBe(visible);
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