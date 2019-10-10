package com.github.benchdoos.weblocopenercore.core;

import com.github.benchdoos.weblocopenercore.core.constants.ArgumentConstants;

public enum  Mode {
    WEBLOCOPENER, UPDATE;

    public static Mode getModeFromArgs(String... args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase(ArgumentConstants.OPENER_UPDATE_ARGUMENT)) {
                return Mode.UPDATE;
            }
        }
        return Mode.WEBLOCOPENER;
    }
}
