package com.github.benchdoos.weblocopenercore;

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Logging;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.service.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.system.OS;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import com.github.benchdoos.weblocopenercore.utils.system.UnsupportedSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

public class Main {
    private static Logger log;

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
        new Logging(WEBLOCOPENER_APPLICATION_NAME);
        log = LogManager.getLogger(Logging.getCurrentClassName());
    }
}
