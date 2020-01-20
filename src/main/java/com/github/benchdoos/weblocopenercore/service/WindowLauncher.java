package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.windows.DefaultWindowController;
import lombok.extern.log4j.Log4j2;

import java.awt.Window;

@Log4j2
public abstract class WindowLauncher<Child extends Window> {
    final Child window;
    private DefaultWindowController windowController;

    public WindowLauncher() {
        windowController = new DefaultWindowController();

        if (PreferencesManager.isDarkModeEnabledNow()) {
            final JColorful colorful = new JColorful(ApplicationConstants.DARK_MODE_THEME);
            colorful.colorizeGlobal();

            window = initWindow();
            colorful.colorize(window);
        } else {
            window = initWindow();
        }

        windowController.loadWindow(window);
    }

    public abstract Child initWindow();

    public Child getWindow() {
        return window;
    }
}
