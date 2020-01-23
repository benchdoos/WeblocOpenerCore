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

import com.github.benchdoos.linksupport.links.Link;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.gui.AboutApplicationDialog;
import com.github.benchdoos.weblocopenercore.gui.ConverterDialog;
import com.github.benchdoos.weblocopenercore.gui.EditDialog;
import com.github.benchdoos.weblocopenercore.gui.SettingsDialog;
import com.github.benchdoos.weblocopenercore.gui.ShowQrDialog;
import com.github.benchdoos.weblocopenercore.gui.unix.ModeSelectorDialog;
import com.github.benchdoos.weblocopenercore.gui.wrappers.CreateNewFileDialogWrapper;
import com.github.benchdoos.weblocopenercore.gui.wrappers.CreateNewFileFrameWrapper;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.ExtendedFileAnalyzer;
import com.github.benchdoos.weblocopenercore.service.LinkFile;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.service.WindowLauncher;
import com.github.benchdoos.weblocopenercore.service.clipboard.ClipboardManager;
import com.github.benchdoos.weblocopenercore.service.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.service.recentFiles.OpenedFileInfo;
import com.github.benchdoos.weblocopenercore.service.recentFiles.RecentFilesManager;
import com.github.benchdoos.weblocopenercore.service.share.UserShareInfoService;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.browser.BrowserManager;
import com.github.benchdoos.weblocopenercore.utils.system.OS;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import lombok.extern.log4j.Log4j2;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Frame.MAXIMIZED_HORIZ;

@Log4j2
public class Application {
    private static final String CORRECT_CREATION_SYNTAX = "-create <file path> <url>";
    private static SettingsDialog settingsDialog;

    public Application(final String[] args) {
        printDevMode();
        log.info("{} starts with arguments: {}", ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME, Arrays.toString(args));

        BrowserManager.loadBrowserList();

        CoreUtils.enableLookAndFeel();

        shareUserInfo();

        if (args.length > 1) {
            manageArguments(args);
        } else if (args.length == 1) {
            manageSoloArgument(args);
        } else {
            runSettingsDialog(null);
        }

    }

    private void printDevMode() {
        if (PreferencesManager.isDevMode()) {
            log.fatal("[DEV MODE ACTIVATED]");
        }
    }

    private void shareUserInfo() {
        log.info("Checking if sharing anonymous info enabled");
        if (PreferencesManager.isShareAnonymousInfoEnabled()) {
            new Thread(() -> {
                try {
                    new UserShareInfoService().sendInfo(PreferencesManager.getApplicationUuid());
                } catch (IOException e) {
                    log.warn("Could not send user info.", e);
                }
            }).start();
        }

    }

    private static String helpText() {
        return ApplicationArgument.OPENER_CREATE_ARGUMENT.getArgument() + "\t[filepath] [link] \tCreates a new .webloc file on [filepath]. " +
                "[filepath] should end with [\\filename.webloc]\n" +
                ApplicationArgument.OPENER_EDIT_ARGUMENT.getArgument() + "\t[filepath] \t\t\tCalls Edit window to edit given .webloc file.\n" +
                ApplicationArgument.OPENER_SETTINGS_ARGUMENT.getArgument() + "\t\t\t\t\tCalls Settings window of WeblocOpener.\n" +
                ApplicationArgument.OPENER_UPDATE_ARGUMENT.getArgument() + "\t\t\t\t\t\tCalls update-tool for WeblocOpener.";
    }

