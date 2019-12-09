package com.github.benchdoos.weblocopenercore.gui.panels;

import javax.swing.JPanel;
import java.awt.GridLayout;

public class PreviousOpenedFilesPanel extends JPanel implements SettingsPanel {
    private JPanel contentPane;

    public PreviousOpenedFilesPanel() {
        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);
    }

    @Override
    public void loadSettings() {
        //todo load previous opened files
    }

    @Override
    public void saveSettings() {
        //NOP
    }
}
