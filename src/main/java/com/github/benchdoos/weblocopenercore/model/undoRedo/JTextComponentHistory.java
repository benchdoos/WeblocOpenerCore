package com.github.benchdoos.weblocopenercore.model.undoRedo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class JTextComponentHistory {
    private final String text;
    private final int caretPosition;
}