    /**
     * Manages incoming arguments
     *
     * @param args App start arguments
     */
    public static void manageArguments(String[] args) {
        log.debug("Managing arguments: " + Arrays.toString(args));
        if (args.length > 0) {
            log.info("Got args: " + Arrays.toString(args));
            final String arg = args[0];

            final ApplicationArgument argument = ApplicationArgument.getByArgument(arg);

            if (argument != null) {
                switch (argument) {
                    case OPENER_OPEN_ARGUMENT:
                        runAnalyzer(args[1]);
                        break;
                    case OPENER_EDIT_ARGUMENT:
                        manageEditArgument(args);
                        break;
                    case OPENER_SETTINGS_ARGUMENT:
                        String launcherLocation = null;
                        if (args.length > 1) {
                            launcherLocation = args[1];
                        }
                        runSettingsDialog(launcherLocation);
                        break;

                    case OPENER_ABOUT_ARGUMENT:
                        new AboutApplicationDialog().setVisible(true);
                        break;

                    case OPENER_CREATE_NEW_ARGUMENT:
                        manageCreateNewArgument();
                        break;

                    case OPENER_CREATE_ARGUMENT:
                        try {
                            manageCreateArgument(args);
                        } catch (Exception e) {
                            log.warn("Can not create .webloc file (" + CORRECT_CREATION_SYNTAX + "): "
                                    + Arrays.toString(args), e);
                        }
                        break;
                    case OPENER_HELP_ARGUMENT_HYPHEN: {
                        System.out.println(helpText());
                        break;
                    }
                    case OPENER_QR_ARGUMENT:
                        if (args.length > 1) {
                            runQrDialog(args[1]);
                        }
                        break;
                    case OPENER_COPY_LINK_ARGUMENT:
                        if (args.length > 1) {
                            runCopyLink(args[1]);
                        }
                        break;

                    case OPENER_COPY_QR_ARGUMENT:
                        runCopyQrCode(args);
                        break;

                    case OPENER_UPDATE_ARGUMENT:
                    case UPDATE_SILENT_ARGUMENT:
                        log.warn("UPDATE IS NOT SUPPORTED BY CORE! Argument: {}", arg);
                        break;
                    case OPENER_CONVERT_ARGUMENT:
                        runConverterDialog(args);
                        break;
                    default:
                        runAnalyzer(arg);
                        break;
                }
            } else {
                log.info("No default argument ({}) detected at argument at index 0. ", arg);
                runAnalyzer(arg);
            }
        } else {
            log.debug("No arguments found, launching settings");
            runSettingsDialog(null);
        }
    }

    private static void runConverterDialog(String[] args) {
        if (args.length > 1) {

            final List<File> files = new ArrayList<>();
            Arrays.stream(args).forEach(c -> {
                final File file = new File(c);
                final boolean exists = file.exists();
                if (exists) {
                    files.add(file);
                }
            });

            final WindowLauncher<ConverterDialog> windowLauncher = new WindowLauncher<ConverterDialog>() {
                @Override
                public ConverterDialog initWindow() {
                    return new ConverterDialog(files);
                }
            };

            final ConverterDialog converterDialog = windowLauncher.getWindow();
            converterDialog.setVisible(true);
            converterDialog.setLocationRelativeTo(null);
        } else {
            log.warn("Converter need some files! Args: {}", Arrays.asList(args));
            final String notificationString = Translation.getTranslatedString("ConvertDialogBundle", "convertError");
            NotificationManager.getNotificationForCurrentOS()
                    .showErrorNotification(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME, notificationString);
        }
    }

    private static void runAnalyzer(String arg) {
        try {
            final LinkFile linkFile = new ExtendedFileAnalyzer(arg).getLinkFile();
            final String url = linkFile.getUrl().toString();

            if (!url.isEmpty()) {

                saveRecentFileRecord(new File(arg));

                UrlsProceed.openUrl(url);
            } else {
                runEditDialog(arg);
            }
        } catch (Exception e) {
            log.warn("Could not open file: {}", arg, e);
            final File file = new File(arg);

            if (OS.isUnix()) {
                if (Link.DESKTOP_LINK.getExtension().equals(org.apache.logging.log4j.core.util.FileUtils.getFileExtension(file))) {
                    NotificationManager.getNotificationForCurrentOS()
                            .showWarningNotification("Can not open file: " + file.getName(),
                                    "Opening in nautilus");
                    try {
                        FileUtils.openFileInNautilusUnix(file);
                    } catch (IOException ex) {
                        log.warn("Can not open file in nautilus", ex);
                    }
                }
            }
        }
    }

    private static void manageCreateNewArgument() {
        final CreateNewFileDialogWrapper createNewFileDialogWrapper = new WindowLauncher<CreateNewFileDialogWrapper>() {
            @Override
            public CreateNewFileDialogWrapper initWindow() {
                return new CreateNewFileDialogWrapper();
            }
        }.getWindow();
        FrameUtils.setWindowOnScreenCenter(createNewFileDialogWrapper);
        createNewFileDialogWrapper.setVisible(true);
    }

