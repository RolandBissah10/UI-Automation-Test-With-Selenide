package com.swaglabs.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage {

    private final SelenideElement usernameInput = $("[data-test='username']");
    private final SelenideElement passwordInput = $("[data-test='password']");
    private final SelenideElement loginButton   = $("[data-test='login-button']");
    private final SelenideElement errorMessage  = $("[data-test='error']");
    private final SelenideElement errorIcon     = $(".error-button");

    @Step("Open login page")
    public LoginPage openPage() {
        Selenide.open("/");
        return this;
    }

    @Step("Enter username: {username}")
    public LoginPage enterUsername(String username) {
        usernameInput.shouldBe(visible).setValue(username);
        return this;
    }

    @Step("Enter password")
    public LoginPage enterPassword(String password) {
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    @Step("Click login button")
    public ProductsPage clickLogin() {
        loginButton.click();
        return new ProductsPage();
    }

    @Step("Login with invalid credentials")
    public LoginPage clickLoginExpectingError() {
        loginButton.click();
        return this;
    }

    @Step("Login as {username}")
    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    @Step("Attempt login with {username}")
    public LoginPage attemptLoginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginExpectingError();
    }

    @Step("Verify error message is displayed")
    public LoginPage shouldHaveError(String expectedMessage) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Verify login page is loaded")
    public LoginPage shouldBeLoaded() {
        webdriver().shouldHave(urlContaining("saucedemo.com"));
        loginButton.shouldBe(visible);
        usernameInput.shouldBe(visible);
        return this;
    }

    @Step("Verify error icon is displayed")
    public LoginPage shouldShowErrorIcon() {
        errorIcon.shouldBe(visible);
        return this;
    }

    public String getErrorMessage() {
        return errorMessage.shouldBe(visible).getText();
    }
}