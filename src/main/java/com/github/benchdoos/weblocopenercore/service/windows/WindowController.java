package com.github.benchdoos.weblocopenercore.service.windows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Abstract class that gives ability to store and restore window status
 */
public abstract class WindowController {
    private ObjectMapper mapper;

    public WindowController() {
        mapper = new ObjectMapper();
    }
    public abstract void storeWindow(Window window) throws IOException;

    public void saveSettings(Set<WindowSetting> settings, File file) throws IOException {
        Assertions.assertThat(file).isNotNull();
        Assertions.assertThat(file.isDirectory()).isFalse();

        mapper.writeValue(file, settings);
    }


    public Set<WindowSetting> loadSettings(File file) throws IOException {
        return mapper.readValue(file, new TypeReference<Set<WindowSetting>>() {
        });
    }

    public abstract void loadWindow(Window window) throws IOException;
}
