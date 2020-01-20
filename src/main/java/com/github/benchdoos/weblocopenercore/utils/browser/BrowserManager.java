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

package com.github.benchdoos.weblocopenercore.utils.browser;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.utils.system.OS;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Eugene Zrazhevsky on 24.08.2017.
 */
@Log4j2
public class BrowserManager {
    private static ArrayList<Browser> browserList = new ArrayList<>();

    public static ArrayList<Browser> getBrowserList() {
        return browserList;
    }

    private static ArrayList<Browser> getDefaultBrowsersList() {
        log.debug("Starting loading browser list");

        final ArrayList<Browser> result = new ArrayList<>();

        final GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Browser.class, new BrowserDeserializer());
        final Gson gson = builder.create();
        final String browserListJsonPath = getBrowserListJson();

        log.debug("Browser list path for current system is: {}", browserListJsonPath);

        try {
            final String content = IOUtils.toString(
                    BrowserManager.class.getResourceAsStream(browserListJsonPath),
                    StandardCharsets.UTF_8);

            final JsonElement element = new JsonParser().parse(content);
            final JsonObject asJsonObject = element.getAsJsonObject();
            final JsonArray browsersArray = asJsonObject.getAsJsonArray("browsers");

            log.debug("Browsers were found: {}", browsersArray);

            for (final JsonElement jsonBrowser : browsersArray) {
                final Browser browser = gson.fromJson(jsonBrowser, Browser.class);
                result.add(browser);
            }
        } catch (IOException e) {
            log.warn("Could not load browsers list: {}", browserListJsonPath, e);
        } catch (Exception e) {
            log.warn("Could not load browsers list: {}, ignoring it", browserListJsonPath, e);
        }

        log.info("Loaded browser list: {}", result);

        return result;
    }

    private static String getBrowserListJson() {
        final OS currentOS = OS.getCurrentOS();
        return "/data/" + currentOS.toString().toLowerCase() + "/browsers.json";
    }

    public static void loadBrowserList() {
        loadBrowsersFromDefault(getDefaultBrowsersList());
    }

    private static void loadBrowsersFromDefault(ArrayList<Browser> list) {
        log.info("Loading browser list. Given list: {}", list);
        browserList = list;
        browserList.add(0, new Browser(
                Translation.getTranslatedString("CommonsBundle", "defaultBrowserName"),
                SettingsConstants.BROWSER_DEFAULT_VALUE));
        log.debug("Browsers count: " + browserList.size() + " " + browserList);

    }
}
