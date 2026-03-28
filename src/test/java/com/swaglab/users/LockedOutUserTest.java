package com.swaglab.users;

import com.swaglab.base.BaseTest;
import com.swaglab.data.TestData;
import com.swaglabs.pages.LoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for locked_out_user:
 * User account that is locked and cannot access the application.
 * Tests authentication rejection and error handling.
 */
@Tag("user-locked-out")
@Tag("authentication")
@Epic("User Scenarios")
@Feature("Locked Out User")
@Story("Locked out user cannot access the application")
public class LockedOutUserTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    void openLoginPage() {
        loginPage = new LoginPage().shouldBeLoaded();
    }

    @Test
    @DisplayName("Locked out user gets specific error message")
    @Severity(SeverityLevel.CRITICAL)
    void lockedOutUserCannotLogin() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER);
    }

    @Test
    @DisplayName("Locked out user error shows error icon")
    @Severity(SeverityLevel.NORMAL)
    void lockedOutUserErrorShowsIcon() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldShowErrorIcon();
    }

    @Test
    @DisplayName("Locked out user error shows username field highlight")
    @Severity(SeverityLevel.MINOR)
    void lockedOutUserErrorHighlightsUsernameField() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHighlightUsernameField();
    }

    @Test
    @DisplayName("Locked out user cannot dismiss error and retry")
    @Severity(SeverityLevel.NORMAL)
    void lockedOutUserCanRetryLogin() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER)
                .clearUsernameField()
                .clearPasswordField()
                .enterUsername(CONFIG.lockedUser())
                .enterPassword(CONFIG.password())
                .clickLoginButton()
                .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER);
    }

    @Test
    @DisplayName("Locked out user cannot login even with correct credentials entered slowly")
    @Severity(SeverityLevel.MINOR)
    void lockedOutUserBehaviorIsConsistent() {
        for (int i = 0; i < 3; i++) {
            loginPage
                    .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                    .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER);
            loginPage.clearError();
        }
    }

    @Test
    @DisplayName("Locked out user error can be cleared using X button")
    @Severity(SeverityLevel.MINOR)
    void lockedOutUserCanClearError() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER)
                .closeErrorMessage()
                .shouldNotHaveError();
    }

    @Test
    @DisplayName("Locked out user error remains after attempting different credentials")
    @Severity(SeverityLevel.MINOR)
    void lockedOutUserCannotBypassWithDifferentPassword() {
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), "different_password")
                .shouldHaveError(TestData.ERROR_INVALID_CREDENTIALS);
    }

    @Test
    @DisplayName("Standard user can still login while locked out user cannot")
    @Severity(SeverityLevel.CRITICAL)
    void onlyLockedOutUserIsBlocked() {
        // First attempt with locked out user
        loginPage
                .attemptLoginAs(CONFIG.lockedUser(), CONFIG.password())
                .shouldHaveError(TestData.ERROR_LOCKED_OUT_USER)
                .closeErrorMessage();

        // Then attempt with standard user should succeed
        loginPage
                .loginAs(CONFIG.standardUser(), CONFIG.password())
                .shouldBeLoaded();
    }
}
