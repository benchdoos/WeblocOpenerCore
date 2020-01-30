package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableExecutor;
import lombok.RequiredArgsConstructor;

import javax.swing.text.JTextComponent;

@RequiredArgsConstructor
public class RemoveUndoableExecutor implements UndoableExecutor<RemoveUndoableCommand> {
    private final JTextComponent textComponent;
    //todo need to store text some where

    @Override
    public void execute(RemoveUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("REMOVE: EX> " + text);  //save to somewhere

    }

    @Override
    public void reexecute(RemoveUndoableCommand command) throws Exception {
        final String text = textComponent.getText();
        System.out.println("REMOVE: RE> " + text);

    }

    @Override
    public void unexecute(RemoveUndoableCommand undoableCommand) throws Exception {
        final String text = textComponent.getText();
        System.out.println("REMOVE: UN> " + text);
    }
}
