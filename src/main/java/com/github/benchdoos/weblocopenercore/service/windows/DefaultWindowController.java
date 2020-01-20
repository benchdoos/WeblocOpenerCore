package com.github.benchdoos.weblocopenercore.service.windows;

import lombok.extern.log4j.Log4j2;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.github.benchdoos.weblocopenercore.core.constants.PathConstants.WINDOW_LOCATIONS_FILE_PATH;

@Log4j2
public class DefaultWindowController extends WindowController {

    private static final File WINDOW_SETTINGS_FILE = new File(WINDOW_LOCATIONS_FILE_PATH);
    private static Set<WindowSetting> windowSettings = null;

    @Override
    public void storeWindow(Window window) throws IOException {
        final HashMap<WindowSettingName, Object> settings = new HashMap<>();

        saveWindowProperties(window, settings);

        updateWindowsSettings(window, settings);
    }

    @Override
    public void loadWindow(Window window) throws IOException {
        if (windowSettings == null) {
            forceSettingsLoad();
        }

        windowSettings.stream()
                .filter(setting -> setting.getWindowClassName().equals(window.getClass().getSimpleName()))
                .findFirst().ifPresent(s -> setWindowPropertiesFromSettings(window, s));
    }

    public void forceSettingsLoad() throws IOException {
        windowSettings = loadSettings(WINDOW_SETTINGS_FILE);
    }

    private void setWindowPropertiesFromSettings(Window window, WindowSetting windowSetting) {
        final Map<WindowSettingName, Object> settings = windowSetting.getSettings();
        if (settings.containsKey(WindowSettingName.LOCATION)) {
            final Point point = (Point) settings.get(WindowSettingName.LOCATION);
            window.setLocation(point);
        }

        if (settings.containsKey(WindowSettingName.WINDOW_SIZE)) {
            final Dimension dimension = (Dimension) settings.get(WindowSettingName.WINDOW_SIZE);
            window.setSize(dimension);
        }

        if (settings.containsKey(WindowSettingName.EXTENDED_STATE)) {
            if (window instanceof JFrame) {
                final JFrame frame = (JFrame) window;

                final int state = (int) settings.get(WindowSettingName.EXTENDED_STATE);
                frame.setExtendedState(state);
            }
        }
    }

    private void updateWindowsSettings(Window window, HashMap<WindowSettingName, Object> settings) throws IOException {
        forceSettingsLoad();

        final Set<WindowSetting> windowSettings = loadSettings(WINDOW_SETTINGS_FILE);
        windowSettings.add(new WindowSetting(window.getClass().getSimpleName(), settings));

        saveSettings(windowSettings, WINDOW_SETTINGS_FILE);
    }

    private void saveWindowProperties(Window window, HashMap<WindowSettingName, Object> settings) {
        if (window instanceof JFrame) {
            final int extendedState = ((JFrame) window).getExtendedState();
            settings.put(WindowSettingName.EXTENDED_STATE, extendedState);
        }

        final Point location = window.getLocation();
        settings.put(WindowSettingName.LOCATION, location);

        final Dimension size = window.getSize();
        settings.put(WindowSettingName.WINDOW_SIZE, size);
    }
}
