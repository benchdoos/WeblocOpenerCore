package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;

import java.awt.Window;

public abstract class WindowLauncher<Child extends Window> {
    final Child window;

    public WindowLauncher() {
        if (PreferencesManager.isDarkModeEnabledNow()) {
            final JColorful colorful = new JColorful(ApplicationConstants.DARK_MODE_THEME);
            colorful.colorizeGlobal();

            window = initWindow();
            colorful.colorize(window);
        } else {
            window = initWindow();
        }
    }

    public abstract Child initWindow();

    public Child getWindow() {
        return window;
    }
}