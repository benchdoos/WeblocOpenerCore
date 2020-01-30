package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.weblocopenercore.model.undoRedo.ChangeUndoableCommand;
import com.github.benchdoos.weblocopenercore.model.undoRedo.ChangeUndoableExecutor;
import com.github.benchdoos.weblocopenercore.model.undoRedo.InsertUndoableCommand;
import com.github.benchdoos.weblocopenercore.model.undoRedo.InsertUndoableExecutor;
import com.github.benchdoos.weblocopenercore.model.undoRedo.RemoveUndoableCommand;
import com.github.benchdoos.weblocopenercore.model.undoRedo.RemoveUndoableExecutor;
import com.logdyn.CommandDelegator;
import com.logdyn.ExecutionException;
import com.logdyn.UndoableCommand;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.JTextComponent;

@Log4j2
public class DefaultHistoryServiceImpl implements HistoryService {

    private final JTextComponent component;

    public DefaultHistoryServiceImpl(@NotNull JTextComponent component) {
        this.component = component;
        CommandDelegator.getINSTANCE().subscribe(new InsertUndoableExecutor(component), InsertUndoableCommand.class);
        CommandDelegator.getINSTANCE().subscribe(new ChangeUndoableExecutor(component), ChangeUndoableCommand.class);
        CommandDelegator.getINSTANCE().subscribe(new RemoveUndoableExecutor(component), RemoveUndoableCommand.class);
    }

    @Override
    public void registerCommand(UndoableCommand command) {
        try {
            CommandDelegator.getINSTANCE().publish(command);
        } catch (ExecutionException e) {
            log.warn("Could not register command:{}", command, e);
        }
    }

    @Override
    public void undo() {
        if (CommandDelegator.getINSTANCE().canUndo()) {
            try {
                CommandDelegator.getINSTANCE().undo();
            } catch (ExecutionException e) {
                log.warn("Could not undo command", e);
            }
        }
    }

    @Override
    public void redo() {
        if (CommandDelegator.getINSTANCE().canRedo()) {
            try {
                CommandDelegator.getINSTANCE().redo();
            } catch (ExecutionException e) {
                log.warn("Could not redo command", e);
            }
        }
    }
}
