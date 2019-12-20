package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.weblocopenercore.gui.panels.resentFilesPanels.DisabledResentFilesPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.resentFilesPanels.MessagePanel;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.recentFiles.OpenedFileInfo;
import com.github.benchdoos.weblocopenercore.service.recentFiles.RecentFilesManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jsoup.internal.StringUtil;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Set;

public class ResentOpenedFilesPanel extends JPanel implements SettingsPanel {
    private JPanel contentPane;
    private JPanel enabledPanel;
    private JPanel disabledPanel;
    private JButton enableButton;
    private JButton removeSelectedItemsButton;
    private JButton removeAllItemsButton;
    private JPanel linkInfoPanel;
    private JList<OpenedFileInfo> fileList;
    private DisabledResentFilesPanel disabledRecentFilesPanel;

    public ResentOpenedFilesPanel() {
        $$$setupUI$$$();
        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        initVisiblePanels();
        initFileList();
        loadFileList();
    }

    private void loadFileList() {
        final Set<OpenedFileInfo> openedFileInfos = new RecentFilesManager().loadRecentOpenedFilesList();
        final DefaultListModel<OpenedFileInfo> model = new DefaultListModel<>();
        openedFileInfos.forEach(model::addElement);
        fileList.setModel(model);
    }

    private void initFileList() {
        initFileListRenderer();
        initFileListListeners();
    }

    private void initFileListRenderer() {
        fileList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof OpenedFileInfo) {
                    final OpenedFileInfo fileInfo = (OpenedFileInfo) value;
                    return super.getListCellRendererComponent(list, fileInfo.getFilename(), index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
    }

    private void initFileListListeners() {
        fileList.addListSelectionListener(listener -> {
            final int firstIndex = listener.getFirstIndex();
            final int lastIndex = listener.getLastIndex();

            if (firstIndex == -1 && lastIndex == -1) {
                showNoSelectedItemMessage();
            }
        });
    }

    private void showNoSelectedItemMessage() {
        linkInfoPanel.removeAll();
        linkInfoPanel.add(new MessagePanel("No selected item", "Select item to see its info"));
    }

    @Override
    public String getName() {
        return "Recently opened files"; //todo translate
    }


    private void initVisiblePanels() {
        final boolean enabled = PreferencesManager.isRecentOpenedFilesHistoryEnabled();
        enabledPanel.setVisible(enabled);
        disabledPanel.setVisible(!enabled);
    }

    @Override
    public void loadSettings() {
        initVisiblePanels();
        //todo load previous opened files
    }

    @Override
    public void saveSettings() {
        initVisiblePanels();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        enabledPanel = new JPanel();
        enabledPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(enabledPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        enabledPanel.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel2);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(200, -1), null, 0, false));
        fileList = new JList();
        scrollPane1.setViewportView(fileList);
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setOrientation(0);
        panel2.add(toolBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeSelectedItemsButton = new JButton();
        removeSelectedItemsButton.setBorderPainted(false);
        removeSelectedItemsButton.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        removeSelectedItemsButton.setOpaque(false);
        removeSelectedItemsButton.setText("");
        toolBar1.add(removeSelectedItemsButton);
        removeAllItemsButton = new JButton();
        removeAllItemsButton.setText("Remove all");
        toolBar1.add(removeAllItemsButton);
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        linkInfoPanel = new JPanel();
        linkInfoPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(linkInfoPanel);
        disabledPanel = new JPanel();
        disabledPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(disabledPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        disabledPanel.add(disabledRecentFilesPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        disabledRecentFilesPanel = new DisabledResentFilesPanel();
    }
}
