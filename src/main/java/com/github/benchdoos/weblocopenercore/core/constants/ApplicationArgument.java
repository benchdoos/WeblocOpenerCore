package com.github.benchdoos.weblocopenercore.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ApplicationArgument {
    OPENER_OPEN_ARGUMENT("-open"),
    OPENER_CREATE_ARGUMENT("-create"),
    OPENER_CREATE_NEW_ARGUMENT("-create-new"),
    OPENER_EDIT_ARGUMENT("-edit"),
    OPENER_SETTINGS_ARGUMENT("-settings"),
    OPENER_UPDATE_ARGUMENT("-update"),
    OPENER_ABOUT_ARGUMENT("-about"),
    OPENER_HELP_ARGUMENT_HYPHEN("-help"),
    OPENER_QR_ARGUMENT("-qr"),
    OPENER_COPY_LINK_ARGUMENT("-copy"),
    OPENER_COPY_QR_ARGUMENT("-copy-qr"),
    OPENER_CONVERT_ARGUMENT("-convert"),
    UPDATE_SILENT_ARGUMENT("-update-silent");

    private final String argument;

    public static ApplicationArgument getByArgument(String argument) {
        return Arrays.stream(ApplicationArgument.values())
                .filter(current -> current.getArgument().equalsIgnoreCase(argument))
                .findFirst()
                .orElse(null);
    }
}
