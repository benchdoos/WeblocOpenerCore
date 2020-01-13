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

import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.system.OperatingSystem;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;

import java.io.File;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;


public interface PathConstants {

    String ROOT_PATH = SystemUtils.getCurrentOS().equals(OperatingSystem.OS.WINDOWS) ?
            System.getProperty("java.io.tmpdir") + File.separator + WEBLOCOPENER_APPLICATION_NAME :
            System.getProperty("user.home") + File.separator + "." + WEBLOCOPENER_APPLICATION_NAME;

    String APP_LOG_FOLDER_PATH = ROOT_PATH + File.separator + "Log";
    String APP_LOG_PROPERTY = "com.github.benchdoos.weblocopenercore.log.folder";

    String RECENT_OPENED_FILES_FILE_PATH = ROOT_PATH + File.separator
            + CoreUtils.fixFileName("recent-files-" + System.getProperty("user.name").toLowerCase() + ".json");
}
