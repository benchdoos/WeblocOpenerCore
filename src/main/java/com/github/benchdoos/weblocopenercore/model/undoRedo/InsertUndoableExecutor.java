package com.github.benchdoos.weblocopenercore.model.undoRedo;

import com.logdyn.UndoableExecutor;

import javax.swing.text.JTextComponent;
import java.util.LinkedList;
import java.util.List;

public class InsertUndoableExecutor implements UndoableExecutor<InsertUndoableCommand> {
    private List<JTextComponentHistory> history;
    private int currentPosition;
    private final JTextComponent textComponent;

    public InsertUndoableExecutor(JTextComponent textComponent) {
        this.textComponent = textComponent;
        history = new LinkedList<>();
        currentPosition = -1; //-1?
    }

    @Override
    public void execute(InsertUndoableCommand undoableCommand) throws Exception {
        final JTextComponentHistory historyEvent = new JTextComponentHistory(textComponent.getText(), textComponent.getCaretPosition());
        System.out.println("INSERT: EX> " + historyEvent);  //save to somewhere

        currentPosition = currentPosition + 1;
        history.add(historyEvent);
    }

    @Override
    public void reexecute(InsertUndoableCommand command) throws Exception {

        if (currentPosition + 1 <= history.size()) {
            currentPosition++;
            final JTextComponentHistory jTextComponentHistory = history.get(currentPosition);
            System.out.println("INSERT: RE> " + jTextComponentHistory);
            restoreFromHistoryEvent(jTextComponentHistory);
        }
    }

    @Override
    public void unexecute(InsertUndoableCommand undoableCommand) throws Exception {
        if (currentPosition - 1 <= history.size()) {
            currentPosition--;
            final JTextComponentHistory jTextComponentHistory = history.get(currentPosition);
            System.out.println("INSERT: UN> " + jTextComponentHistory);
            restoreFromHistoryEvent(jTextComponentHistory);
        }
    }

    private void restoreFromHistoryEvent(JTextComponentHistory historyEvent) {
        textComponent.setText(historyEvent.getText());
        textComponent.setCaretPosition(historyEvent.getCaretPosition());
    }
}
