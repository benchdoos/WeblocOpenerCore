package com.github.benchdoos.weblocopenercore.service.recentFiles;

import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import com.github.benchdoos.weblocopenercore.utils.browser.BrowserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Log4j2
//todo move to jackson
public class RecentFilesManager {
    private static final File historyFile = new File(PathConstants.RECENT_OPENED_FILES_FILE_PATH);

    public RecentFilesManager() {
        log.info("Processing recent files. History file location: {}", historyFile);
    }

    public List<OpenedFileInfo> loadRecentOpenedFilesList() {
        if (historyFile.exists() && historyFile.isFile()) {
            try {
                final String jsonString = IOUtils.toString(
                        BrowserManager.class.getResourceAsStream(historyFile.getAbsolutePath()),
                        StandardCharsets.UTF_8);
                //todo load file
            } catch (IOException e) {
                log.warn("Could not load file list", e);
            }
        }
        return Collections.emptyList();
    }

    public void appendRecentOpenedFile(OpenedFileInfo file) throws IOException {
        appendRecentOpenedFile(Collections.singletonList(file));
    }

    public void appendRecentOpenedFile(List<OpenedFileInfo> files) throws IOException {
        JsonElement rootElement = null;

        rootElement = readJsonAndGetRootJsonElement(rootElement);

        if (rootElement == null) {
                rootElement = new JsonArray();
            }

            final JsonArray rootArray = rootElement.getAsJsonArray();

            final JsonArray arrayToAppend = new JsonArray();

            files.forEach(file -> {
                final JsonObject element = new JsonObject();
                element.addProperty("type", file.getType().toString());
                element.addProperty("filename", file.getFilename());
                element.addProperty("filePath", file.getFilePath().toString());

                arrayToAppend.add(element);
            });
            rootArray.addAll(arrayToAppend);

        writeRootJsonElementToFile(rootElement);
    }

    private void writeRootJsonElementToFile(JsonElement rootElement) throws IOException {
        try (final FileWriter fileWriter = new FileWriter(historyFile)){
            new GsonBuilder().create().toJson(rootElement, fileWriter);
        }
    }

    private JsonElement readJsonAndGetRootJsonElement(JsonElement rootElement) throws IOException {
        try (final FileReader fileReader = new FileReader(historyFile);) {
            final GsonBuilder builder = new GsonBuilder();
            final Gson gson = builder.create();

            if (historyFile.exists()) {
                try {
                    rootElement = gson.fromJson(fileReader, JsonElement.class);
                } catch (Exception e) {
                    log.warn("Could not parse get json string from existed file: {}", historyFile, e);
                    rootElement = new JsonArray();
                }
            }
        }
        return rootElement;
    }
}
