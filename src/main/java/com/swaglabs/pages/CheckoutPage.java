package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

public class CheckoutPage {

    // Step One - Customer Info
    private final SelenideElement pageTitle       = $(".title");
    private final SelenideElement firstNameInput  = $("[data-test='firstName']");
    private final SelenideElement lastNameInput   = $("[data-test='lastName']");
    private final SelenideElement postalCodeInput = $("[data-test='postalCode']");
    private final SelenideElement continueBtn     = $("[data-test='continue']");
    private final SelenideElement cancelBtn       = $("[data-test='cancel']");
    private final SelenideElement errorMessage    = $("[data-test='error']");

    // Step Two - Overview
    private final SelenideElement finishBtn       = $("[data-test='finish']");
    private final SelenideElement itemTotal       = $(".summary_subtotal_label");
    private final SelenideElement taxAmount       = $(".summary_tax_label");
    private final SelenideElement totalAmount     = $(".summary_total_label");

    // Confirmation
    private final SelenideElement confirmHeader   = $(".complete-header");
    private final SelenideElement confirmText     = $(".complete-text");
    private final SelenideElement backHomeBtn     = $("[data-test='back-to-products']");

    @Step("Verify checkout step one is loaded")
    public CheckoutPage shouldBeOnStepOne() {
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Your Information"));
        return this;
    }

    @Step("Fill checkout information")
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        firstNameInput.shouldBe(visible).setValue(firstName);
        lastNameInput.setValue(lastName);
        postalCodeInput.setValue(postalCode);
        return this;
    }

    @Step("Continue to order overview")
    public CheckoutPage continueToOverview() {
        continueBtn.click();
        return this;
    }

    @Step("Verify checkout overview is loaded")
    public CheckoutPage shouldBeOnStepTwo() {
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Overview"));
        return this;
    }

    @Step("Finish checkout")
    public CheckoutPage finishCheckout() {
        finishBtn.shouldBe(visible).click();
        return this;
    }

    @Step("Verify order confirmation")
    public CheckoutPage shouldShowConfirmation() {
        confirmHeader.shouldBe(visible).shouldHave(text("Thank you for your order!"));
        confirmText.shouldBe(visible);
        return this;
    }

    @Step("Verify error: {expectedError}")
    public CheckoutPage shouldHaveError(String expectedError) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedError));
        return this;
    }

    @Step("Go back home")
    public ProductsPage goBackHome() {
        backHomeBtn.click();
        return new ProductsPage();
    }

    @Step("Cancel checkout")
    public CartPage cancelCheckout() {
        cancelBtn.click();
        return new CartPage();
    }

    public String getTotalAmount() {
        return totalAmount.shouldBe(visible).getText();
    }

    public String getItemTotal() {
        return itemTotal.shouldBe(visible).getText();
    }
}
