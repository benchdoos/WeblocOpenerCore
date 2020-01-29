package com.github.benchdoos.weblocopenercore.service.undoRedo;

import com.logdyn.UndoableExecutor;
import lombok.RequiredArgsConstructor;

import javax.swing.text.JTextComponent;

@RequiredArgsConstructor
public class DefaultUndoableExecutor implements UndoableExecutor<DefaultUndoableCommand> {
    private final JTextComponent textComponent;
    //todo need to store text some where

    @Override
    public void execute(DefaultUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("EX> " + text);  //save to somewhere

    }

    @Override
    public void reexecute(DefaultUndoableCommand command) throws Exception {
        final String text = textComponent.getText();
        System.out.println("RE> " + text);

    }

    @Override
    public void unexecute(DefaultUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("UN> " + text);
    }
}
