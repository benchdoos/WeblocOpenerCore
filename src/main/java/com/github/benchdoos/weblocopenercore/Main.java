package com.github.benchdoos.weblocopenercore;

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import com.github.benchdoos.weblocopenercore.service.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.model.enumirations.OS;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import com.github.benchdoos.weblocopenercore.utils.system.UnsupportedSystemException;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

@Log4j2
public class Main {

    public static void main(String[] args) {
        System.out.println(WEBLOCOPENER_APPLICATION_NAME + " starting with args: " + Arrays.toString(args));

        try {
            initLogging();

            SystemUtils.checkIfSystemIsSupported();

            new Application(args);
        } catch (UnsupportedSystemException e) {
            log.fatal("System not supported", e);
            final String translatedString = Translation.get("CommonsBundle", "systemNotSupported");
            final String message = translatedString + " " + OS.getCurrentOS().name();

            NotificationManager.getForcedNotification(null).showErrorNotification(message, message);
        } catch (Exception e) {
            log.fatal("System exited with exception", e);
            CoreUtils.showFatalErrorMessage(e);
        }
    }

    private static void initLogging() {
        log.info("Logging to: {}", PathConstants.APP_LOG_FOLDER_PATH);
    }
}
