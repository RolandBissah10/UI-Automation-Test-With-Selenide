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
    private final SelenideElement logoutLink       = $("#logout_sidebar_link");
    private final SelenideElement sortDropdown     = $("[data-test='product-sort-container']");
    private final SelenideElement cartIcon         = $(".shopping_cart_link");
    private final SelenideElement cartBadge        = $(".shopping_cart_badge");
    private final SelenideElement footer           = $("footer");
    private final ElementsCollection productCards  = $$(".inventory_item");
    private final ElementsCollection productNames  = $$(".inventory_item_name");
    private final ElementsCollection productImages = $$(".inventory_item_img img");
    private final ElementsCollection productPrices = $$(".inventory_item_price");
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
        String removeDataTest = "remove-" + dataTest.replace("add-to-cart-", "");

        $("[data-test='" + dataTest + "']").shouldBe(visible).click();

        // Wait for the button to flip to "Remove" — confirms the add actually registered
        // This is the key fix: without this, shouldHaveCartCount runs before the DOM updates on CI
        $("[data-test='" + removeDataTest + "']").shouldBe(visible);
        return this;
    }

    @Step("Add first product to cart")
    public ProductsPage addFirstProductToCart() {
        SelenideElement btn = addToCartBtns.first().shouldBe(visible);
        // Derive the expected remove button from the add button's data-test attribute
        String addDataTest = btn.getAttribute("data-test");
        String removeDataTest = addDataTest != null
                ? addDataTest.replace("add-to-cart-", "remove-")
                : null;
        btn.click();
        if (removeDataTest != null) {
            $("[data-test='" + removeDataTest + "']").shouldBe(visible);
        }
        return this;
    }

    @Step("Open product details: {productName}")
    public ProductDetailsPage openProduct(String productName) {
        SelenideElement link = productNames.findBy(text(productName)).shouldBe(visible);
        link.click();
        sleep(500);
        if (!webdriver().driver().url().contains("inventory-item")) {
            executeJavaScript("arguments[0].click()", link.getWrappedElement());
        }
        webdriver().shouldHave(urlContaining("inventory-item"));
        return new ProductDetailsPage();
    }

    @Step("Sort products by: {sortOption}")
    public ProductsPage sortBy(String sortOption) {
        sortDropdown.shouldBe(visible).selectOption(sortOption);
        return this;
    }

    @Step("Open cart")
    public CartPage openCart() {
        cartIcon.shouldBe(visible).click();
        if (!webdriver().driver().url().contains("cart")) {
            sleep(500);
            executeJavaScript("arguments[0].click()", cartIcon.getWrappedElement());
        }
        webdriver().shouldHave(urlContaining("cart"));
        return new CartPage();
    }

    @Step("Open hamburger menu")
    public MenuPage openMenu() {
        $("#react-burger-menu-btn").shouldBe(visible).click();
        // Wait for the slide-open animation to complete before checking child links.
        // The menu wrapper becomes aria-hidden=false and gets display:block only after
        // the CSS transition finishes — on CI this takes measurably longer than locally.
        $(".bm-menu-wrap").shouldNotHave(attribute("aria-hidden", "true"));
        $("#inventory_sidebar_link").shouldBe(visible);
        return new MenuPage();
    }

    @Step("Logout")
    public LoginPage logout() {
        openMenu();
        logoutLink.shouldBe(visible).click();
        return new LoginPage().shouldBeLoaded();
    }

    @Step("Verify cart badge shows {count} item(s)")
    public ProductsPage shouldHaveCartCount(int count) {
        cartBadge.shouldBe(visible).shouldHave(text(String.valueOf(count)));
        return this;
    }

    @Step("Verify cart badge is visible")
    public ProductsPage shouldHaveCartBadge() {
        cartBadge.shouldBe(visible);
        return this;
    }

    @Step("Verify cart shows {count} item(s)")
    public ProductsPage shouldShowCartCountAs(int count) {
        cartBadge.shouldBe(visible).shouldHave(text(String.valueOf(count)));
        return this;
    }

    @Step("Verify product count is {count}")
    public ProductsPage shouldHaveProductCount(int count) {
        productCards.shouldHave(size(count));
        return this;
    }

    @Step("Verify all product images are visible")
    public ProductsPage shouldHaveProductImages() {
        productImages.shouldHave(sizeGreaterThan(0));
        productImages.forEach(img -> img.shouldBe(visible));
        return this;
    }

    @Step("Verify all product names are visible")
    public ProductsPage shouldHaveProductNames() {
        productNames.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify all product prices are visible")
    public ProductsPage shouldHaveProductPrices() {
        productPrices.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify all Add to Cart buttons are visible")
    public ProductsPage shouldHaveAddToCartButtons() {
        addToCartBtns.shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify product '{productName}' has a broken image")
    public ProductsPage shouldHaveProductWithBrokenImage(String productName) {
        SelenideElement itemContainer = productCards.findBy(text(productName));
        SelenideElement img = itemContainer.$(".inventory_item_img img");
        img.shouldBe(exist);
        return this;
    }

    @Step("Verify product '{productName}' has price {price}")
    public ProductsPage shouldHaveProductPrice(String productName, String price) {
        SelenideElement itemContainer = productCards.findBy(text(productName));
        itemContainer.$(".inventory_item_price")
                .shouldBe(visible)
                .shouldHave(text(price));
        return this;
    }

    @Step("Verify sort dropdown is visible")
    public ProductsPage shouldHaveSortDropdown() {
        sortDropdown.shouldBe(visible);
        return this;
    }

    @Step("Verify all sort options are present")
    public ProductsPage shouldShowAllSortOptions() {
        sortDropdown.shouldBe(visible);
        $$("[data-test='product-sort-container'] option").shouldHave(sizeGreaterThan(0));
        return this;
    }

    @Step("Verify footer is visible")
    public ProductsPage shouldHaveFooter() {
        footer.shouldBe(visible);
        return this;
    }

}