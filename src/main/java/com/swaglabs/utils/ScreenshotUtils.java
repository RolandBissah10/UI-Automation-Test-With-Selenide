package com.swaglabs.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for manual screenshot capture.
 * Note: Selenide + AllureSelenide automatically captures screenshots on failures.
 * Use this class for capturing screenshots at specific test steps.
 */
public class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/selenide-screenshots";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtils() {}

    /**
     * Takes a screenshot and attaches it to the Allure report.
     *
     * @param stepName descriptive name shown in the Allure report
     */
    public static void takeScreenshot(String stepName) {
        try {
            byte[] screenshot = captureScreenshotAsBytes();
            if (screenshot != null && screenshot.length > 0) {
                Allure.addAttachment(stepName, "image/png", new ByteArrayInputStream(screenshot), ".png");
                log.debug("Screenshot captured: {}", stepName);
            }
        } catch (Exception e) {
            log.warn("Failed to capture screenshot for step '{}': {}", stepName, e.getMessage());
        }
    }

    /**
     * Takes a screenshot and saves it to disk under target/selenide-screenshots.
     *
     * @param testName used to build the filename
     * @return path to the saved file, or null if capture failed
     */
    public static Path saveScreenshot(String testName) {
        try {
            String filename = testName + "_" + LocalDateTime.now().format(FORMATTER) + ".png";
            Path dir = Paths.get(SCREENSHOT_DIR);
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);

            byte[] screenshot = captureScreenshotAsBytes();
            if (screenshot != null && screenshot.length > 0) {
                Files.write(target, screenshot);
                log.info("Screenshot saved: {}", target.toAbsolutePath());
                return target;
            }
        } catch (IOException e) {
            log.warn("Failed to save screenshot for '{}': {}", testName, e.getMessage());
        }
        return null;
    }

    private static byte[] captureScreenshotAsBytes() throws IOException {
        // Try Selenide native API first (varies by version), then fallback to WebDriver TakesScreenshot.
        try {
            String screenshotPath = Selenide.screenshot("screenshot-" + System.currentTimeMillis());
            if (screenshotPath != null) {
                Path path = Paths.get(screenshotPath);
                if (Files.exists(path)) {
                    return Files.readAllBytes(path);
                }
            }
        } catch (Exception ignored) {
            // fallback to direct Selenium screenshot capture
        }

        try {
            return ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            throw new IOException("Unable to capture screenshot", e);
        }
    }

    /**
     * Captures the current page source and attaches it to the Allure report.
     *
     * @param label label shown in the Allure report attachment
     */
    public static void capturePageSource(String label) {
        try {
            String source = Selenide.source();
            Allure.addAttachment(label + " (page source)", "text/html",
                    new ByteArrayInputStream(source.getBytes()), ".html");
        } catch (Exception e) {
            log.warn("Failed to capture page source: {}", e.getMessage());
        }
    }
}
