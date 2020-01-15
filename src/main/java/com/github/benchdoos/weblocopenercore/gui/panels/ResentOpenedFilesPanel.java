package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.weblocopenercore.gui.panels.resentFilesPanels.DisabledResentFilesPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.resentFilesPanels.LinkInfoPanel;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.recentFiles.OpenedFileInfo;
import com.github.benchdoos.weblocopenercore.service.recentFiles.RecentFilesManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Log4j2
public class ResentOpenedFilesPanel extends JPanel implements SettingsPanel {
    private JPanel contentPane;
    private JPanel enabledPanel;
    private JPanel disabledPanel;
    private JButton enableButton;
    private JButton removeSelectedItemsButton;
    private JPanel infoPanel;
    private JList<OpenedFileInfo> fileList;
    private DisabledResentFilesPanel disabledRecentFilesPanel;
    private JButton updateItemsList;

    public ResentOpenedFilesPanel() {
        $$$setupUI$$$();
        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        initVisiblePanels();
        initFileList();
        loadFileList(new RecentFilesManager().loadRecentOpenedFilesList());

        initListeners();
    }

    private void initListeners() {
        updateItemsList.addActionListener(e -> updateFilesList());
        removeSelectedItemsButton.addActionListener(e -> removeSelectedItems());
    }

    private void removeSelectedItems() {
        if (fileList.getModel().getSize() > 0) {
            final List<OpenedFileInfo> selectedValuesList = fileList.getSelectedValuesList();
            if (CollectionUtils.isNotEmpty(selectedValuesList)) {
                try {
                    final Set<OpenedFileInfo> openedFileInfos = new RecentFilesManager().removeFiles(selectedValuesList);
                    loadFileList(openedFileInfos);
                } catch (IOException e) {
                    log.warn("Could not remove files", e);
                }
            }
        }
    }

    private void loadFileList(Set<OpenedFileInfo> openedFileInfos) {
        final DefaultListModel<OpenedFileInfo> model = new DefaultListModel<>();
        openedFileInfos.forEach(model::addElement);
        fileList.setModel(model);
        showNoSelectedItemMessage();
    }

    private void initFileList() {
        initFileListRenderer();
        initFileListListeners();
    }

    private void initFileListRenderer() {
        fileList.setCellRenderer(new FileListCellRenderer());
    }

    private void initFileListListeners() {
        fileList.addListSelectionListener(listener -> {
            final int[] selectedIndices = fileList.getSelectedIndices();

            if (selectedIndices.length == 0) {
                showNoSelectedItemMessage();
            } else if (selectedIndices.length == 1) {
                showLinkInfoPanel(fileList.getSelectedValue());
            } else {
                showMultipleSelectedItemsMessage();
            }
        });
    }

    private void showLinkInfoPanel(OpenedFileInfo selectedValue) {
        if (infoPanel != null && selectedValue != null) {
            final LinkInfoPanel comp = new LinkInfoPanel(selectedValue);
            updateInfoPanel(comp);
        }
    }

    private void showNoSelectedItemMessage() {
        updateInfoPanel(new MessagePanel("No selected item", "Select item to see its info"));
    }

    private void showMultipleSelectedItemsMessage() {
        updateInfoPanel(new MessagePanel("Multiple selection", "You have selected multiple files. No data to show."));
    }

    private void updateInfoPanel(Component component) {
        infoPanel.removeAll();
        infoPanel.add(component);
        infoPanel.revalidate();
        infoPanel.repaint();
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

    private void updateFilesList() {
        if (PreferencesManager.isRecentOpenedFilesHistoryEnabled()) {
            if (fileList != null) {
                try {
                    final int[] selectedIndices = fileList.getSelectedIndices();

                    final Set<OpenedFileInfo> openedFileInfos = new RecentFilesManager().loadRecentOpenedFilesList();
                    final DefaultListModel<OpenedFileInfo> model = (DefaultListModel<OpenedFileInfo>) fileList.getModel();

                    model.clear();
                    openedFileInfos.forEach(model::addElement);

                    fileList.updateUI();
                    fileList.setSelectedIndices(selectedIndices);
                } catch (Exception ex) {
                    log.warn("Can not update recent opened files list", ex);
                }
            }
        }
    }

    @Override
    public void saveSettings() {
        initVisiblePanels();
    }

    /**
     * A FileListCellRenderer for a File.
     */
    private static class FileListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -7799441088157759804L;
        private final JList list = new JList();
        private FileSystemView fileSystemView;
        private JLabel label;
        private Color selectionForeground = list.getSelectionForeground();
        private Color selectionBackground = list.getSelectionBackground();
        private Color foreground = list.getForeground();
        private Color background = list.getBackground();

        FileListCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean selected,
                boolean expanded) {

            final File file = ((OpenedFileInfo) value).getFilePath().toFile();
            label.setIcon(fileSystemView.getSystemIcon(file));
            label.setText(fileSystemView.getSystemDisplayName(file));
            label.setToolTipText(file.getPath());

            if (selected) {
                label.setBackground(selectionBackground);
                label.setForeground(selectionForeground);
            } else {
                label.setBackground(background);
                label.setForeground(foreground);
            }

            return label;
        }
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
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
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
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(100, -1), new Dimension(200, -1), null, 0, false));
        fileList = new JList();
        scrollPane1.setViewportView(fileList);
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setFloatable(false);
        toolBar1.setOrientation(0);
        panel2.add(toolBar1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateItemsList = new JButton();
        updateItemsList.setIcon(new ImageIcon(getClass().getResource("/images/refresh16.png")));
        updateItemsList.setText("");
        updateItemsList.setToolTipText("Update items list");
        toolBar1.add(updateItemsList);
        removeSelectedItemsButton = new JButton();
        removeSelectedItemsButton.setBorderPainted(false);
        removeSelectedItemsButton.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        removeSelectedItemsButton.setOpaque(false);
        removeSelectedItemsButton.setText("");
        toolBar1.add(removeSelectedItemsButton);
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        splitPane1.setRightComponent(infoPanel);
        disabledPanel = new JPanel();
        disabledPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(disabledPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        disabledPanel.add(disabledRecentFilesPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        disabledRecentFilesPanel = new DisabledResentFilesPanel();
        infoPanel = new JPanel(new GridLayout());
    }
}
