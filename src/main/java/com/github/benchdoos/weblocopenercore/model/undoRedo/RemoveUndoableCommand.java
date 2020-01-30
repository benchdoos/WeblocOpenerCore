package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableCommand;

public class RemoveUndoableCommand implements UndoableCommand {
    @Override
    public String getName() {
        return "Change"; //todo for what???
    }
}
