package com.swaglabs.config; // Defines the package where this class belongs (configuration-related classes)

import org.aeonbits.owner.ConfigFactory; // Imports OWNER library's ConfigFactory to create config objects dynamically

public class ConfigManager { // Defines a public class to manage test configuration

    // Static variable to hold a single instance of TestConfig (singleton pattern)
    private static TestConfig config;

    // Private constructor prevents instantiation of this class from outside
    // This ensures ConfigManager is only used through the get() method
    private ConfigManager() {}

    // Public method to access the TestConfig instance
    public static TestConfig get() {
        // Check if the config instance has already been created
        if (config == null) {
            // If not created yet, create a new instance using OWNER
            // OWNER reads properties from System.getProperties() (e.g., from JVM -D arguments)
            config = ConfigFactory.create(TestConfig.class, System.getProperties());
        }
        // Return the single TestConfig instance
        return config;
    }
}