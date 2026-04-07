package com.swaglabs.pages;
// Defines the package for page object classes, here for the Cart page

// Imports Selenide classes for web elements and collections
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

// Imports Allure annotation for step reporting
import io.qameta.allure.Step;

// Static imports from Selenide for cleaner code
import static com.codeborne.selenide.CollectionCondition.*; // for collection checks like size()
import static com.codeborne.selenide.Condition.*;           // for element conditions like visible, enabled
import static com.codeborne.selenide.Selenide.*;            // for $(), $$(), executeJavaScript(), etc.
import static com.codeborne.selenide.WebDriverConditions.urlContaining; // for URL-based conditions

public class CartPage {

    // ---------- Web Elements ----------

    private final SelenideElement pageTitle     = $("span.title");
    // Selects the cart page title element (<span class="title">Your Cart</span>)

    private final SelenideElement checkoutBtn   = $("[data-test='checkout']");
    // Selects the "Checkout" button using data-test attribute

    private final SelenideElement continueBtn   = $("[data-test='continue-shopping']");
    // Selects the "Continue Shopping" button

    private final ElementsCollection cartItems  = $$(".cart_item");
    // Selects all cart item elements on the page

    private final ElementsCollection itemNames  = $$(".inventory_item_name");
    // Selects all item name elements within the cart

    // ---------- Page Actions / Verification Methods ----------

    @Step("Verify cart page is loaded")
    // Allure annotation to log this step in the test report
    public CartPage shouldBeLoaded() {
        pageTitle.shouldBe(visible).shouldHave(text("Your Cart"));
        // Verify the page title is visible and contains text "Your Cart"
        return this;
        // Return this page object for method chaining
    }

    @Step("Verify cart has {count} item(s)")
    public CartPage shouldHaveItems(int count) {
        cartItems.shouldHave(size(count));
        // Verify the number of cart items equals count
        return this;
    }

    @Step("Verify cart is empty")
    public CartPage shouldBeEmpty() {
        cartItems.shouldHave(size(0));
        // Verify there are no items in the cart
        return this;
    }

    @Step("Verify item in cart: {itemName}")
    public CartPage shouldContainItem(String itemName) {
        itemNames.findBy(text(itemName)).shouldBe(visible);
        // Verify a specific item is present and visible in the cart
        return this;
    }

    @Step("Remove item from cart: {itemName}")
    public CartPage removeItem(String itemName) {
        SelenideElement item = cartItems.findBy(text(itemName));
        // Find the cart item by its name
        item.shouldBe(visible).scrollIntoView(true);
        // Ensure it is visible and scroll it into view

        SelenideElement removeBtn = item.$("button.cart_button, button[data-test^='remove-']");
        // Find the remove button inside this cart item (supports multiple selectors)
        removeBtn.shouldBe(visible, enabled).scrollIntoView(true);
        // Ensure remove button is visible and clickable

        removeBtn.click();
        // Click to remove the item

        cartItems.filterBy(text(itemName)).shouldHave(size(0));
        // Verify the item no longer exists in the cart
        return this;
    }

    @Step("Proceed to checkout")
    public CheckoutPage proceedToCheckout() {
        checkoutBtn.shouldBe(visible).shouldBe(enabled).click();
        // Click the checkout button if visible and enabled

        // Fallback for flaky clicks (sometimes the click may not register)
        if (webdriver().driver().url().contains("cart")) {
            executeJavaScript("arguments[0].click()", checkoutBtn.getWrappedElement());
            // Use JavaScript click as a fallback
        }

        return new CheckoutPage().shouldBeOnStepOne();
        // Navigate to CheckoutPage object and verify it is on step one
    }

    @Step("Continue shopping")
    public ProductsPage continueShopping() {
        continueBtn.shouldBe(visible, enabled).click();
        // Click the continue shopping button

        // Fallback for flaky clicks
        if (webdriver().driver().url().contains("cart")) {
            executeJavaScript("arguments[0].click()", continueBtn.getWrappedElement());
            // Use JavaScript click as a fallback
        }

        return new ProductsPage().shouldBeLoaded();
        // Navigate back to the ProductsPage and verify it loaded
    }
}