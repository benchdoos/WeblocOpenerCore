package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableExecutor;
import lombok.RequiredArgsConstructor;

import javax.swing.text.JTextComponent;

@RequiredArgsConstructor
public class ChangeUndoableExecutor implements UndoableExecutor<ChangeUndoableCommand> {
    private final JTextComponent textComponent;
    //todo need to store text some where

    @Override
    public void execute(ChangeUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("CHANGE: EX> " + text);  //save to somewhere

    }

    @Override
    public void reexecute(ChangeUndoableCommand command) throws Exception {
        final String text = textComponent.getText();
        System.out.println("CHANGE: RE> " + text);

    }

    @Override
    public void unexecute(ChangeUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("CHANGE: UN> " + text);
    }
}
