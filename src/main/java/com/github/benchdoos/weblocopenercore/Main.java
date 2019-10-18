package com.github.benchdoos.weblocopenercore;

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Logging;
import com.github.benchdoos.weblocopenercore.core.Mode;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.gui.InfoDialog;
import com.github.benchdoos.weblocopenercore.utils.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.system.SystemUtils;
import com.github.benchdoos.weblocopenercore.utils.system.UnsupportedSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

public class Main {
    private static Mode currentMode = Mode.WEBLOCOPENER;
    private static Logger log;

    public static void main(String[] args) {
        System.out.println(WEBLOCOPENER_APPLICATION_NAME + " starting with args: " + Arrays.toString(args));

        try {
            initLogging();

            SystemUtils.checkIfSystemIsSupported();

            new Application(args);
        } catch (UnsupportedSystemException e) {
            log.fatal("System not supported", e);
            final String translatedString = Translation.getTranslatedString("CommonsBundle", "systemNotSupported");
            final String message = translatedString + " " + SystemUtils.getCurrentOS().name();

            NotificationManager.getForcedNotification(null).showErrorNotification(message, message);
        } catch (Exception e) {
            log.fatal("System exited with exception", e);
            showFatalErrorMessage(e);
        }
    }

    private static void showFatalErrorMessage(Exception e) {
        InfoDialog infoDialog = new InfoDialog();
        infoDialog.setTitle(WEBLOCOPENER_APPLICATION_NAME);
        //todo load this from file
        // todo add reporting errors??? how - hz...
        final String content = "<html>\n" +
                "<body>\n" +
                "<h1>\n" +
                "    Application failed with exception\n" +
                "</h1>\n" +
                "<br>\n" +
                "Create your issue <a href=\"https://github.com/benchdoos/WeblocOpener/issues/new/choose\">here</a>.\n" +
                "<br>\n" +
                "Don't forget to copy stacktrace:\n" +
                "<br>\n" +
                "<br>\n" +
                "<br>\n" +
                "<table>\n" +
                "    <tr>\n" +
                "        <td>%s</td>\n" +
                "    </tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);

        infoDialog.setContent(String.format(content, stringWriter.toString()));
        infoDialog.setVisible(true);
    }

    private static void initLogging() {
        new Logging(WEBLOCOPENER_APPLICATION_NAME);
        log = LogManager.getLogger(Logging.getCurrentClassName());
    }
    public static Mode getCurrentMode() {
        return currentMode;
    }

}
