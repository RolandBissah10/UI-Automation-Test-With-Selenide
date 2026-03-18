package com.swaglabs.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    // Locators
    private final SelenideElement usernameInput   = $("#user-name");
    private final SelenideElement passwordInput   = $("#password");
    private final SelenideElement loginButton     = $("#login-button");
    private final SelenideElement errorMessage    = $("[data-test='error']");
    private final SelenideElement errorIcon       = $(".error-button");

    @Step("Open login page")
    public LoginPage open() {
        open("/");
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

    // Assertions
    @Step("Verify error message is displayed")
    public LoginPage shouldHaveError(String expectedMessage) {
        errorMessage.shouldBe(visible).shouldHave(text(expectedMessage));
        return this;
    }

    @Step("Verify login page is loaded")
    public LoginPage shouldBeLoaded() {
        loginButton.shouldBe(visible);
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
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
