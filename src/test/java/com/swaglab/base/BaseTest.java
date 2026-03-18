package com.swaglab.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.swaglabs.config.ConfigManager;
import com.swaglabs.config.TestConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    protected static final TestConfig CONFIG = ConfigManager.get();

    @BeforeAll
    static void globalSetup() {
        // Allure-Selenide integration: captures screenshots + page source on failures
        SelenideLogger.addListener("allure", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));

        // Browser configuration
        Configuration.browser        = CONFIG.browser();
        Configuration.headless       = CONFIG.headless();
        Configuration.baseUrl        = CONFIG.baseUrl();
        Configuration.timeout        = CONFIG.timeout();
        Configuration.pageLoadTimeout = 30_000;
        Configuration.reportsFolder  = "target/selenide-screenshots";

        // Browser-specific options
        if ("chrome".equalsIgnoreCase(CONFIG.browser())) {
            Configuration.browserCapabilities = chromeOptions();
        } else if ("firefox".equalsIgnoreCase(CONFIG.browser())) {
            Configuration.browserCapabilities = firefoxOptions();
        }
    }

    @BeforeEach
    void openBrowser() {
        Selenide.open(CONFIG.baseUrl());
    }

    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }

    private static org.openqa.selenium.chrome.ChromeOptions chromeOptions() {
        var options = new org.openqa.selenium.chrome.ChromeOptions();
        options.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--disable-gpu",
            "--window-size=1920,1080",
            "--disable-extensions",
            "--remote-allow-origins=*"
        );
        return options;
    }

    private static org.openqa.selenium.firefox.FirefoxOptions firefoxOptions() {
        var options = new org.openqa.selenium.firefox.FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");
        return options;
    }
}
