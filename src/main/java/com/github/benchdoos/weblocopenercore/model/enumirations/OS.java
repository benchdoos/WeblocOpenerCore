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

package com.github.benchdoos.weblocopenercore.model.enumirations;

import lombok.RequiredArgsConstructor;
import org.apache.tika.utils.SystemUtils;

@RequiredArgsConstructor
public enum OS {
    WINDOWS,
    MAC_OS,
    UNIX,
    UNSUPPORTED;

    public static String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    public static boolean isMac() {
        return SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX;
    }

    public static boolean isUnix() {
        return SystemUtils.IS_OS_UNIX;
    }

    public static OS getCurrentOS() {
        if (isWindows()) {
            return WINDOWS;
        } else if (isMac()) {
            return MAC_OS;
        } else if (isUnix()) {
            return UNIX;
        }

        return UNSUPPORTED;
    }
}
