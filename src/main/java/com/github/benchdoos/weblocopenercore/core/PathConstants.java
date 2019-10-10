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

import java.io.File;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

public interface PathConstants {
    String APP_LOG_FOLDER_PATH = System
            .getProperty("java.io.tmpdir") + File.separator + WEBLOCOPENER_APPLICATION_NAME + File.separator + "Log";
    String APP_LOG_PROPERTY = "com.github.benchdoos.weblocopener.log.folder";

    String UPDATE_PATH_FILE = System.getProperty("java.io.tmpdir") + File.separator + WEBLOCOPENER_APPLICATION_NAME + File.separator;
}
