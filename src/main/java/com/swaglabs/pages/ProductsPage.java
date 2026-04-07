package com.swaglabs.pages;
// Defines the package where this page object class resides (page classes)

import com.codeborne.selenide.ElementsCollection;
// Import Selenide ElementsCollection to represent lists of web elements
import com.codeborne.selenide.SelenideElement;
// Import SelenideElement to represent single web elements
import io.qameta.allure.Step;
// Import Allure @Step annotation to log steps in reports

// Static imports for Selenide conditions, selectors, and URL checks
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class ProductsPage {

    // ---------- Page elements ----------

    private final SelenideElement pageTitle        = $("span.title");
    // Page title element showing "Products"

    private final SelenideElement logoutLink       = $("#logout_sidebar_link");
    // Logout link in hamburger menu

    private final SelenideElement sortDropdown     = $("[data-test='product-sort-container']");
    // Dropdown for sorting products

    private final SelenideElement cartIcon         = $(".shopping_cart_link");
    // Shopping cart icon

    private final SelenideElement cartBadge        = cartIcon.$("[data-test='shopping-cart-badge'], .shopping_cart_badge");
    // Badge showing number of items in the cart

    private final SelenideElement footer           = $("footer");
    // Footer element of the page

    private final ElementsCollection productCards  = $$(".inventory_item");
    // Collection of product cards displayed on the page

    private final ElementsCollection productNames  = $$(".inventory_item_name");
    // Collection of product names

    private final ElementsCollection productImages = $$(".inventory_item_img img");
    // Collection of product images

    private final ElementsCollection productPrices = $$(".inventory_item_price");
    // Collection of product prices

    private final ElementsCollection addToCartBtns = $$("[data-test^='add-to-cart']");
    // Collection of all "Add to Cart" buttons

    // ---------- Verification / Actions ----------

    @Step("Verify products page is loaded")
    public ProductsPage shouldBeLoaded() {
        pageTitle.shouldBe(visible).shouldHave(text("Products"));
        // Verify page title is visible and shows "Products"
        productCards.shouldHave(sizeGreaterThan(0));
        // Ensure there is at least 1 product on the page
        return this; // Return current page object for method chaining
    }

    @Step("Add product to cart: {productName}")
    public ProductsPage addProductToCart(String productName) {
        SelenideElement productCard = productCards.findBy(text(productName));
        // Find the product card by product name
        productCard.shouldBe(visible).scrollIntoView(true);
        // Make sure it is visible and scroll into view

        SelenideElement actionButton = productCard.$("button[data-test^='add-to-cart'], button[data-test^='remove-']");
        // Locate the action button (either add-to-cart or remove)
        actionButton.shouldBe(visible, enabled);
        // Ensure button is visible and enabled

        String dataTest = actionButton.getAttribute("data-test");
        if (dataTest != null && dataTest.startsWith("add-to-cart")) {
            actionButton.click();
            actionButton.shouldHave(text("Remove"));
            // If it was "Add to Cart", click and verify it changes to "Remove"
        } else {
            actionButton.shouldHave(text("Remove"));
            // If already added, verify the button shows "Remove"
        }

        cartIcon.shouldBe(visible);
        // Ensure the cart icon is visible
        cartBadge.shouldBe(visible);
        // Ensure the cart badge is visible
        return this;
    }

    @Step("Open product details: {productName}")
    public ProductDetailsPage openProduct(String productName) {
        SelenideElement link = productNames.findBy(text(productName)).shouldBe(visible);
        // Find the product name element
        link.click();
        // Click to open product details
        sleep(500);
        // Wait for page transition
        if (!webdriver().driver().url().contains("inventory-item")) {
            executeJavaScript("arguments[0].click()", link.getWrappedElement());
            // Fallback: click with JS if URL didn't change
        }
        webdriver().shouldHave(urlContaining("inventory-item"));
        // Verify URL contains "inventory-item" for product details page
        return new ProductDetailsPage();
        // Return new ProductDetailsPage object
    }

    @Step("Sort products by: {sortOption}")
    public ProductsPage sortBy(String sortOption) {
        sortDropdown.shouldBe(visible).selectOption(sortOption);
        // Select the sort option from dropdown
        return this;
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartIcon.shouldBe(visible).click();
        // Click on cart icon
        if (!webdriver().driver().url().contains("cart")) {
            sleep(500);
            executeJavaScript("arguments[0].click()", cartIcon.getWrappedElement());
            // Fallback JS click if URL didn't change
        }
        CartPage cartPage = new CartPage();
        cartPage.shouldBeLoaded();
        // Ensure cart page is loaded
        return cartPage;
    }

    @Step("Open hamburger menu")
    public MenuPage openMenu() {
        $("#react-burger-menu-btn").shouldBe(visible).click();
        // Click hamburger menu button
        $("#inventory_sidebar_link").shouldBe(visible);
        // Verify menu links are visible
        return new MenuPage();
        // Return MenuPage object for menu interactions
    }

    @Step("Verify cart badge shows {count} item(s)")
    public ProductsPage shouldHaveCartCount(int count) {
        cartBadge.shouldBe(visible).shouldHave(text(String.valueOf(count)));
        // Verify cart badge shows correct number of items
        return this;
    }

    @Step("Verify product count is {count}")
    public ProductsPage shouldHaveProductCount(int count) {
        productCards.shouldHave(size(count));
        // Verify the page shows the expected number of products
        return this;
    }
}