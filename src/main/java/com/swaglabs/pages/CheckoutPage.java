package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CheckoutPage {

    // Use span.title (the actual element) and data-test attributes throughout
    private final SelenideElement pageTitle       = $("span.title");

    // Step One — Customer Info
    // Swag Labs uses "first-name", "last-name", "postal-code" (kebab-case)
    private final SelenideElement firstNameInput  = $("[data-test='firstName']");
    private final SelenideElement lastNameInput   = $("[data-test='lastName']");
    private final SelenideElement postalCodeInput = $("[data-test='postalCode']");
    private final SelenideElement continueBtn     = $("[data-test='continue']");
    private final SelenideElement cancelBtn       = $("[data-test='cancel']");
    private final SelenideElement errorMessage    = $("[data-test='error']");

    // Step Two — Overview
    private final SelenideElement finishBtn       = $("[data-test='finish']");
    private final SelenideElement totalAmount     = $(".summary_total_label");
    private final SelenideElement itemTotal       = $(".summary_subtotal_label");

    // Confirmation
    private final SelenideElement confirmHeader   = $(".complete-header");
    private final SelenideElement confirmText     = $(".complete-text");
    private final SelenideElement backHomeBtn     = $("[data-test='back-to-products']");

    @Step("Verify checkout step one is loaded")
    public CheckoutPage shouldBeOnStepOne() {
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Your Information"));
        return this;
    }

    @Step("Fill checkout information")
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        firstNameInput.shouldBe(visible).setValue(firstName);
        lastNameInput.shouldBe(visible).setValue(lastName);
        postalCodeInput.shouldBe(visible).setValue(postalCode);
        return this;
    }

    @Step("Continue to order overview")
    public CheckoutPage continueToOverview() {
        continueBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Verify checkout overview is loaded")
    public CheckoutPage shouldBeOnStepTwo() {
        webdriver().shouldHave(urlContaining("checkout-step-two"));
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Overview"));
        return this;
    }

    @Step("Finish checkout")
    public CheckoutPage finishCheckout() {
        webdriver().shouldHave(urlContaining("checkout-step-two"));
        finishBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Verify order confirmation")
    public CheckoutPage shouldShowConfirmation() {
        webdriver().shouldHave(urlContaining("checkout-complete"));
        confirmHeader.shouldBe(visible).shouldHave(text("Thank you for your order!"));
        confirmText.shouldBe(visible);
        return this;
    }

    @Step("Verify error: {expectedError}")
    public CheckoutPage shouldHaveError(String expectedError) {
        // Error stays on step-one page — assert URL hasn't changed
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        errorMessage.shouldBe(visible).shouldHave(text(expectedError));
        return this;
    }

    @Step("Cancel checkout — returns to cart")
    public CartPage cancelCheckout() {
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        cancelBtn.shouldBe(visible).click();
        return new CartPage();
    }

    @Step("Go back home after order")
    public ProductsPage goBackHome() {
        backHomeBtn.shouldBe(visible).click();
        return new ProductsPage();
    }

    public String getTotalAmount() {
        return totalAmount.shouldBe(visible).getText();
    }

    public String getItemTotal() {
        return itemTotal.shouldBe(visible).getText();
    }
}