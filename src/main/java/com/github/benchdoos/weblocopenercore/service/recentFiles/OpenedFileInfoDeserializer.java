package com.github.benchdoos.weblocopenercore.service.recentFiles;

import com.github.benchdoos.weblocopenercore.utils.browser.Browser;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class OpenedFileInfoDeserializer implements JsonDeserializer<OpenedFileInfo> {
    @Override
    public OpenedFileInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return null;
    }
}
