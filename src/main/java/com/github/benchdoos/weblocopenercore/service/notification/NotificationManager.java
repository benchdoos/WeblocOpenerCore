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

package com.github.benchdoos.weblocopenercore.service.notification;

import com.github.benchdoos.weblocopenercore.gui.ForcedNotificationDialog;
import com.github.benchdoos.weblocopenercore.service.notification.impl.UnixNotification;
import com.github.benchdoos.weblocopenercore.service.notification.impl.WindowsNotification;
import com.github.benchdoos.weblocopenercore.utils.system.OS;

import java.awt.Component;

public class NotificationManager {
    private static final Notification DEFAULT_NOTIFICATION = new WindowsNotification();

    public static Notification getNotificationForCurrentOS() {
        if (OS.isWindows()) {
            return new WindowsNotification();
        } else if (OS.isUnix()) {
            return new UnixNotification();
        } else return DEFAULT_NOTIFICATION;
    }

    public static Notification getForcedNotification(Component component) {
        return new ForcedNotificationDialog(component);
    }
}
