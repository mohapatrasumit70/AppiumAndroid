package com.company.framework.config;

import org.aeonbits.owner.ConfigCache;

/**
 * ConfigManager - Singleton accessor for FrameworkConfig.
 * Usage: ConfigManager.getConfig().deviceName()
 */
public class ConfigManager {

    private ConfigManager() {}

    public static FrameworkConfig getConfig() {
        return ConfigCache.getOrCreate(FrameworkConfig.class);
    }
}
