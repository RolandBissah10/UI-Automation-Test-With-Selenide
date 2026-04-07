package com.swaglabs.utils;
// Package for utility classes used in tests

import com.codeborne.selenide.Selenide;
// Selenide class provides browser automation functions like taking screenshots
import com.codeborne.selenide.WebDriverRunner;
// WebDriverRunner gives access to underlying WebDriver (for page source, browser info)
import io.qameta.allure.Allure;
// Allure class used to attach files (screenshots, page source) to test reports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// SLF4J logging classes for debug and error messages

import java.io.ByteArrayInputStream;
// To wrap byte arrays as InputStreams for Allure attachments
import java.io.File;
// To work with file paths
import java.io.IOException;
// For handling IO exceptions
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
// NIO classes for creating directories, copying files, and path operations
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
// DateTime classes to add timestamps to filenames

/**
 * Utility class for manual screenshot capture.
 * Selenide + AllureSelenide already capture screenshots on failures automatically.
 * Use this class when you want screenshots at specific test steps.
 */
public class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    // Logger instance for debug, info, and warning messages

    private static final String SCREENSHOT_DIR = "target/selenide-screenshots";
    // Default directory where screenshots will be saved

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    // Formatter for adding timestamps to filenames for uniqueness

    private ScreenshotUtils() {}
    // Private constructor prevents instantiation (utility class)

    /**
     * Takes a screenshot and attaches it to the Allure report.
     *
     * @param stepName descriptive name shown in the Allure report
     */
    public static void takeScreenshot(String stepName) {
        try {
            // Get current timestamp
            String timestamp = LocalDateTime.now().format(FORMATTER);

            // Capture screenshot using Selenide and store in file
            File screenshot = new File(Selenide.screenshot(stepName + "_" + timestamp));

            if (screenshot != null) {
                // Read file bytes
                byte[] bytes = Files.readAllBytes(screenshot.toPath());

                // Attach screenshot to Allure report
                Allure.addAttachment(stepName, "image/png", new ByteArrayInputStream(bytes), ".png");

                // Log debug message for successful screenshot capture
                log.debug("Screenshot captured: {}", stepName);
            }
        } catch (Exception e) {
            // Log warning if screenshot capture fails
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
            // Get timestamp
            String timestamp = LocalDateTime.now().format(FORMATTER);

            // Build filename with timestamp
            String filename = testName + "_" + timestamp;

            // Take screenshot with Selenide
            File screenshot = new File(Selenide.screenshot(filename));

            if (screenshot != null) {
                // Ensure the directory exists
                Path dir = Paths.get(SCREENSHOT_DIR);
                Files.createDirectories(dir);

                // Destination path for saved screenshot
                Path dest = dir.resolve(screenshot.getName());

                // Copy screenshot to destination, replace if exists
                Files.copy(screenshot.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

                // Log info about saved screenshot
                log.info("Screenshot saved: {}", dest.toAbsolutePath());

                // Return path of saved file
                return dest;
            }
        } catch (IOException e) {
            // Log warning if saving fails
            log.warn("Failed to save screenshot for '{}': {}", testName, e.getMessage());
        }

        // Return null if capture failed
        return null;
    }

    /**
     * Captures the current page source and attaches it to the Allure report.
     *
     * @param label label shown in the Allure report attachment
     */
    public static void capturePageSource(String label) {
        try {
            // Get the current page source from the browser
            String source = WebDriverRunner.getWebDriver().getPageSource();

            // Attach page source to Allure report as HTML
            Allure.addAttachment(label + " (page source)", "text/html",
                    new ByteArrayInputStream(source.getBytes()), ".html");
        } catch (Exception e) {
            // Log warning if page source capture fails
            log.warn("Failed to capture page source: {}", e.getMessage());
        }
    }
}