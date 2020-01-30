package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableCommand;

public class InsertUndoableCommand implements UndoableCommand {
    @Override
    public String getName() {
        return "Insert"; //todo for what???
    }
}
