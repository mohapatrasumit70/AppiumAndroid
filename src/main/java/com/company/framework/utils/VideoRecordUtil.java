package com.company.framework.utils;

import com.company.framework.base.DriverManager;
import com.company.framework.config.ConfigManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

public class VideoRecordUtil {

    private static final Logger log = LogManager.getLogger(VideoRecordUtil.class);

    private VideoRecordUtil() {}

    /**
     * Starts screen recording on the Android device if record.video=true.
     */
    public static void startRecording() {
        if (!ConfigManager.getConfig().recordVideo()) {
            return;
        }

        AndroidDriver driver = DriverManager.getDriver();
        if (driver != null) {
            try {
                driver.startRecordingScreen(
                        new AndroidStartScreenRecordingOptions()
                                .withTimeLimit(Duration.ofMinutes(10))
                                .withVideoSize("720x1280") // Keeps file size small and smooth
                );
                log.info("🎥 Started screen recording for test.");
            } catch (Exception e) {
                log.warn("Could not start screen recording: {}", e.getMessage());
            }
        }
    }

    /**
     * Stops screen recording and saves the .mp4 file if conditions are met.
     *
     * @param testName Name of the test method
     * @param isFailed true if the test failed, false otherwise
     * @return Absolute path to the saved .mp4 video, or null if not saved
     */
    public static String stopAndSaveRecording(String testName, boolean isFailed) {
        var cfg = ConfigManager.getConfig();
        if (!cfg.recordVideo()) {
            return null;
        }

        AndroidDriver driver = DriverManager.getDriver();
        if (driver == null) {
            return null;
        }

        try {
            // Stop recording and get Base64 encoded video from Appium
            String base64Video = driver.stopRecordingScreen();

            // If configured to save ONLY on failure and test passed, discard the video
            if (cfg.videoOnFailureOnly() && !isFailed) {
                log.info("Test passed and video.on.failure.only=true. Discarding video recording.");
                return null;
            }

            if (base64Video == null || base64Video.isEmpty()) {
                return null;
            }

            // Create /videos directory if it doesn't exist
            File videoDir = new File(System.getProperty("user.dir") + File.separator + "videos");
            if (!videoDir.exists()) {
                videoDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String statusPrefix = isFailed ? "FAIL_" : "PASS_";
            File videoFile = new File(videoDir, statusPrefix + testName + "_" + timestamp + ".mp4");

            byte[] decodedBytes = Base64.getDecoder().decode(base64Video);
            try (FileOutputStream fos = new FileOutputStream(videoFile)) {
                fos.write(decodedBytes);
            }

            log.info("🎥 Video recording saved at: {}", videoFile.getAbsolutePath());
            return videoFile.getAbsolutePath();

        } catch (Exception e) {
            log.error("Failed to stop/save screen recording: {}", e.getMessage());
            return null;
        }
    }
}