    private static void manageCreateArgument(String[] args) throws Exception {
        String filePath;
        String url;
        if (args.length > 2) {
            filePath = args[1];
            url = args[2];
            if (args.length > 3) {
                log.info("You can create only one link in one file. Creating.");
            }
            PreferencesManager.getLink()
                    .getLinkProcessor()
                    .createLink(new URL(url), new FileOutputStream(new File(filePath)));
        } else {
            throw new IllegalArgumentException("Not all arguments (" + CORRECT_CREATION_SYNTAX + "):" + Arrays.toString(args));
        }
    }

    /**
     * Manages edit argument, runs edit-updateMode to file in second argument
     *
     * @param args main args
     */
    private static void manageEditArgument(String[] args) {
        if (args.length > 1) {
            final String path = args[1];
            final File file;
            try {
                file = new ExtendedFileAnalyzer(path).getLinkFile().getFile();
                if (file != null) {

                    saveRecentFileRecord(file);

                    runEditDialog(file.getAbsolutePath());
                }
            } catch (Exception e) {
                log.warn("Could not edit file: {}", path, e);
            }
        } else {
            showIncorrectArgumentMessage(ApplicationArgument.OPENER_EDIT_ARGUMENT.getArgument());
        }
    }

    public static void saveRecentFileRecord(File file) throws IOException {
        if (PreferencesManager.isRecentOpenedFilesHistoryEnabled()) {
            final OpenedFileInfo fileInfo = OpenedFileInfo.fromFile(file);
            if (fileInfo != null) {
                new RecentFilesManager().appendRecentOpenedFile(fileInfo);
            }
        }
    }

    private static void showIncorrectArgumentMessage(String argument) {
        Translation translation = new Translation("CommonsBundle");
        final String message = translation.getTranslatedString("incorrectArgument").replace("{}", argument);
        NotificationManager.getForcedNotification(null).showErrorNotification(
                translation.getTranslatedString("errorTitle"),
                message);
    }

    private static void runCopyLink(String path) {
        String url;
        try {
            final LinkFile linkFile = new ExtendedFileAnalyzer(path).getLinkFile();

            saveRecentFileRecord(linkFile.getFile());

            url = linkFile.getUrl().toString();
            ClipboardManager.getClipboardForSystem().copy(url);
            log.info("Successfully copied url to clipboard from: " + path);

            try {
                NotificationManager.getNotificationForCurrentOS().showInfoNotification(
                        ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                        Translation.getTranslatedString("CommonsBundle", "linkCopied"));
            } catch (Exception e) {
                log.warn("Could not show message for user", e);
            }
        } catch (Exception e) {
            log.warn("Could not copy url from file: {}", path, e);
        }
    }

    private static void runCopyQrCode(String[] args) {
        try {
            if (args.length > 1) {
                final String path = args[1];
                String url;
                final LinkFile linkFile = new ExtendedFileAnalyzer(path).getLinkFile();

                saveRecentFileRecord(linkFile.getFile());

                url = linkFile.getUrl().toString();
                final BufferedImage image = UrlsProceed.generateQrCode(url);

                ClipboardManager.getClipboardForSystem().copy(image);

                NotificationManager.getNotificationForCurrentOS().showInfoNotification(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                        Translation.getTranslatedString("ShowQrDialogBundle", "successCopyImage"));
            }
        } catch (Exception e) {
            log.warn("Could not copy qr code for {}", args[1], e);
            NotificationManager.getNotificationForCurrentOS().showErrorNotification(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                    Translation.getTranslatedString("ShowQrDialogBundle", "errorCopyImage"));
        }
    }

    /**
     * Runs EditDialog
     *
     * @param filepath file path
     */
    private static void runEditDialog(String filepath) {
        final EditDialog dialog = new WindowLauncher<EditDialog>() {
            @Override
            public EditDialog initWindow() {
                final EditDialog editDialog = new EditDialog(filepath);
                editDialog.updateTextFont();
                return editDialog;
            }
        }.getWindow();

        dialog.setVisible(true);
        dialog.setMaximumSize(new Dimension(MAXIMIZED_HORIZ, dialog.getHeight()));
        dialog.setLocationRelativeTo(null);
    }

    private static void runQrDialog(String arg) {
        try {
            final File file = new ExtendedFileAnalyzer(arg).getLinkFile().getFile();

            saveRecentFileRecord(file);

            final ShowQrDialog qrDialog = new WindowLauncher<ShowQrDialog>() {
                @Override
                public ShowQrDialog initWindow() {
                    return new ShowQrDialog(file);
                }
            }.getWindow();
            qrDialog.setVisible(true);
        } catch (Exception e) {
            log.warn("Can not create a qr-code from url: [" + arg + "]", e);
        }
    }

