package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableExecutor;
import lombok.RequiredArgsConstructor;

import javax.swing.text.JTextComponent;

@RequiredArgsConstructor
public class InsertUndoableExecutor implements UndoableExecutor<InsertUndoableCommand> {
    private final JTextComponent textComponent;
    //todo need to store text some where

    @Override
    public void execute(InsertUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("INSERT: EX> " + text);  //save to somewhere

    }

    @Override
    public void reexecute(InsertUndoableCommand command) throws Exception {
        final String text = textComponent.getText();
        System.out.println("INSERT: RE> " + text);

    }

    @Override
    public void unexecute(InsertUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("INSERT: UN> " + text);
    }
}
