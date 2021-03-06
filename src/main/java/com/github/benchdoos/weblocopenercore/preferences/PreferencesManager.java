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

package com.github.benchdoos.weblocopenercore.preferences;

import com.github.benchdoos.linksupport.links.Link;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.DarkModeAnalyzer;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.SimpleTime;
import com.github.benchdoos.weblocopenercore.service.links.LinkFactory;
import lombok.extern.log4j.Log4j2;

import javax.swing.JOptionPane;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.OPENER_COPY_LINK_ARGUMENT;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.OPENER_COPY_QR_ARGUMENT;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.OPENER_EDIT_ARGUMENT;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.OPENER_OPEN_ARGUMENT;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.OPENER_QR_ARGUMENT;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument.UNIX_DEFAULT_OPEN_MODE_ARGUMENT;

/**
 * Created by Eugene Zrazhevsky on 19.11.2016.
 */
@Log4j2
public class PreferencesManager {
    public static final String KEY_AUTO_UPDATE = "auto_update_enabled";
    private static final String KEY_BETA_UPDATE_INSTALL = "install_beta_updates";
    private static final String KEY_OPEN_FOR_QR = "open_folder_for_qr";
    private static final String KEY_BROWSER = "browser";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String DEV_MODE_KEY = "dev_mode";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_LOCALE = "locale";
    private static final String KEY_UNIX_OPENING_MODE = "unix_open_mode";
    private static final String KEY_LATEST_UPDATE_CHECK = "last_update_check";
    private static final String KEY_URL_PROCESSOR = "url_processor";
    private static final String KEY_OPEN_FOR_NEW_FILE = "open_folder_for_new_file";
    private static final String KEY_RECENT_OPENED_FILES_HISTORY_ENABLED = "recent_opened_files_history_enabled";
    private static final String KEY_MINIMAL_LIST_MODE_ENABLED = "minimal_list_mode";
    private static final String KEY_SHARE_USER_INFO_ENABLED = "share_anonymous_info_enabled";
    private static final String KEY_APPLICATION_UUID = "application_uuid";

    private static final Preferences PREFERENCES = Preferences.userRoot().node(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME.toLowerCase());

    private static volatile SimpleTime lastDarkModeUpdateTime = null;
    private static volatile boolean lastDarkModeEnabled = false;

    public static String getBrowserValue() {
        final String value = PREFERENCES.get(KEY_BROWSER, SettingsConstants.BROWSER_DEFAULT_VALUE);
        if (value.isEmpty()) {
            return SettingsConstants.BROWSER_DEFAULT_VALUE;
        }
        return value;
    }

    public static void setBrowserValue(String callPath) {
        if (!callPath.isEmpty()) {
            PREFERENCES.put(KEY_BROWSER, callPath);
        }
    }

    public static void flushPreferences() {
        try {
            PREFERENCES.flush();
            log.info("Preferences flushed, new settings applied immediately");
        } catch (BackingStoreException e) {
            log.warn("Could not flush preferences immediately", e);
        }
    }

    private static String getDarkMode() {
        final String s = PREFERENCES.get(KEY_DARK_MODE, SettingsConstants.DARK_MODE_DEFAULT_VALUE.toString());
        if (s.equalsIgnoreCase(DARK_MODE.ALWAYS.toString())) {
            return DARK_MODE.ALWAYS.toString();
        } else if (s.equalsIgnoreCase(DARK_MODE.DISABLED.toString())) {
            return DARK_MODE.DISABLED.toString();
        } else {
            return s;
        }
    }

    public static void setDarkMode(String value) {
        PREFERENCES.put(KEY_DARK_MODE, value);
    }

    public static Locale getLocale() {
        final String s = PREFERENCES.get(KEY_LOCALE, SettingsConstants.LOCALE_DEFAULT_VALUE);

        try {
            if (s.equalsIgnoreCase(SettingsConstants.LOCALE_DEFAULT_VALUE)) {
                return Locale.getDefault();
            } else {
                final String[] split = s.split("_");
                return new Locale(split[0], split[1]);
            }
        } catch (Exception e) {
            return Locale.getDefault();
        }
    }

    public static void setLocale(Locale locale) {
        if (locale != null) {
            PREFERENCES.put(KEY_LOCALE, locale.toString());
        } else {
            PREFERENCES.put(KEY_LOCALE, SettingsConstants.LOCALE_DEFAULT_VALUE);
        }
    }

