package com.company.framework.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({"classpath:config/config.properties"})
public interface FrameworkConfig extends Config {

    @Key("appium.server.url")
    @DefaultValue("http://127.0.0.1:4723")
    String appiumServerUrl();

    // --- ADD THESE 3 MAPPINGS FOR AUTO SERVER START ---
    @Key("auto.start.appium")
    @DefaultValue("true")
    boolean autoStartAppium();

    @Key("appium.host")
    @DefaultValue("127.0.0.1")
    String appiumHost();

    @Key("appium.port")
    @DefaultValue("4723")
    int appiumPort();
    // --------------------------------------------------

    @Key("platform.name")
    @DefaultValue("Android")
    String platformName();

    @Key("device.name")
    String deviceName();

    @Key("udid")
    String udid();

    @Key("platform.version")
    String platformVersion();

    @Key("app.path")
    String appPath();

    @Key("app.package")
    String appPackage();

    @Key("app.activity")
    String appActivity();

    @Key("automation.name")
    @DefaultValue("UiAutomator2")
    String automationName();

    @Key("implicit.wait")
    @DefaultValue("10")
    int implicitWait();

    @Key("explicit.wait")
    @DefaultValue("20")
    int explicitWait();

    @Key("screenshot.on.failure")
    @DefaultValue("true")
    boolean screenshotOnFailure();

    @Key("report.title")
    @DefaultValue("Android Automation Report")
    String reportTitle();

    @Key("report.name")
    @DefaultValue("Test Execution Report")
    String reportName();

    @Key("environment")
    @DefaultValue("QA")
    String environment();

    @Key("tester.name")
    @DefaultValue("Automation Team")
    String testerName();

    @Key("auto.grant.permissions")
    boolean autoGrantPermissions();

    @Key("no.reset")
    boolean noReset();
    @Key("db.url")
    String dbUrl();

    @Key("db.username")
    String dbUsername();

    @Key("db.password")
    String dbPassword();
    
    
    @Key("record.video")
    @DefaultValue("true")
    boolean recordVideo();

    @Key("video.on.failure.only")
    @DefaultValue("true")
    boolean videoOnFailureOnly();
}