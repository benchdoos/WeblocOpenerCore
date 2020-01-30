package com.github.benchdoos.weblocopenercore.gui.panels.resentFilesPanels;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.model.recentFile.OpenedFileInfo;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class LinkInfoPanel extends JPanel implements Translatable {
    private JPanel contentPane;
    private JPanel linkInfoPanel;
    private JTextField fileNameTextField;
    private JTextField fullLinkTextField;
    private JButton openLinkButton;
    private JButton openLocationButton;
    private JLabel fileNameLabel;
    private JLabel fullLinkLabel;
    private OpenedFileInfo selectedValue;

    public LinkInfoPanel(OpenedFileInfo selectedValue) {
        this.selectedValue = selectedValue;
        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        fillData();
        updateCursorLocations();
        initListeners();
        translate();
    }

    private void updateCursorLocations() {
        fileNameTextField.select(0, 0);
        fullLinkTextField.select(0, 0);
    }

    private void initListeners() {
        openLinkButton.addActionListener(e -> {
            final File file = selectedValue.getFilePath().toFile();
            try {
                final URL url = selectedValue.getType().getLinkProcessor().getUrl(file);
                UrlsProceed.openUrl(url);
            } catch (IOException ex) {
                log.warn("Can not open file: {}", file, ex);
            }
        });

        openLocationButton.addActionListener(e -> FileUtils.openFileInFileBrowser(selectedValue.getFilePath().toFile()));
    }

    private void fillData() {
        fileNameTextField.setText(selectedValue.getFilename());
        try {
            final File file = selectedValue.getFilePath().toFile();
            final URL url = selectedValue.getType().getLinkProcessor().getUrl(file);
            fullLinkTextField.setText(url.toString());
        } catch (Exception e) {
            log.warn("Could not init url", e);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        linkInfoPanel = new JPanel();
        linkInfoPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(linkInfoPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        linkInfoPanel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fileNameLabel = new JLabel();
        this.$$$loadLabelText$$$(fileNameLabel, ResourceBundle.getBundle("translations/LinkInfoPanelBundle").getString("fileNameLabel"));
        linkInfoPanel.add(fileNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fullLinkLabel = new JLabel();
        this.$$$loadLabelText$$$(fullLinkLabel, ResourceBundle.getBundle("translations/LinkInfoPanelBundle").getString("fullLinkLabel"));
        linkInfoPanel.add(fullLinkLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        linkInfoPanel.add(panel1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        openLinkButton = new JButton();
        this.$$$loadButtonText$$$(openLinkButton, ResourceBundle.getBundle("translations/LinkInfoPanelBundle").getString("openLinkButtonText"));
        panel1.add(openLinkButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openLocationButton = new JButton();
        this.$$$loadButtonText$$$(openLocationButton, ResourceBundle.getBundle("translations/LinkInfoPanelBundle").getString("openLocationButtonText"));
        panel1.add(openLocationButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fileNameTextField = new JTextField();
        fileNameTextField.setEditable(false);
        linkInfoPanel.add(fileNameTextField, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fullLinkTextField = new JTextField();
        fullLinkTextField.setEditable(false);
        linkInfoPanel.add(fullLinkTextField, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    @Override
    public void translate() {
        final Translation translation = new Translation("LinkInfoPanelBundle");

        fileNameLabel.setText(translation.get("fileNameLabel"));
        fullLinkLabel.setText(translation.get("fullLinkLabel"));
        openLocationButton.setText(translation.get("openLocationButtonText"));
        openLinkButton.setText(translation.get("openLinkButtonText"));
    }
}
