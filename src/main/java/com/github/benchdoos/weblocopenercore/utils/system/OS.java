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

package com.github.benchdoos.weblocopenercore.utils.system;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum OS {
    WINDOWS(Collections.singletonList("win")),
    MAC_OS(Collections.singletonList("mac")),
    UNIX(Arrays.asList("nix", "nux", "aix")),
    SOLARIS(Collections.singletonList("sunos")),
    UNSUPPORTED(Collections.emptyList());

    private final List<String> names;

    public static String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindows() {
        return (WINDOWS.names.contains(getOsName()));
    }

    public static boolean isMac() {
        return (MAC_OS.names.contains(getOsName()));
    }

    public static boolean isUnix() {
        return getCurrentOS() == OS.UNIX;
    }

    public static boolean isSolaris() {
        return (SOLARIS.names.contains(getOsName()));
    }

    public static OS getCurrentOS() {
        for (OS current : OS.values()) {
            for (String name : current.names) {
                if (getOsName().toLowerCase().contains(name)) {
                    return current;
                }
            }
        }
        return UNSUPPORTED;
    }
}
