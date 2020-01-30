package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableCommand;

public class ChangeUndoableCommand implements UndoableCommand {
    @Override
    public String getName() {
        return "Remove"; //todo for what???
    }
}
