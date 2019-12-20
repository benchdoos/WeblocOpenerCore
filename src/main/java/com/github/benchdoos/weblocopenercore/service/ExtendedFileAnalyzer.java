package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.linksupport.links.Link;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.exceptions.UnsupportedFileFormatException;
import com.github.benchdoos.weblocopenercore.gui.FileChooser;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.links.LinkUtilities;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import lombok.extern.log4j.Log4j2;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
//todo implement all this
public class ExtendedFileAnalyzer implements FileAnalyzer {
    private final String filePath;

    public ExtendedFileAnalyzer(@NotNull String filePath) {
        this.filePath = filePath;
    }

    @Override
    public LinkFile getUrl() {
        final File file = getFileIfExists(filePath);

        validateExistedFile(file);


        return null;
    }

    private File getFileIfExists(String filePath) {
        final File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file;
        } else {
            final ArrayList<File> files = getFilesThatCanBeSelected(filePath);

            File chosen = null;
            if (files != null) {
                if (files.size() > 1) {
                    final FileChooser fileChooser;
                    //todo check if can be moved to WindowLauncher
                    if (PreferencesManager.isDarkModeEnabledNow()) {
                        final JColorful colorful = new JColorful(ApplicationConstants.DARK_MODE_THEME);
                        colorful.colorizeGlobal();
                        fileChooser = new FileChooser(files);
                        SwingUtilities.invokeLater(() -> colorful.colorize(fileChooser));
                    } else {
                        fileChooser = new FileChooser(files);
                    }
                    FrameUtils.setWindowOnScreenCenter(fileChooser);
                    fileChooser.setVisible(true);
                    chosen = fileChooser.getChosenFile();
                } else if (files.size() == 1) {
                    chosen = files.get(0);
                } else {
                    log.warn("Could not find files that math... or something went wrong: {}", files);
                }
            }

            if (chosen != null) {
                if (chosen.exists()) {
                    return chosen;
                }
            } else {
                log.debug("User canceled selection from file list");
            }
        }
        return null;
    }

    /**
     * Returns a <code>.webloc</code> file from path.
     *
     * @param arg File path.
     * @return <code>.webloc</code> file.
     */
    private ArrayList<File> getFilesThatCanBeSelected(final String arg) {

        final String extension = FilenameUtils.getExtension(arg);

        final Link linkByExtension = Link.getByExtension(extension);

        if (linkByExtension != null) {

            final File currentFile = new File(arg);
            log.info("File [" + arg + "] exists: " + currentFile.exists() + " file?: " + currentFile.isFile());

            if (currentFile.isFile() && currentFile.exists()) {

                if (FilenameUtils.getExtension(currentFile.getName()).equals(linkByExtension.getExtension())) {
                    log.info("File added to proceed: " + currentFile.getAbsolutePath());
                    final ArrayList<File> arrayList = new ArrayList<>();
                    arrayList.add(currentFile);
                    return arrayList;
                } else {
                    log.warn("Wrong argument. File extension is not webloc: " + currentFile.getAbsolutePath());
                }

            } else {

                log.warn("Wrong argument. Invalid file path or file not exist: " + currentFile.getAbsolutePath());
                log.info("Trying to guess what file user wants to open");
                final ArrayList<File> files = findOpeningFile(currentFile, linkByExtension);
                log.info("Got files, that match: {}", files);
                return files;
            }
        }
        return null;
    }

    private ArrayList<File> findOpeningFile(final File file, Link link) {
        final File parent = file.getParentFile();
        if (parent.exists() && parent.isDirectory()) {
            final ArrayList<ComparedFile> values = new ArrayList<>();

            final File[] files = parent.listFiles();
            if (files != null) {
                for (File current : files) {
                    if (FileUtils.getFileExtension(current).equalsIgnoreCase(link.getExtension())) {
                        int compared = compareFileNames(file, current);
                        values.add(new ComparedFile(compared, current));
                    }
                }
                final int maximumValue = getMaximumValue(values);

                return getAllFilesWithMaximumValue(maximumValue, values);
            }
        }
        return null;
    }

    private ArrayList<File> getAllFilesWithMaximumValue(final int maximumValue,
                                                        @NotNull final ArrayList<ComparedFile> values) {
        final List<File> collect = values.stream()
                .filter(c -> c.getEqualSymbols() == maximumValue)
                .map(ComparedFile::getFile)
                .collect(Collectors.toList());

        return collect.size() > 0 ? new ArrayList<>(collect) : null;
    }

    private int compareFileNames(final File fileOriginal, final File fileComparing) {
        final String originalFileName = fileOriginal.getName();
        final String comparingFileName = fileComparing.getName();
        return FuzzySearch.ratio(originalFileName, comparingFileName);
    }

    private int getMaximumValue(final ArrayList<ComparedFile> values) {
        return values.stream()
                .mapToInt(ComparedFile::getEqualSymbols)
                .filter(comparedFile -> comparedFile >= 0)
                .max().orElse(0);
    }

    private void validateExistedFile(File file) {
        if (!LinkUtilities.isFileSupported(FileUtils.getFileExtension(file))) {
            throw new UnsupportedFileFormatException(file.getPath());
        }
    }
}
