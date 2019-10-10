package com.github.benchdoos.weblocopenercore;

import com.github.benchdoos.weblocopenercore.core.Logging;
import com.github.benchdoos.weblocopenercore.core.Mode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.UPDATER_APPLICATION_NAME;
import static com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME;

public class Main {
    private static Mode currentMode;
    private static Logger log;

    public static void main(String[] args) {
        System.out.println("WeblocOpener starting with args: " + Arrays.toString(args));

        currentMode = Mode.getModeFromArgs(args);
    }

    private void initLogging(Mode mode) {
        switch (mode) {
            case WEBLOCOPENER:
                new Logging(WEBLOCOPENER_APPLICATION_NAME);
                break;
            case UPDATE:
                new Logging(UPDATER_APPLICATION_NAME);
                break;
        }
        log = LogManager.getLogger(Logging.getCurrentClassName());
    }

    public static Mode getCurrentMode() {
        return currentMode;
    }

}
