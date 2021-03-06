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

import com.github.benchdoos.weblocopenercore.utils.system.OS;

public interface ApplicationConstants {
    String WEBLOCOPENER_APPLICATION_NAME = "WeblocOpener";
    String CORE_NAME = "WeblocOpenerCore";

    String DEFAULT_APPLICATION_CHARSET = "UTF-8";

    OS[] SUPPORTED_OS = new OS[]{OS.WINDOWS, OS.UNIX};
}
