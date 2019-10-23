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

package com.github.benchdoos.weblocopenercore.core.constants;

import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.SimpleTime;
import com.github.benchdoos.weblocopenercore.service.links.LinkFactory;
import com.github.benchdoos.weblocopenercore.utils.system.OperatingSystem;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;

public interface SettingsConstants {
    boolean IS_APP_AUTO_UPDATE_DEFAULT_VALUE = true;
    boolean IS_APP_BETA_UPDATE_INSTALLING_DEFAULT_VALUE = false;
    boolean OPEN_FOLDER_FOR_QR_CODE = true;
    boolean OPEN_FOLDER_FOR_NEW_FILE = false;
    boolean SHOW_NOTIFICATIONS_TO_USER = true;
    LinkFactory.LinkType URL_PROCESSOR = OperatingSystem.OS.WINDOWS
            .equals(SystemUtils.getCurrentOS()) ?
            LinkFactory.LinkType.url : (OperatingSystem.OS.UNIX
            .equals(SystemUtils.getCurrentOS()) ?
            LinkFactory.LinkType.desktop
            : LinkFactory.LinkType.binary_webloc);
    String BROWSER_DEFAULT_VALUE = "default";
    String CONVERTER_DEFAULT_EXTENSION = ApplicationConstants.URL_FILE_EXTENSION;
    PreferencesManager.DARK_MODE DARK_MODE_DEFAULT_VALUE = PreferencesManager.DARK_MODE.DISABLED;

    SimpleTime DARK_MODE_BEGINNING_DEFAULT_TIME = new SimpleTime(21, 0);
    SimpleTime DARK_MODE_ENDING_DEFAULT_TIME = new SimpleTime(7, 0);
    String LOCALE_DEFAULT_VALUE = "default";
    String OPENER_UNIX_DEFAULT_SELECTOR_MODE = "default";
}
