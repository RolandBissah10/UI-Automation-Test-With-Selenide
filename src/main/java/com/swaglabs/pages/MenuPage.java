package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MenuPage {

    private final SelenideElement allItemsLink     = $("#inventory_sidebar_link");
    private final SelenideElement aboutLink        = $("#about_sidebar_link");
    private final SelenideElement logoutLink       = $("#logout_sidebar_link");
    private final SelenideElement resetLink        = $("#reset_sidebar_link");
    private final SelenideElement closeMenuButton  = $("#react-burger-cross-btn");

    @Step("Verify All Items link is visible in menu")
    public MenuPage shouldShowAllItems() {
        allItemsLink.shouldBe(visible);
        return this;
    }

    @Step("Verify About link is visible in menu")
    public MenuPage shouldShowAbout() {
        aboutLink.shouldBe(visible);
        return this;
    }

    @Step("Verify Logout link is visible in menu")
    public MenuPage shouldShowLogout() {
        logoutLink.shouldBe(visible);
        return this;
    }

    @Step("Verify Reset App State link is visible in menu")
    public MenuPage shouldShowResetButton() {
        resetLink.shouldBe(visible);
        return this;
    }

    @Step("Click Logout from menu")
    public LoginPage clickLogout() {
        logoutLink.shouldBe(visible).click();
        return new LoginPage().shouldBeLoaded();
    }

    @Step("Close menu")
    public ProductsPage closeMenu() {
        closeMenuButton.shouldBe(visible).click();
        return new ProductsPage();
    }
}
