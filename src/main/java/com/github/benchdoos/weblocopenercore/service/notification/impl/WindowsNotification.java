/*
 * (C) Copyright 2019.  Eugene Zrazhevsky and others.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 * Eugene Zrazhevsky <eugene.zrazhevsky@gmail.com>
 */

package com.github.benchdoos.weblocopenercore.service.notification.impl;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.notification.Notification;
import lombok.extern.log4j.Log4j2;

import javax.swing.Timer;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

@Log4j2
public class WindowsNotification implements Notification {
    @Override
    public void showInfoNotification(String title, String message) {
        showNotification(title, message, TrayIcon.MessageType.INFO);
    }

    @Override
    public void showWarningNotification(String title, String message) {
        showNotification(title, message, TrayIcon.MessageType.WARNING);

    }

    @Override
    public void showErrorNotification(String title, String message) {
        showNotification(title, message, TrayIcon.MessageType.ERROR);
    }

    private void showNotification(String title, String message, TrayIcon.MessageType messageType) {
        if (PreferencesManager.isNotificationsShown()) {
            createTrayMessage(title, message, messageType);
        }
    }

    private void createTrayMessage(String title, String message, TrayIcon.MessageType messageType) {
        final int delay = 7000;
        final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(
                WindowsNotification.class.getResource("/images/balloonIcon256.png")));
        trayIcon.setImageAutoSize(true);

        final SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
            trayIcon.displayMessage(title, message, messageType);

            PopupMenu menu = new PopupMenu();

            MenuItem close = new MenuItem(Translation.get("CommonsBundle", "closeButton"));
            close.addActionListener(e -> tray.remove(trayIcon));

            menu.add(close);

            trayIcon.setPopupMenu(menu);

            Timer timer = new Timer(delay, e -> tray.remove(trayIcon));
            timer.setRepeats(false);
            timer.start();
        } catch (AWTException e) {
            tray.remove(trayIcon);
            log.warn("Could not add icon to tray, removing from tray", e);
        }
    }
}
