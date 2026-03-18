package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CartPage {

    private final SelenideElement pageTitle      = $(".title");
    private final SelenideElement checkoutBtn    = $("[data-test='checkout']");
    private final SelenideElement continueBtn    = $("[data-test='continue-shopping']");
    private final ElementsCollection cartItems   = $$(".cart_item");
    private final ElementsCollection itemNames   = $$(".inventory_item_name");
    private final ElementsCollection removeBtns  = $$("[data-test^='remove']");

    public CartPage shouldBeLoaded() {
        pageTitle.shouldBe(visible).shouldHave(text("Your Cart"));
        return this;
    }

    public CartPage shouldHaveItems(int count) {
        cartItems.shouldHave(size(count));
        return this;
    }

    public CartPage shouldBeEmpty() {
        cartItems.shouldHave(size(0));
        return this;
    }

    public CartPage shouldContainItem(String itemName) {
        itemNames.findBy(text(itemName)).shouldBe(visible);
        return this;
    }

    public CartPage removeItem(String itemName) {
        itemNames.findBy(text(itemName))
                 .parent().parent()
                 .$("[data-test^='remove']")
                 .click();
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        checkoutBtn.shouldBe(visible).click();
        return new CheckoutPage();
    }

    public ProductsPage continueShopping() {
        continueBtn.click();
        return new ProductsPage();
    }

    public int getCartItemCount() {
        return cartItems.size();
    }
}
