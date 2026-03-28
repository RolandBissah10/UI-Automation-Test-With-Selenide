package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CartPage {

    private final SelenideElement pageTitle     = $("span.title");
    private final SelenideElement checkoutBtn   = $("[data-test='checkout']");
    private final SelenideElement continueBtn   = $("[data-test='continue-shopping']");
    private final ElementsCollection cartItems  = $$(".cart_item");
    private final ElementsCollection itemNames  = $$(".inventory_item_name");

    @Step("Verify cart page is loaded")
    public CartPage shouldBeLoaded() {
        pageTitle.shouldBe(visible).shouldHave(text("Your Cart"));
        return this;
    }

    @Step("Verify cart has {count} item(s)")
    public CartPage shouldHaveItems(int count) {
        cartItems.shouldHave(size(count));
        return this;
    }

    @Step("Verify cart is empty")
    public CartPage shouldBeEmpty() {
        cartItems.shouldHave(size(0));
        return this;
    }

    @Step("Verify item in cart: {itemName}")
    public CartPage shouldContainItem(String itemName) {
        itemNames.findBy(text(itemName)).shouldBe(visible);
        return this;
    }


    @Step("Remove item from cart: {itemName}")
    public CartPage removeItem(String itemName) {
        String dataTest = "remove-" + itemName.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
        $("[data-test='" + dataTest + "']").shouldBe(visible).click();
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        checkoutBtn.shouldBe(visible).click();
        // Fallback for flaky checkout button
        if (webdriver().driver().url().contains("cart")) {
            executeJavaScript("arguments[0].click()", checkoutBtn);
        }
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        return new CheckoutPage();
    }

    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        continueBtn.click();
        return new ProductsPage();
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    @Step("Verify cart page header is visible")
    public CartPage shouldHaveCartHeader() {
        pageTitle.shouldBe(visible).shouldHave(text("Your Cart"));
        return this;
    }

    @Step("Verify cart has items")
    public CartPage shouldHaveCartItems() {
        cartItems.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify Continue Shopping button is visible")
    public CartPage shouldHaveContinueShoppingButton() {
        continueBtn.shouldBe(visible);
        return this;
    }

    @Step("Verify Checkout button is visible")
    public CartPage shouldHaveCheckoutButton() {
        checkoutBtn.shouldBe(visible);
        return this;
    }
}