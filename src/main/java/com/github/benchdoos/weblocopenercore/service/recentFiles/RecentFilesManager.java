package com.github.benchdoos.weblocopenercore.service.recentFiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
//todo move to jackson
public class RecentFilesManager {
    private static final File historyFile = new File(PathConstants.RECENT_OPENED_FILES_FILE_PATH);

    public RecentFilesManager() {
        log.info("Processing recent files. History file location: {}", historyFile);
    }

    public Set<OpenedFileInfo> loadRecentOpenedFilesList() {
        if (historyFile.exists() && historyFile.isFile()) {
            try {
                final ObjectMapper mapper = new ObjectMapper();

                return (Set<OpenedFileInfo>) mapper.readValue(historyFile, Set.class);
            } catch (IOException e) {
                log.warn("Could not load file list", e);
            }
        }
        return Collections.emptySet();
    }

    public void appendRecentOpenedFile(OpenedFileInfo file) throws IOException {
        appendRecentOpenedFile(Collections.singletonList(file));
    }

    public void appendRecentOpenedFile(List<OpenedFileInfo> files) throws IOException {
        //todo fix linkedHashMap issue
        final Set<OpenedFileInfo> openedFileInfos = loadRecentOpenedFilesList();
        final Set<OpenedFileInfo> result = new HashSet<>(openedFileInfos);
        result.addAll(files);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(historyFile, result);
    }

}
