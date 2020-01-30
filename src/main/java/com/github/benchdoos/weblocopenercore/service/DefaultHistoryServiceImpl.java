package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.weblocopenercore.model.undoRedo.DefaultUndoableCommand;
import com.github.benchdoos.weblocopenercore.model.undoRedo.DefaultUndoableExecutor;
import com.logdyn.CommandDelegator;
import com.logdyn.ExecutionException;
import com.logdyn.UndoableCommand;
import lombok.extern.log4j.Log4j2;

import javax.swing.text.JTextComponent;

@Log4j2
public class DefaultHistoryServiceImpl implements HistoryService {

    private final JTextComponent component;

    public  DefaultHistoryServiceImpl(JTextComponent component) {
        this.component = component;
        CommandDelegator.getINSTANCE().subscribe(
                new DefaultUndoableExecutor(component),
                DefaultUndoableCommand.class //todo change it if needed
        );
    }

    @Override
    public void registerCommand(UndoableCommand command) {
        try {
            CommandDelegator.getINSTANCE().publish(new DefaultUndoableCommand()); //todo change it...?
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
