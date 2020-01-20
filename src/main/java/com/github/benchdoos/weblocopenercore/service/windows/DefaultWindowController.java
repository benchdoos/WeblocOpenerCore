package com.github.benchdoos.weblocopenercore.service.windows;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.github.benchdoos.weblocopenercore.core.constants.PathConstants.WINDOW_LOCATIONS_FILE_PATH;

@Log4j2
public class DefaultWindowController extends WindowController {

    private static final File WINDOW_SETTINGS_FILE = new File(WINDOW_LOCATIONS_FILE_PATH);
    private static Set<WindowSettings> windowSettings = null;

    @Override
    public void storeWindow(@NotNull Window window) {
        final WindowSettings.Settings settings = new WindowSettings.Settings();
        try {

            saveWindowProperties(window, settings);

            updateWindowsSettings(window, settings);
        } catch (IOException e) {
            log.warn("Could not save window location. Window: {}. Settings: {}", window.getClass().getSimpleName(), settings, e);
        }
    }

    @Override
    public void loadWindow(@NotNull Window window) {
        if (windowSettings == null) {
            forceSettingsLoad();
        }

        if (windowSettings != null) {
            windowSettings.stream()
                    .filter(setting -> setting.getWindow().equals(window.getClass().getName()))
                    .findFirst().ifPresent(s -> setWindowPropertiesFromSettings(window, s));
        }
    }

    public void forceSettingsLoad() {
        try {
            windowSettings = loadSettings(WINDOW_SETTINGS_FILE);
        } catch (IOException e) {
            log.warn("Could not read settings from file: {}", WINDOW_SETTINGS_FILE, e);
            windowSettings = new HashSet<>();
        }
    }

    private void setWindowPropertiesFromSettings(Window window, WindowSettings windowSettings) {
        final WindowSettings.Settings settings = windowSettings.getSettings();

        if (settings.getExtendedState() != null) {
            if (window instanceof JFrame) {
                final JFrame frame = (JFrame) window;

                frame.setExtendedState(settings.getExtendedState());

                if (settings.getExtendedState() == JFrame.NORMAL) {
                    applyLocationAndSizeSettingsToWindow(window, settings);
                }
            }
        } else {
            applyLocationAndSizeSettingsToWindow(window, settings);
        }
        log.info("Window ({}) settings were set to {}", window.getClass().getName(), settings);
    }

    private void applyLocationAndSizeSettingsToWindow(Window window, WindowSettings.Settings settings) {
        if (settings.getLocation() != null) {
            if (settings.getLocation().x > 50 && settings.getLocation().y > 50) {
                window.setLocation(settings.getLocation());
            }
        }

        if (settings.getSize() != null) {
            if (settings.getSize().getWidth() >= window.getMinimumSize().getWidth()
                    && settings.getSize().getHeight() >= window.getMinimumSize().getHeight()) {

                window.setSize(settings.getSize());
            }
        }
    }

    private void updateWindowsSettings(Window window, WindowSettings.Settings settings) throws IOException {
        forceSettingsLoad();

        final String name = window.getClass().getName();

        // settings not used in equals+hashcode, so object is equal to each other, so it is not updated.
        windowSettings.remove(new WindowSettings(name, null));

        windowSettings.add(new WindowSettings(name, settings));

        saveSettings(windowSettings, WINDOW_SETTINGS_FILE);

        log.info("Saved window settings for window: {}. Settings: {}", name, settings);
    }

    private void saveWindowProperties(Window window, WindowSettings.Settings settings) {
        if (window instanceof JFrame) {
            final int extendedState = ((JFrame) window).getExtendedState();
            settings.setExtendedState(extendedState);

            if (extendedState == JFrame.NORMAL) {
                updateLocationAndSize(window, settings);
            }
        } else {
            updateLocationAndSize(window, settings);
        }
    }

    private void updateLocationAndSize(Window window, WindowSettings.Settings settings) {
        final Point location = window.getLocation();
        settings.setLocation(location);

        final Dimension size = window.getSize();
        settings.setSize(size);
    }
}
