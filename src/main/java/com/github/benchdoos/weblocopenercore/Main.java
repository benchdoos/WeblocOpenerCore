package com.github.benchdoos.weblocopenercore;

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Logging;
import com.github.benchdoos.weblocopenercore.core.Mode;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.utils.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import com.github.benchdoos.weblocopenercore.utils.system.UnsupportedSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.UPDATER_APPLICATION_NAME;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

public class Main {
    private static Mode currentMode = Mode.WEBLOCOPENER;
    private static Logger log;

    public static void main(String[] args) {
        System.out.println("WeblocOpener starting with args: " + Arrays.toString(args));

        try {
            SystemUtils.checkIfSystemIsSupported();

            initLogging();

            new Application(args);
        } catch (UnsupportedSystemException e) {
            log.fatal("System not supported", e);
            final String translatedString = Translation.getTranslatedString("CommonsBundle", "systemNotSupported");
            final String message = translatedString + " " + SystemUtils.getCurrentOS().name();

            NotificationManager.getForcedNotification(null).showErrorNotification(message, message);
        } catch (Exception e) {
            log.fatal("System exited with exception", e);
        }
    }

    private static void initLogging() {
        new Logging(WEBLOCOPENER_APPLICATION_NAME);
        log = LogManager.getLogger(Logging.getCurrentClassName());
    }
    public static Mode getCurrentMode() {
        return currentMode;
    }

}
