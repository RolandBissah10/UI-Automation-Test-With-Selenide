package com.swaglabs.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            // Selenide.screenshot(String) saves to file and returns the path
            String timestamp = LocalDateTime.now().format(FORMATTER);
            File screenshot = new File(Selenide.screenshot(stepName + "_" + timestamp));
            if (screenshot != null) {
                byte[] bytes = Files.readAllBytes(screenshot.toPath());
                Allure.addAttachment(stepName, "image/png", new ByteArrayInputStream(bytes), ".png");
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
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String filename = testName + "_" + timestamp;

            File screenshot = new File(Selenide.screenshot(filename));
            if (screenshot != null) {
                Path dir = Paths.get(SCREENSHOT_DIR);
                Files.createDirectories(dir);
                Path dest = dir.resolve(screenshot.getName());
                Files.copy(screenshot.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                log.info("Screenshot saved: {}", dest.toAbsolutePath());
                return dest;
            }
        } catch (IOException e) {
            log.warn("Failed to save screenshot for '{}': {}", testName, e.getMessage());
        }
        return null;
    }

    /**
     * Captures the current page source and attaches it to the Allure report.
     *
     * @param label label shown in the Allure report attachment
     */
    public static void capturePageSource(String label) {
        try {
            String source = WebDriverRunner.getWebDriver().getPageSource();
            Allure.addAttachment(label + " (page source)", "text/html",
                    new ByteArrayInputStream(source.getBytes()), ".html");
        } catch (Exception e) {
            log.warn("Failed to capture page source: {}", e.getMessage());
        }
    }
}