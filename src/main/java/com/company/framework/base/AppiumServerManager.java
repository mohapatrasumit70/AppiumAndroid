package com.company.framework.base;

import com.company.framework.config.ConfigManager;
import com.company.framework.config.FrameworkConfig;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.Duration;

public class AppiumServerManager {

    private static final Logger log = LogManager.getLogger(AppiumServerManager.class);
    private static AppiumDriverLocalService service;

    public static void startServerIfEnabled() {
        FrameworkConfig cfg = ConfigManager.getConfig();

        if (!cfg.autoStartAppium()) {
            log.info("auto.start.appium is FALSE. Please ensure Appium server is started manually.");
            return;
        }

        if (service != null && service.isRunning()) {
            log.info("Appium Server is already running at: {}", service.getUrl());
            return;
        }

        log.info("auto.start.appium is TRUE. Starting Appium Server automatically...");

        // Locate global Appium main.js on Windows
        String userHome = System.getProperty("user.home");
        File appiumMainJs = new File(userHome + "\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js");

        // Ensure logs directory exists
        File logDir = new File("logs");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress(cfg.appiumHost())
                .usingPort(cfg.appiumPort())
                .withTimeout(Duration.ofSeconds(60))
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withLogFile(new File(logDir, "appium-server.log"));

        if (appiumMainJs.exists()) {
            builder.withAppiumJS(appiumMainJs);
        }

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        if (service == null || !service.isRunning()) {
            throw new RuntimeException("Failed to start Appium Server automatically!");
        }

        log.info("✔ Appium Server started completely at: {}", service.getUrl());
    }

    public static void stopServerIfRunning() {
        if (service != null && service.isRunning()) {
            log.info("Stopping automatically started Appium Server...");
            service.stop();
            log.info("✔ Appium Server stopped.");
        }
    }
}