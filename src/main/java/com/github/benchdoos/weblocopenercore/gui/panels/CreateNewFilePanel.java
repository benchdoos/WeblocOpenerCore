/*
 * (C) Copyright 2019.  Eugene Zrazhevsky and others.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Contributors:
 * Eugene Zrazhevsky <eugene.zrazhevsky@gmail.com>
 */

package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.linksupport.links.Link;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.gui.PlaceholderTextField;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.notification.NotificationManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

@Log4j2
public class CreateNewFilePanel extends JPanel implements Translatable {
    private Window parentWindow;

    private JPanel contentPane;

    private JButton buttonSave;
    private JButton buttonCancel;
    private JTextField urlTextField;
    private JComboBox<Link> linkComboBox;

    public CreateNewFilePanel() {
        $$$setupUI$$$();
        initGui();
    }

    public void initGui() {
        initActionListeners();

        initWindowGui();

        initLinkComboBox();

        loadLinkComboBox();

        translate();
    }

    private void loadLinkComboBox() {
        linkComboBox.setModel(new DefaultComboBoxModel<>(Link.values()));
    }

    private void initLinkComboBox() {
        linkComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object o, int i, boolean b, boolean b1) {

                final Link link = (Link) o;

                return super.getListCellRendererComponent(jList, link.getExtension(), i, b, b1);
            }
        });
    }

    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    private void initActionListeners() {
        buttonSave.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        urlTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                urlKeyListener(keyEvent);
                super.keyPressed(keyEvent);
            }

            private void urlKeyListener(KeyEvent e) {
                final int selectedIndex = linkComboBox.getSelectedIndex();

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    final int next = selectedIndex - 1;
                    if (next >= 0) {
                        linkComboBox.setSelectedIndex(next);
                    } else {
                        linkComboBox.setSelectedIndex(linkComboBox.getModel().getSize() - 1);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    final int next = selectedIndex + 1;
                    if (next < linkComboBox.getModel().getSize()) {
                        linkComboBox.setSelectedIndex(next);
                    } else {
                        linkComboBox.setSelectedIndex(0);
                    }
                }
            }
        });
    }


    private void initWindowGui() {
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JTextField getUrlTextField() {
        return urlTextField;
    }

    private void onOK() {
        final String text = urlTextField.getText();
        try {
            final URL url = new URL(text);
            String path = saveFileBrowser();
            final Link link = ((Link) linkComboBox.getSelectedItem());

            if (path != null && link != null) {
                final String suffix = "." + link.getExtension();
                if (!path.endsWith(suffix)) {
                    path += suffix;
                }
                try {
                    log.debug("Link with url: {} at location: {} will be created as: {}", url, path, link.getName());

                    link.getLinkProcessor().createLink(url, new FileOutputStream(new File(path)));

                    if (PreferencesManager.openFolderForNewFile()) {
                        FileUtils.openFileInFileBrowser(new File(path));
                    } else {
                        log.info("Opening in file browser disabled by settings");
                    }

                    parentWindow.dispose();
                } catch (IOException e) {
                    log.warn("Could not create .{} link at: {} with url: {}", link.getExtension(), path, url, e);
                    NotificationManager.getNotificationForCurrentOS().showErrorNotification(
                            ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                            Translation.getTranslatedString("CreateNewFileBundle", "errorSave")
                                    + " " + new File(path).getName() + " \n" + e.getLocalizedMessage()
                    );
                }
            } else {
                log.warn("Will not create file, because path is null or link is not selected. path: {}, link: {}", path, link);
            }
        } catch (MalformedURLException e) {
            log.warn("Could not create url from text: {}, cause: {}", text, e.getMessage());
            FrameUtils.shakeFrame(this);
        }
    }

    private void onCancel() {
        parentWindow.dispose();
    }

    private String saveFileBrowser() {
        log.debug("Opening File Browser");

        FileDialog fileDialog = new FileDialog(FrameUtils.findDialog(this),
                Translation.getTranslatedString("CreateNewFileBundle", "saveAsFile"),
                FileDialog.SAVE);
        try {
            fileDialog.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource("/images/balloonIcon256.png")));
            final String property = System.getProperty("user.home");
            fileDialog.setDirectory(property);
            return FrameUtils.getFilePathFromFileDialog(fileDialog, log);
        } catch (Exception e) {
            log.warn("Could not launch File Browser", e);
            fileDialog.dispose();
            return null;
        }
    }

    private void createUIComponents() {
        urlTextField = new PlaceholderTextField();
        ((PlaceholderTextField) urlTextField).setPlaceholder("URL");

        linkComboBox = new JComboBox<>();
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSave = new JButton();
        this.$$$loadButtonText$$$(buttonSave, ResourceBundle.getBundle("translations/CreateNewFileBundle").getString("saveButton"));
        panel2.add(buttonSave, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, ResourceBundle.getBundle("translations/CommonsBundle").getString("cancelButton"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.add(urlTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(400, -1), new Dimension(400, -1), new Dimension(400, -1), 0, false));
        linkComboBox = new JComboBox();
        panel4.add(linkComboBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        buttonSave.setText(Translation.getTranslatedString("CreateNewFileBundle", "saveButton"));
        buttonCancel.setText(Translation.getTranslatedString("CommonsBundle", "cancelButton"));
    }

    public JButton getOkButton() {
        return buttonSave;
    }
}
