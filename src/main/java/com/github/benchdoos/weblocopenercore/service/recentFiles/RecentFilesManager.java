package com.github.benchdoos.weblocopenercore.service.recentFiles;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
public class RecentFilesManager {
    private static final File historyFile = new File(PathConstants.RECENT_OPENED_FILES_FILE_PATH);

    public RecentFilesManager() {
        log.info("Processing recent files. History file location: {}", historyFile);
    }

    public Set<OpenedFileInfo> loadRecentOpenedFilesList() {
        if (historyFile.exists() && historyFile.isFile()) {
            try {
                final ObjectMapper mapper = new ObjectMapper();

                return mapper.readValue(historyFile, new TypeReference<Set<OpenedFileInfo>>() {
                });
            } catch (IOException e) {
                log.warn("Could not load file list", e);
            }
        } else {
            log.warn("Can not get recently opened files list. History file exists: {}, is file: {}. Returning empty list.",
                    historyFile.exists(),
                    historyFile.isFile());
        }
        return Collections.emptySet();
    }

    public void appendRecentOpenedFile(OpenedFileInfo file) throws IOException {
        appendRecentOpenedFile(Collections.singletonList(file));
    }

    public void appendRecentOpenedFile(List<OpenedFileInfo> files) throws IOException {
        log.info("Appending recent file to list. Collection: {}", CollectionUtils.isNotEmpty(files) ? files.size() : files);
        final Set<OpenedFileInfo> openedFileInfos = loadRecentOpenedFilesList();

        log.debug("Recent files list size: {}", openedFileInfos.size());

        final Set<OpenedFileInfo> result = new HashSet<>(openedFileInfos);
        result.addAll(files);

        log.debug("Updated size is: {}", result.size());

        saveFilesList(result);

        log.info("Recent files appended");
    }

    /**
     * Saves given set to file
     * @param files set of files
     * @throws IOException if can not write a file
     */
    private void saveFilesList(Set<OpenedFileInfo> files) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(historyFile, files);
    }

    public boolean cleanup() {
        log.info("Removing recent opened files list");
        final boolean delete = historyFile.delete();
        log.info("File was deleted: {}", delete);
        return delete;
    }

    public boolean historyExistsForCurrentUser() {
        return historyFile.exists();
    }

    public Set<OpenedFileInfo> removeFiles(List<OpenedFileInfo> selectedValuesList) throws IOException {
        final Set<OpenedFileInfo> openedFileInfos = loadRecentOpenedFilesList();
        openedFileInfos.removeAll(selectedValuesList);
        saveFilesList(openedFileInfos);
        return openedFileInfos;
    }
}
