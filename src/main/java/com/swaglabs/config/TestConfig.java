package com.swaglabs.config;

import org.aeonbits.owner.Config;

@Config.Sources({
    "system:properties",
    "classpath:config.properties"
})
public interface TestConfig extends Config {

    @Key("base.url")
    @DefaultValue("https://www.saucedemo.com")
    String baseUrl();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("headless")
    @DefaultValue("true")
    boolean headless();

    @Key("timeout")
    @DefaultValue("10000")
    long timeout();

    @Key("standard.user")
    @DefaultValue("standard_user")
    String standardUser();

    @Key("locked.user")
    @DefaultValue("locked_out_user")
    String lockedUser();

    @Key("password")
    @DefaultValue("secret_sauce")
    String password();
}