    public static void runSettingsDialog(String launcherLocationPath) {
        final SettingsDialog settingsDialog = new WindowLauncher<SettingsDialog>() {
            @Override
            public SettingsDialog initWindow() {
                return new SettingsDialog(launcherLocationPath);
            }
        }.getWindow();
        Application.settingsDialog = settingsDialog;
        settingsDialog.setVisible(true);
    }

    public static void launchApplication(String applicationPath, String... args) {
        try {
            log.debug("Launching application: {} with arguments: {}", applicationPath, args);
            final List<String> allArguments = new ArrayList<>();

            final String fileExtension = FileUtils.getFileExtension(new File(applicationPath));
            if ((fileExtension.equals("jar"))) {
                allArguments.add("java");
                allArguments.add("-jar");
            }

            allArguments.add(applicationPath);
            allArguments.addAll(Arrays.asList(args.clone()));

            Runtime.getRuntime().exec("chmod u+x " + applicationPath);
            final ProcessBuilder processBuilder = new ProcessBuilder(allArguments);
            processBuilder.start();
        } catch (IOException e) {
            log.warn("Can not launch application: {}", applicationPath, e);
        }
    }

    private void runCreateNewFileWindow() {
        final CreateNewFileFrameWrapper createNewFileFrameWrapper = new WindowLauncher<CreateNewFileFrameWrapper>() {
            @Override
            public CreateNewFileFrameWrapper initWindow() {
                return new CreateNewFileFrameWrapper();
            }
        }.getWindow();
        FrameUtils.setWindowOnScreenCenter(createNewFileFrameWrapper);
        createNewFileFrameWrapper.setVisible(true);
    }

    private void manageArgumentsOnUnix(String[] args) {
        final ApplicationArgument unixOpeningMode = PreferencesManager.getUnixOpeningMode();
        log.info("Unix: opening mode is: {}", unixOpeningMode);
        if (unixOpeningMode.equals(SettingsConstants.OPENER_UNIX_DEFAULT_SELECTOR_MODE)) {
            runModeSelectorWindow(args);
        } else {
            String[] unixArgs = new String[]{unixOpeningMode.getArgument(), args[0]};
            manageArguments(unixArgs);
        }
    }

    private void runModeSelectorWindow(String[] args) {
        String filePath = args[0];
        ModeSelectorDialog modeSelectorDialog = new ModeSelectorDialog(new File(filePath));
        modeSelectorDialog.setVisible(true);
    }

    private void manageSoloArgument(String[] args) {
        if (OS.isWindows()) {
            manageArguments(args);
        } else if (OS.isUnix()) {
            final String arg = args[0];

            final ApplicationArgument argument = ApplicationArgument.getByArgument(arg);

            if (argument != null) {

                switch (argument) {
                    case OPENER_CREATE_NEW_ARGUMENT:
                        runCreateNewFileWindow();
                        break;
                    case OPENER_SETTINGS_ARGUMENT:
                        runSettingsDialog(null);
                        break;
                    case OPENER_ABOUT_ARGUMENT:
                        new AboutApplicationDialog().setVisible(true);
                        break;
                    case OPENER_UPDATE_ARGUMENT:
                    case UPDATE_SILENT_ARGUMENT:
                        log.warn("UPDATE IS NOT SUPPORTED BY CORE! Argument: {}", arg);
                        break;
                    case OPENER_HELP_ARGUMENT_HYPHEN: {
                        System.out.println(helpText());
                        break;
                    }
                    case OPENER_EDIT_ARGUMENT:
                        manageEditArgument(args);
                        break;
                    case OPENER_OPEN_ARGUMENT:
                        showIncorrectArgumentMessage(ApplicationArgument.OPENER_OPEN_ARGUMENT.getArgument());
                        break;
                    case OPENER_CONVERT_ARGUMENT:
                        runConverterDialog(args);
                        break;
                    default:
                        manageArgumentsOnUnix(args);
                        break;
                }
            } else {
                manageArgumentsOnUnix(args);
            }
        } else {
            log.warn("System is not supported yet: {}", OS.getCurrentOS());
        }
    }

    public static SettingsDialog getSettingsDialog() {
        return settingsDialog;
    }
}
