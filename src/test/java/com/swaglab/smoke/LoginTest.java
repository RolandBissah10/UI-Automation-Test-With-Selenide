package com.swaglab.smoke;

import com.swaglab.base.BaseTest;
import com.swaglabs.pages.LoginPage;
import com.swaglabs.pages.ProductsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

     @Test
    @DisplayName("Successful login with standard user")
    @Story("Valid user can log in")
    @Severity(SeverityLevel.BLOCKER)
    void successfulLoginTest() {
        new LoginPage()
                .shouldBeLoaded()
                .loginAs(CONFIG.standardUser(), CONFIG.password())
                .shouldBeLoaded();
    }

     @Test
    @DisplayName("Login fails with locked out user")
    @Story("Locked user cannot log in")
    @Severity(SeverityLevel.CRITICAL)
    void lockedUserLoginTest() {
        new LoginPage()
                .shouldBeLoaded()
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHaveError("Sorry, this user has been locked out.")
                .shouldShowErrorIcon();
    }

     @Test
    @DisplayName("Login fails with wrong password")
    @Story("Invalid credentials show error message")
    @Severity(SeverityLevel.NORMAL)
    void invalidPasswordTest() {
        new LoginPage()
                .attemptLoginAs(CONFIG.standardUser(), "wrong_password")
                .shouldHaveError("Username and password do not match any user in this service");
    }

     @Test
    @DisplayName("Login fails with empty username")
    @Severity(SeverityLevel.MINOR)
    void emptyUsernameTest() {
        new LoginPage()
                .enterPassword(CONFIG.password())
                .clickLoginExpectingError()
                .shouldHaveError(" Username is required");
    }

     @Test
    @DisplayName("Login fails with empty password")
    @Severity(SeverityLevel.MINOR)
    void emptyPasswordTest() {
        new LoginPage()
                .enterUsername(CONFIG.standardUser())
                .clickLoginExpectingError()
                .shouldHaveError(" Password is required");
    }


}