    public static Object getRealDarkMode() {
        final String s = PREFERENCES.get(KEY_DARK_MODE, SettingsConstants.DARK_MODE_DEFAULT_VALUE.toString());
        if (s.equalsIgnoreCase(DARK_MODE.ALWAYS.toString())) {
            return Boolean.TRUE;
        } else if (s.equalsIgnoreCase(DARK_MODE.DISABLED.toString())) {
            return Boolean.FALSE;
        } else {
            try {
                return DarkModeAnalyzer.getDarkModeValue(s);
            } catch (Exception e) {
                return SettingsConstants.DARK_MODE_DEFAULT_VALUE == DARK_MODE.ALWAYS;
            }
        }
    }

    public static boolean isAutoUpdateActive() {
        return PREFERENCES.getBoolean(KEY_AUTO_UPDATE, SettingsConstants.IS_APP_AUTO_UPDATE_DEFAULT_VALUE);
    }

    public static void setAutoUpdateActive(boolean autoUpdateActive) {
        PREFERENCES.putBoolean(KEY_AUTO_UPDATE, autoUpdateActive);
    }

    public static boolean isBetaUpdateInstalling() {
        return PREFERENCES.getBoolean(KEY_BETA_UPDATE_INSTALL, SettingsConstants.IS_APP_BETA_UPDATE_INSTALLING_DEFAULT_VALUE);
    }

    public static void setBetaUpdateInstalling(boolean betaUpdatesInstalling) {
        PREFERENCES.putBoolean(KEY_BETA_UPDATE_INSTALL, betaUpdatesInstalling);
    }

    public static boolean isDarkModeEnabledNow() {
        switch (getDarkMode()) {
            case "ALWAYS":
                return true;
            case "DISABLED":
                return false;
            default: {
                try {
                    final Calendar instance = Calendar.getInstance();
                    final int startHour = instance.toInstant().atZone(ZoneId.systemDefault()).getHour();
                    final int startMinute = instance.toInstant().atZone(ZoneId.systemDefault()).getMinute();
                    SimpleTime time = new SimpleTime(startHour, startMinute);

                    if (lastDarkModeUpdateTime != null) {
                        if (lastDarkModeUpdateTime.equals(time)) {
                            return lastDarkModeEnabled;
                        } else {
                            lastDarkModeUpdateTime = time;
                            final boolean darkModeEnabledByNotDefaultData = DarkModeAnalyzer.isDarkModeEnabledByNotDefaultData(getDarkMode());
                            lastDarkModeEnabled = darkModeEnabledByNotDefaultData;
                            return darkModeEnabledByNotDefaultData;
                        }
                    } else {
                        lastDarkModeUpdateTime = time;
                        final boolean darkModeEnabledByNotDefaultData = DarkModeAnalyzer.isDarkModeEnabledByNotDefaultData(getDarkMode());
                        lastDarkModeEnabled = darkModeEnabledByNotDefaultData;
                        return darkModeEnabledByNotDefaultData;

                    }

                } catch (Exception e) {
                    return SettingsConstants.DARK_MODE_DEFAULT_VALUE == DARK_MODE.ALWAYS;
                }

            }
        }
    }

    public static boolean isDevMode() {
        return PREFERENCES.getBoolean(DEV_MODE_KEY, false);
    }

    public static boolean isNotificationsShown() {
        return PREFERENCES.getBoolean(KEY_NOTIFICATIONS, SettingsConstants.SHOW_NOTIFICATIONS_TO_USER);
    }

    public static void setNotificationsShown(boolean showNotifications) {
        PREFERENCES.putBoolean(KEY_NOTIFICATIONS, showNotifications);
    }

    public static boolean openFolderForQrCode() {
        return PREFERENCES.getBoolean(KEY_OPEN_FOR_QR, SettingsConstants.OPEN_FOLDER_FOR_QR_CODE);
    }

    public static void setOpenFolderForQrCode(boolean openFolderForQrCode) {
        PREFERENCES.putBoolean(KEY_OPEN_FOR_QR, openFolderForQrCode);
    }

    public static void setOpenFolderForNewFile(boolean value) {
        PREFERENCES.putBoolean(KEY_OPEN_FOR_NEW_FILE, value);
    }


    public static boolean openFolderForNewFile() {
        return PREFERENCES.getBoolean(KEY_OPEN_FOR_NEW_FILE, SettingsConstants.OPEN_FOLDER_FOR_NEW_FILE);
    }

    public static ApplicationArgument getUnixOpeningMode() {
        final String modeArgument = PREFERENCES.get(
                KEY_UNIX_OPENING_MODE,
                SettingsConstants.OPENER_UNIX_DEFAULT_SELECTOR_MODE.getArgument());

        final ApplicationArgument argument = ApplicationArgument.getByArgument(modeArgument);
        return argument != null ? argument : SettingsConstants.OPENER_UNIX_DEFAULT_SELECTOR_MODE;
    }

