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

package com.github.benchdoos.weblocopenercore.core;

import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Log4j2
public class Translation {
    private static Map<MessageInformation, Integer> deadLockProtection = new HashMap<>();

    public static final Locale[] SUPPORTED_LOCALES = {
            new Locale("en", "EN"), new Locale("de", "DE"),
            new Locale("fr", "FR"), new Locale("it", "IT"),
            new Locale("ru", "RU")};

    private static volatile Locale locale;
    private ResourceBundle messages;
    private final String bundlePath;
    private final String bundleName;

    public Translation(String bundleName) {
        locale = PreferencesManager.getLocale();
        bundleName = updateBundleName(bundleName);

        this.bundleName = bundleName;
        this.bundlePath = "translations/" + bundleName;
        this.messages = getTranslation();
    }

    /**
     * Returns translated string by bundle and message name. Supports deadlock protection
     *
     * @param stringBundleName name of bundle
     * @param message message name
     * @return translated string
     * @see #protectFromDeadLock(MessageInformation)
     */
    public static String get(String stringBundleName, String message) {
        stringBundleName = updateBundleName(stringBundleName);

        try {
            final String bundlePath = "translations/" + stringBundleName;
            locale = PreferencesManager.getLocale();


            log.trace("[TRANSLATION] Locale: {} {}; Bundle: {}:[{}]", locale.getCountry(),
                    locale.getLanguage(), stringBundleName, message);

            final ResourceBundle bundle = ResourceBundle.getBundle(bundlePath, locale);

            return bundle.getString(message);
        } catch (MissingResourceException e) {
            log.warn("Could not find bundle {}:[{}] for locale: {}, trying to get necessary locale",
                    stringBundleName, message, locale);

            protectFromDeadLock(new MessageInformation(locale, stringBundleName, message));

            final Locale supportedLocale = getSupportedLocale(locale);

            log.info("For old locale: {} was found locale: {}", locale, supportedLocale);
            log.info("APPLYING new locale: {}", supportedLocale);
            locale = supportedLocale;
            PreferencesManager.setLocale(supportedLocale);
            return get(stringBundleName, message);
        } catch (Exception e) {
            log.warn("Could not translate string: {}:[{}] for locale: {}", stringBundleName, message, locale, e);
            throw new RuntimeException("Could not localize string: " + stringBundleName + ":[" + message + "]", e);
        }
    }

    private static String updateBundleName(String stringBundleName) {
        final String bundle = "Bundle";
        if (!stringBundleName.contains(bundle)) {
            stringBundleName = stringBundleName + bundle;
        }
        return stringBundleName;
    }

    private static Locale getSupportedLocale(Locale locale) {
        log.info("Trying to get current locale for {}", locale);

        for (Locale currentLocale : SUPPORTED_LOCALES) {
            try {
                if (locale.getLanguage().equalsIgnoreCase(currentLocale.getLanguage())) {
                    return locale;
                }
            } catch (Exception e) {
                log.warn("Could not get supported locale for locale: {}; current is: {}", locale, currentLocale, e);
            }
        }
        log.warn("Could not get locale, switching to en_EN");
        return new Locale("en", "EN");
    }

    public String get(String message) {
        try {
            log.trace("[TRANSLATION] Translating message: {}", message);

            locale = PreferencesManager.getLocale();
            this.messages = getTranslation();

            return messages.getString(message);
        } catch (Exception e) {
            log.warn("Could not localize string: " + bundleName + ":[" + message + "]", e);
            return message;
        }
    }

    /**
     * Protects message seeking from deadlock. More then 3 iterations will break seeking by {@link RuntimeException}.
     *
     * @param messageInformation info about message
     */
    private static void protectFromDeadLock(MessageInformation messageInformation) {
        if (deadLockProtection.containsKey(messageInformation)) {
            final Integer current = deadLockProtection.get(messageInformation);
            if (current <= 3) {
                deadLockProtection.put(messageInformation, current + 1);
            } else {
                log.warn("[DEADLOCK PROTECTION] Could not translate string: {}:[{}] for locale: {}",
                        messageInformation.getBundleName(),
                        messageInformation.getMessage(),
                        messageInformation.getLocale());

                throw new RuntimeException("[DEADLOCK PROTECTION] Could not localize string: "
                        + messageInformation.getBundleName()
                        + ":[" + messageInformation.getMessage()
                        + "] for locale:[" + messageInformation.getLocale()+"]");
            }
        } else {
            deadLockProtection.put(messageInformation, 0);
        }
    }

    private ResourceBundle getTranslation() {
        return ResourceBundle.getBundle(bundlePath, locale);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class MessageInformation {
        private Locale locale;
        private String bundleName;
        private String message;
    }
}
