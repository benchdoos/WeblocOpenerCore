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


import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.utils.Internal;
import lombok.extern.log4j.Log4j2;

import static com.github.benchdoos.weblocopenercore.utils.system.OS.getOsName;
import static com.github.benchdoos.weblocopenercore.utils.system.OS.isWindows;

@Log4j2
public class SystemUtils {
    private static final String MINIMUM_WINDOWS_VERSION = "5.1"; //Windows XP

    private static final String CURRENT_OS_VERSION = getOsVersion();
    public static final boolean IS_WINDOWS_XP = isWindows()
            && Internal.versionCompare(SystemUtils.CURRENT_OS_VERSION, "5.1") >= 0
            && Internal.versionCompare(SystemUtils.CURRENT_OS_VERSION, "6.0") < 0;
    private static final OS CURRENT_OS = OS.getCurrentOS();


    private static String getOsVersion() {
        return System.getProperty("os.version");
    }

    private static String getRealSystemArch() {
        if (OS.isWindows()) {
            String arch = System.getenv("PROCESSOR_ARCHITECTURE");
            String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

            return arch.endsWith("64")
                    || wow64Arch != null && wow64Arch.endsWith("64")
                    ? "64" : "32";
        } else return System.getProperty("os.arch");
    }

    public static void checkIfSystemIsSupported() throws UnsupportedSystemException {
        initSystem();
        if (isSupported()) {
            if (CURRENT_OS == OS.WINDOWS) {
                checkWindows();
            }
        } else {
            log.warn(getOsName() + " v" + getOsVersion() + " is not supported yet.");
            throw new UnsupportedSystemException();
        }
    }

    private static void initSystem() {
        log.info("Initializing system...");
        try {
            log.info(getSystemParameters());
        } catch (Throwable e) {
            log.warn("Could not properly init system, but anyway continuing...", e);
        }
    }

    private static void checkWindows() throws UnsupportedSystemException {
        if (Internal.versionCompare(CURRENT_OS_VERSION, MINIMUM_WINDOWS_VERSION) < 0) {
            log.warn("Windows " + CURRENT_OS_VERSION + " is not supported yet.");
            throw new UnsupportedSystemException();
        }
    }

    private static boolean isSupported() {
        for (OS supportedSystems : ApplicationConstants.SUPPORTED_OS) {
            if (SystemUtils.CURRENT_OS.equals(supportedSystems)) {
                return true;
            }
        }
        return false;
    }

    private static String getSystemParameters() {
        try {
            int megabyte = 1024 * 1024;


            final Runtime runtime = Runtime.getRuntime();
            return "System: " +
                    "OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version")
                    + " x" + getRealSystemArch() + " " +
                    "Processors: " + runtime.availableProcessors() + " " +
                    "JVM memory: " + runtime.maxMemory() / megabyte + " MB (" +
                    "free:" + runtime.freeMemory() / megabyte + " MB) " +
                    "Java v." + System.getProperty("java.specification.version") + "(" +
                    System.getProperty("java.version") + ")" + " runtime v." +
                    System.getProperty("java.vm.version");
        } catch (Exception e) {
            log.warn("Could not show system parameters, proceeding", e);
            return "";
        }
    }

}
