package com.swaglabs.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigManager {

    private static TestConfig config;

    private ConfigManager() {}

    public static TestConfig get() {
        if (config == null) {
            config = ConfigFactory.create(TestConfig.class, System.getProperties());
        }
        return config;
    }
}