    public static void setUnixOpeningMode(ApplicationArgument mode) {
        switch (mode) {
            case OPENER_OPEN_ARGUMENT:
            case OPENER_EDIT_ARGUMENT:
            case OPENER_QR_ARGUMENT:
            case OPENER_COPY_LINK_ARGUMENT:
            case OPENER_COPY_QR_ARGUMENT:
            case UNIX_DEFAULT_OPEN_MODE_ARGUMENT:
                PREFERENCES.put(KEY_UNIX_OPENING_MODE, mode.getArgument());
                break;
            default:
                log.warn("Can not save mode: {}, supported modes are: {},{},{},{},{},{}", mode,
                        OPENER_OPEN_ARGUMENT,
                        OPENER_EDIT_ARGUMENT,
                        OPENER_QR_ARGUMENT,
                        OPENER_COPY_LINK_ARGUMENT,
                        OPENER_COPY_QR_ARGUMENT,
                        UNIX_DEFAULT_OPEN_MODE_ARGUMENT);
                break;

        }
    }

    public static Date getLatestUpdateCheck() {
        final long aLong = PREFERENCES.getLong(KEY_LATEST_UPDATE_CHECK, 0);

        return new Date(aLong);
    }

    public static void setLatestUpdateCheck(Date date) {
        PREFERENCES.putLong(KEY_LATEST_UPDATE_CHECK, date.getTime());
    }

    public static Link getLink() {
        final String result = PREFERENCES.get(KEY_URL_PROCESSOR, SettingsConstants.URL_PROCESSOR.toString());
        return LinkFactory.getLinkByName(result);
    }

    public static void setLink(Link link) {
        PREFERENCES.put(KEY_URL_PROCESSOR, LinkFactory.getNameByLink(link));
    }

    public static void setRecentOpenedFilesHistoryEnable(boolean enabled) {
        PREFERENCES.putBoolean(KEY_RECENT_OPENED_FILES_HISTORY_ENABLED, enabled);
    }

    public static boolean isRecentOpenedFilesHistoryEnabled() {
        return PREFERENCES.getBoolean(KEY_RECENT_OPENED_FILES_HISTORY_ENABLED, SettingsConstants.OPENED_PREVIOUSLY_FILES_HISTORY_ENABLED);
    }

    public static boolean isMinimalListModeEnabled() {
        return PREFERENCES.getBoolean(KEY_MINIMAL_LIST_MODE_ENABLED, SettingsConstants.MINIMAL_LIST_MODE_ENABLED);
    }

    public static void setMinimalListModeEnabled(boolean enabled) {
        PREFERENCES.putBoolean(KEY_MINIMAL_LIST_MODE_ENABLED, enabled);
    }

    public static boolean isShareAnonymousInfoEnabled() {
        try {
            if (preferencesContain(KEY_SHARE_USER_INFO_ENABLED)) {
                return PREFERENCES.getBoolean(KEY_SHARE_USER_INFO_ENABLED, SettingsConstants.SHARE_USER_INFO);
            } else {
                try {
                    Translation translation = new Translation("ShareAnonymousInfoBundle");
                    final int selected = JOptionPane.showOptionDialog(
                            null,
                            translation.get("message"),
                            ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            new String[]{
                                    translation.get("disableButton"),
                                    translation.get("enableButton")
                            },
                            translation.get("enableButton"));

                    final boolean enabled = selected == 1;

                    setShareAnonymousInfoEnabled(enabled);
                    flushPreferences();

                    return enabled;
                } catch (Exception e) {
                    return SettingsConstants.SHARE_USER_INFO;
                }
            }
        } catch (BackingStoreException e) {
            return SettingsConstants.SHARE_USER_INFO;
        }
    }

    public static void setShareAnonymousInfoEnabled(boolean enabled) {
        PREFERENCES.putBoolean(KEY_SHARE_USER_INFO_ENABLED, enabled);
    }

    public static UUID getApplicationUuid() {

        final UUID alternative = UUID.randomUUID();

        try {
            if (preferencesContain(KEY_APPLICATION_UUID)) {
                final String name = PREFERENCES.get(KEY_APPLICATION_UUID, alternative.toString());
                return UUID.fromString(name);
            } else {
                PREFERENCES.put(KEY_APPLICATION_UUID, alternative.toString());
            }
        } catch (BackingStoreException e) {
            PREFERENCES.put(KEY_APPLICATION_UUID, alternative.toString());
        }

        return alternative;
    }

    private static boolean preferencesContain(String key) throws BackingStoreException {
        return Arrays.asList(PREFERENCES.keys()).contains(key);
    }

    public enum DARK_MODE {ALWAYS, DISABLED;}
}

