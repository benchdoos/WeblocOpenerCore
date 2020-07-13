package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;

import java.awt.Window;

/**
 * Gives ability to launch {@link Window} with {@link JColorful} if {@link PreferencesManager#isDarkModeEnabledNow()}
 * is true
 *
 * @param <Child> window to colorize and create
 */
public abstract class WindowLauncher<Child extends Window> {
    private final Child window;

    public WindowLauncher() {
        window = initWindow(); //todo remove window launcher
    }

    /**
     * Method creates instance of {@link WindowLauncher<Child>}.
     * DO NOT USE this method, use {@link #getWindow()} instead.
     *
     * @return instance of {@link WindowLauncher<Child>}.
     */
    public abstract Child initWindow();

    public Child getWindow() {
        return window;
    }
}
