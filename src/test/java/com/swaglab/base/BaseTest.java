package com.swaglab.base; // Defines the package where this class belongs

// Import Selenide configuration and browser control classes
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;

// Import Selenide logging for Allure integration
import com.codeborne.selenide.logevents.SelenideLogger;

// Import custom configuration manager and config interface
import com.swaglabs.config.ConfigManager;
import com.swaglabs.config.TestConfig;

// Import Allure listener for Selenide
import io.qameta.allure.selenide.AllureSelenide;

// Import JUnit lifecycle annotations
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

// Base class for all UI tests (cannot be instantiated directly)
public abstract class BaseTest {

    // Load configuration once (browser, URL, timeout, etc.)
    protected static final TestConfig CONFIG = ConfigManager.get();

    // Runs once before all tests in the class
    @BeforeAll
    static void globalSetup() {

        // Attach Allure listener to Selenide
        // This enables automatic screenshots and page source capture on failure
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)        // take screenshot on failure
                .savePageSource(true));   // save HTML page source on failure

        // -------- Browser Configuration --------

        // Set browser type (e.g., chrome or firefox)
        Configuration.browser = CONFIG.browser();

        // Run browser in headless mode (no UI) if true
        Configuration.headless = CONFIG.headless();

        // Set base URL for the application under test
        Configuration.baseUrl = CONFIG.baseUrl();

        // Set default timeout for waiting (e.g., element visibility)
        Configuration.timeout = CONFIG.timeout();

        // Set page load timeout (30 seconds)
        Configuration.pageLoadTimeout = 30_000;

        // Folder where screenshots and reports will be saved
        Configuration.reportsFolder = "target/selenide-screenshots";

        // -------- Browser-Specific Options --------

        // If browser is Chrome, apply Chrome-specific settings
        if ("chrome".equalsIgnoreCase(CONFIG.browser())) {
            Configuration.browserCapabilities = chromeOptions();
        }
        // If browser is Firefox, apply Firefox-specific settings
        else if ("firefox".equalsIgnoreCase(CONFIG.browser())) {
            Configuration.browserCapabilities = firefoxOptions();
        }
    }

    // Runs before each test method
    @BeforeEach
    void openBrowser() {

        // Open browser and navigate to base URL
        Selenide.open(CONFIG.baseUrl());

        // Clear cookies to ensure clean session (no previous login/session data)
        Selenide.clearBrowserCookies();
    }

    // Runs after each test method
    @AfterEach
    void closeBrowser() {

        // Close browser and WebDriver instance
        Selenide.closeWebDriver();
    }

    // Method to define Chrome-specific options
    private static org.openqa.selenium.chrome.ChromeOptions chromeOptions() {

        // Create ChromeOptions object
        var options = new org.openqa.selenium.chrome.ChromeOptions();

        // Add arguments to improve stability in CI/CD (Docker, Jenkins)
        options.addArguments(
                "--no-sandbox",                 // required for running in containers
                "--disable-dev-shm-usage",      // avoid memory issues in Docker
                "--disable-gpu",                // disable GPU rendering
                "--window-size=1920,1080",     // set consistent screen resolution
                "--disable-extensions",         // disable browser extensions
                "--remote-allow-origins=*",     // allow remote connections
                "--incognito",                  // run browser in incognito mode
                "--disable-application-cache",  // disable app cache
                "--disable-cache"               // disable browser cache
        );

        // Return configured Chrome options
        return options;
    }

    // Method to define Firefox-specific options
    private static org.openqa.selenium.firefox.FirefoxOptions firefoxOptions() {

        // Create FirefoxOptions object
        var options = new org.openqa.selenium.firefox.FirefoxOptions();

        // Set browser window size
        options.addArguments("--width=1920", "--height=1080");

        // Return configured Firefox options
        return options;
    }
}