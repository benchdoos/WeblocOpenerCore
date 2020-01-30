package com.github.benchdoos.weblocopenercore.service;

import com.logdyn.UndoableCommand;

import javax.swing.text.JTextComponent;

public interface HistoryService {

    public void registerCommand(UndoableCommand command);

    public void undo();

    public void redo();
}
