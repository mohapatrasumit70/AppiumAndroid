package com.company.framework.utils;

import com.company.framework.base.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

/**
 * GestureUtil — Mobile gesture helpers (swipe, scroll, long press, drag).
 * Uses W3C Actions API (Appium 8+ / Selenium 4 compatible).
 */
public class GestureUtil {

    private static final Logger log = LogManager.getLogger(GestureUtil.class);
    private static final PointerInput FINGER = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    private GestureUtil() {}

    private static AndroidDriver driver() {
        return DriverManager.getDriver();
    }

    /**
     * Swipe from one point to another.
     */
    public static void swipe(int startX, int startY, int endX, int endY, int durationMs) {
        log.info("Swipe [{},{}] -> [{},{}]", startX, startY, endX, endY);
        Sequence swipe = new Sequence(FINGER, 0)
                .addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(FINGER.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), endX, endY))
                .addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(List.of(swipe));
    }

    /** Swipe Up (scroll down the page). */
    public static void swipeUp() {
        Dimension size = driver().manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY   = (int) (size.height * 0.2);
        swipe(startX, startY, startX, endY, 600);
    }

    /** Swipe Down (scroll up the page). */
    public static void swipeDown() {
        Dimension size = driver().manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY   = (int) (size.height * 0.8);
        swipe(startX, startY, startX, endY, 600);
    }

    /** Swipe Left. */
    public static void swipeLeft() {
        Dimension size = driver().manage().window().getSize();
        int y      = size.height / 2;
        int startX = (int) (size.width * 0.8);
        int endX   = (int) (size.width * 0.2);
        swipe(startX, y, endX, y, 600);
    }

    /** Swipe Right. */
    public static void swipeRight() {
        Dimension size = driver().manage().window().getSize();
        int y      = size.height / 2;
        int startX = (int) (size.width * 0.2);
        int endX   = (int) (size.width * 0.8);
        swipe(startX, y, endX, y, 600);
    }

    /**
     * Long press an element.
     *
     * @param element    element to long-press
     * @param durationMs duration in milliseconds
     */
    public static void longPress(WebElement element, int durationMs) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.x + size.width / 2;
        int centerY = location.y + size.height / 2;

        log.info("Long press at [{},{}] for {}ms", centerX, centerY, durationMs);

        Sequence longPress = new Sequence(FINGER, 0)
                .addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY))
                .addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(FINGER.createPointerMove(Duration.ofMillis(durationMs), PointerInput.Origin.viewport(), centerX, centerY))
                .addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(List.of(longPress));
    }

    /**
     * Tap at specific coordinates.
     */
    public static void tapAtCoordinates(int x, int y) {
        log.info("Tap at [{},{}]", x, y);
        Sequence tap = new Sequence(FINGER, 0)
                .addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver().perform(List.of(tap));
    }
}
