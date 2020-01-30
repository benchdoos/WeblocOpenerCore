package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableCommand;

public class DefaultUndoableCommand implements UndoableCommand {
    @Override
    public String getName() {
        return "Example Undoable Command"; //todo for what???
    }
}
