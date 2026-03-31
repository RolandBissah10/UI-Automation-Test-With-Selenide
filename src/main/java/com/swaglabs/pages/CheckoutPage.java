package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CheckoutPage {

    private final SelenideElement pageTitle       = $("span.title");

    private final SelenideElement firstNameInput  = $("[data-test='firstName']");
    private final SelenideElement lastNameInput   = $("[data-test='lastName']");
    private final SelenideElement postalCodeInput = $("[data-test='postalCode']");
    private final SelenideElement continueBtn     = $("[data-test='continue']");
    private final SelenideElement cancelBtn       = $("[data-test='cancel']");
    private final SelenideElement errorMessage    = $("[data-test='error']");

    private final SelenideElement finishBtn       = $("[data-test='finish']");
    private final SelenideElement totalAmount     = $(".summary_total_label");
    private final SelenideElement itemTotal       = $(".summary_subtotal_label");

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
        fillField(firstNameInput, firstName);
        fillField(lastNameInput, lastName);
        fillField(postalCodeInput, postalCode);
        return this;
    }

    private CheckoutPage fillField(SelenideElement input, String value) {
        String normalizedValue = value == null ? "" : value;
        input.shouldBe(visible, enabled)
                .scrollIntoView(true)
                .click();
        input.clear();
        input.setValue(normalizedValue);
        input.shouldHave(value(normalizedValue));
        return this;
    }

    @Step("Continue to order overview")
    public CheckoutPage continueToOverview() {
        continueBtn.shouldBe(visible).shouldBe(enabled).click();
        sleep(500);
        if (webdriver().driver().url().contains("checkout-step-one") && !errorMessage.is(visible)) {
            executeJavaScript("arguments[0].click()", continueBtn.getWrappedElement());
        }
        return this;
    }

    @Step("Attempt to continue to overview (expecting failure)")
    public CheckoutPage continueToOverviewExpectingFailure() {
        continueBtn.shouldBe(visible).click();
        errorMessage.shouldBe(visible);
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
        finishBtn.shouldBe(visible).shouldBe(enabled).click();
        if (webdriver().driver().url().contains("checkout-step-two")) {
            sleep(500);
            executeJavaScript("arguments[0].click()", finishBtn.getWrappedElement());
        }
        return this;
    }

    @Step("Verify order confirmation")
    public CheckoutPage shouldShowConfirmation() {
        webdriver().shouldHave(urlContaining("checkout-complete"));
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Complete!"));
        confirmHeader.shouldBe(visible).shouldHave(text("Thank you for your order!"));
        confirmText.shouldBe(visible);
        return this;
    }

    @Step("Verify error: {expectedError}")
    public CheckoutPage shouldHaveError(String expectedError) {
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        errorMessage.shouldBe(visible).shouldHave(text(expectedError));
        return this;
    }

    @Step("Verify checkout page has an error message")
    public CheckoutPage shouldHaveCheckoutError() {
        errorMessage.shouldBe(visible);
        return this;
    }

    @Step("Verify checkout error is displayed")
    public CheckoutPage shouldDisplayError() {
        errorMessage.shouldBe(visible);
        return this;
    }

    @Step("Cancel checkout — returns to cart")
    public CartPage cancelCheckout() {
        cancelBtn.shouldBe(visible).click();
        sleep(500);
        if (webdriver().driver().url().contains("checkout-step-one")) {
            executeJavaScript("arguments[0].click()", cancelBtn.getWrappedElement());
        }
        return new CartPage().shouldBeLoaded();
    }

    @Step("Attempt to cancel checkout (expecting failure)")
    public CheckoutPage cancelCheckoutExpectingFailure() {
        cancelBtn.shouldBe(visible).click();
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        return this;
    }

    @Step("Go back to cart from checkout")
    public CartPage goBackToCart() {
        return cancelCheckout();
    }

    @Step("Attempt to go back to cart from checkout (expecting failure)")
    public CheckoutPage goBackToCartExpectingFailure() {
        return cancelCheckoutExpectingFailure();
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

    @Step("Verify order total is visible")
    public CheckoutPage verifyOrderTotalVisible() {
        totalAmount.shouldBe(visible);
        return this;
    }

    @Step("Verify order complete message: {expectedMessage}")
    public CheckoutPage verifyOrderCompleteMessage(String expectedMessage) {
        confirmHeader.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Verify First Name label is visible")
    public CheckoutPage shouldShowFirstNameLabel() {
        firstNameInput.shouldBe(visible);
        return this;
    }

    @Step("Verify Last Name label is visible")
    public CheckoutPage shouldShowLastNameLabel() {
        lastNameInput.shouldBe(visible);
        return this;
    }

    @Step("Verify Postal Code label is visible")
    public CheckoutPage shouldShowPostalCodeLabel() {
        postalCodeInput.shouldBe(visible);
        return this;
    }

    @Step("Verify Continue button is visible")
    public CheckoutPage shouldHaveContinueButton() {
        continueBtn.shouldBe(visible);
        return this;
    }

    @Step("Verify item summary is visible on overview page")
    public CheckoutPage shouldShowItemSummary() {
        itemTotal.shouldBe(visible);
        return this;
    }

    @Step("Verify order total is visible on overview page")
    public CheckoutPage shouldShowOrderTotal() {
        totalAmount.shouldBe(visible);
        return this;
    }

    @Step("Verify thank you message is displayed")
    public CheckoutPage shouldShowThankyouMessage() {
        confirmHeader.shouldBe(visible).shouldHave(text("Thank you for your order!"));
        return this;
    }

    @Step("Verify Back Home button is visible")
    public CheckoutPage shouldHaveBackHomeButton() {
        backHomeBtn.shouldBe(visible);
        return this;
    }
}