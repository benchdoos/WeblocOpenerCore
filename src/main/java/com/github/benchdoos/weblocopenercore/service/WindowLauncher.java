package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;

import java.awt.Window;

public abstract class WindowLauncher<Child extends Window> {
    private final Child window;

    public WindowLauncher(Child window) {
        if (PreferencesManager.isDarkModeEnabledNow()) {
            final JColorful colorful = new JColorful(ApplicationConstants.DARK_MODE_THEME);
            colorful.colorizeGlobal();

            this.window = window;
            colorful.colorize(window);
        } else {
            this.window = window;
        }
    }

    public Child getWindow() {
        return window;
    }
}
