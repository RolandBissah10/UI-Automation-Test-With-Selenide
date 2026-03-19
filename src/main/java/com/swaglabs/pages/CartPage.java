package com.swaglabs.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

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
}