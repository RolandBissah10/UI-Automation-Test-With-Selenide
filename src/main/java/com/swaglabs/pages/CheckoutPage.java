package com.swaglabs.pages;
// Defines the package where this page object resides (for page classes)

import com.codeborne.selenide.SelenideElement;
// Import SelenideElement to represent individual web elements
import io.qameta.allure.Step;
// Import Allure @Step annotation for reporting test steps

// Static imports for Selenide conditions, element selectors, and WebDriver URL checks
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CheckoutPage {

    // ---------- Page elements ----------

    private final SelenideElement pageTitle       = $("span.title");
    // The page title element on checkout pages

    private final SelenideElement firstNameInput  = $("[data-test='firstName']");
    private final SelenideElement lastNameInput   = $("[data-test='lastName']");
    private final SelenideElement postalCodeInput = $("[data-test='postalCode']");
    // Input fields for user information in step one of checkout

    private final SelenideElement continueBtn     = $("[data-test='continue']");
    private final SelenideElement cancelBtn       = $("[data-test='cancel']");
    private final SelenideElement errorMessage    = $("[data-test='error']");
    // Buttons to continue, cancel, and error message display

    private final SelenideElement finishBtn       = $("[data-test='finish']");
    private final SelenideElement totalAmount     = $(".summary_total_label");
    private final SelenideElement itemTotal       = $(".summary_subtotal_label");
    // Step two elements: finish button, total amount, and item subtotal labels

    private final SelenideElement confirmHeader   = $(".complete-header");
    private final SelenideElement confirmText     = $(".complete-text");
    private final SelenideElement backHomeBtn     = $("[data-test='back-to-products']");
    // Step three elements: confirmation header, text, and back-to-products button

    // ---------- Actions / Verification Methods ----------

    @Step("Verify checkout step one is loaded")
    public CheckoutPage shouldBeOnStepOne() {
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        // Verify current URL contains "checkout-step-one"
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Your Information"));
        // Ensure the title is visible and has expected text
        return this; // return this page object for method chaining
    }

    @Step("Fill checkout information")
    public CheckoutPage fillInfo(String firstName, String lastName, String postalCode) {
        fillField(firstNameInput, firstName); // Fill first name
        fillField(lastNameInput, lastName);   // Fill last name
        fillField(postalCodeInput, postalCode); // Fill postal code
        return this;
    }

    private CheckoutPage fillField(SelenideElement input, String value) {
        String normalizedValue = value == null ? "" : value;
        // Replace null values with empty string
        input.shouldBe(visible, enabled) // Make sure input is visible and editable
                .scrollIntoView(true) // Scroll element into view
                .click();
        input.clear(); // Clear existing text
        input.setValue(normalizedValue); // Set the new value

        // Fallback for inputs where setValue may fail (dispatch JS input event)
        if (!normalizedValue.equals(input.getValue())) {
            executeJavaScript(
                    "arguments[0].value = arguments[1]; " +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    input, normalizedValue
            );
        }
        input.shouldHave(value(normalizedValue));
        // Verify input now contains the expected value
        return this;
    }

    @Step("Continue to order overview")
    public CheckoutPage continueToOverview() {
        continueBtn.shouldBe(visible).shouldBe(enabled).click();
        // Click continue button
        sleep(500); // Small wait for page load

        // Fallback in case click didn't work or page didn’t navigate
        if (webdriver().driver().url().contains("checkout-step-one") && !errorMessage.is(visible)) {
            executeJavaScript("arguments[0].click()", continueBtn.getWrappedElement());
        }
        return this;
    }

    @Step("Attempt to continue to overview (expecting failure)")
    public CheckoutPage continueToOverviewExpectingFailure() {
        continueBtn.shouldBe(visible).click();
        // Attempt click without filling required info
        errorMessage.shouldBe(visible);
        // Verify error message is displayed
        return this;
    }

    @Step("Verify checkout overview is loaded")
    public CheckoutPage shouldBeOnStepTwo() {
        webdriver().shouldHave(urlContaining("checkout-step-two"));
        // Verify URL for step two
        pageTitle.shouldBe(visible).shouldHave(text("Checkout: Overview"));
        // Verify page title text
        return this;
    }

    @Step("Verify error: {expectedError}")
    public CheckoutPage shouldHaveError(String expectedError) {
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        // Ensure still on step one
        errorMessage.shouldBe(visible).shouldHave(text(expectedError));
        // Verify error text matches expected
        return this;
    }

    @Step("Cancel checkout — returns to cart")
    public CartPage cancelCheckout() {
        cancelBtn.shouldBe(visible).click();
        // Click cancel button
        sleep(500); // Wait for navigation

        // Fallback for flaky cancel button
        if (webdriver().driver().url().contains("checkout-step-one")) {
            executeJavaScript("arguments[0].click()", cancelBtn.getWrappedElement());
        }
        return new CartPage().shouldBeLoaded();
        // Return CartPage object to continue interactions
    }

    @Step("Attempt to cancel checkout (expecting failure)")
    public CheckoutPage cancelCheckoutExpectingFailure() {
        cancelBtn.shouldBe(visible).click();
        // Click cancel
        webdriver().shouldHave(urlContaining("checkout-step-one"));
        // Verify still on checkout step one (cancel didn’t work)
        return this;
    }
}