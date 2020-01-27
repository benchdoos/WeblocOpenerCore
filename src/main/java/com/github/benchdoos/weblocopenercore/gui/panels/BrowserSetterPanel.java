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

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.browser.Browser;
import com.github.benchdoos.weblocopenercore.utils.browser.BrowserManager;
import com.github.benchdoos.weblocopenercore.utils.system.OS;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.TimingUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Log4j2
public class BrowserSetterPanel extends JPanel implements SettingsPanel, Translatable {

    private boolean onInit = true;

    private JPanel contentPane;
    private JComboBox<Browser> browserComboBox;
    private JLabel callLabel;
    private JCheckBox incognitoCheckBox;
    private JTextField callTextField;
    private JLabel syntaxInfoLabel;
    private JLabel openInLabel;

    public BrowserSetterPanel() {
        initGui();
        initListeners();
        initBrowserComboBox();
    }

    @Override
    public String getName() {
        return Translation.get("SettingsDialogBundle", "settingsBrowserPanelName");
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null));
        browserComboBox = new JComboBox();
        browserComboBox.setMaximumRowCount(9);
        panel2.add(browserComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openInLabel = new JLabel();
        this.$$$loadLabelText$$$(openInLabel, ResourceBundle.getBundle("translations/BrowserSetterPanelBundle").getString("openInBrowser"));
        panel2.add(openInLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        callLabel = new JLabel();
        this.$$$loadLabelText$$$(callLabel, ResourceBundle.getBundle("translations/BrowserSetterPanelBundle").getString("customCallLabel"));
        callLabel.setVisible(true);
        panel2.add(callLabel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        incognitoCheckBox = new JCheckBox();
        incognitoCheckBox.setEnabled(false);
        incognitoCheckBox.setText("");
        incognitoCheckBox.setToolTipText(ResourceBundle.getBundle("translations/SettingsDialogBundle_en_EN").getString("incognitoModeTooltip"));
        panel2.add(incognitoCheckBox, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        callTextField = new JTextField();
        callTextField.setVisible(true);
        panel2.add(callTextField, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        syntaxInfoLabel = new JLabel();
        syntaxInfoLabel.setIcon(new ImageIcon(getClass().getResource("/images/infoIcon16.png")));
        syntaxInfoLabel.setText("");
        panel2.add(syntaxInfoLabel, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        openInLabel.setLabelFor(browserComboBox);
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
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private int findBrowser(String browserValue) {
        int result;
        for (int i = 0; i < BrowserManager.getBrowserList().size(); i++) {
            Browser browser = BrowserManager.getBrowserList().get(i);
            log.debug("Checking if incoming browser {} is in list, now: {}", browserValue, browser);

            if (browser.getCall() != null) {
                if (browser.getCall().equals(browserValue)) {
                    result = i;
                    log.debug("Browser is: " + browser);
                    return result;
                } else if (browser.getIncognitoCall() != null) {
                    if (browser.getIncognitoCall().equals(browserValue)) {
                        result = i;
                        log.debug("Browser is: " + browser);
                        return result;
                    }
                }
            }
        }

        if (browserValue.equalsIgnoreCase(SettingsConstants.BROWSER_DEFAULT_VALUE) || browserValue.isEmpty()) {
            log.debug("Browser is: default");
            return 0;
        } else {
            final int i = BrowserManager.getBrowserList().size() - 1;
            log.debug("Browser is: custom - [{}]", browserValue);
            return i;
        }
    }

    private BalloonTip generateBalloonTip(String toolTipText) {
        BalloonTip balloonTip = new BalloonTip(syntaxInfoLabel, toolTipText);
        balloonTip.setStyle(new MinimalBalloonStyle(Color.white, 5));
        balloonTip.setCloseButton(null);
        balloonTip.setVisible(false);
        balloonTip.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                balloonTip.setVisible(false);
            }
        });
        return balloonTip;
    }

    public Object getSelectedItem() {
        return browserComboBox.getSelectedItem();
    }

    /**
     * This inits the the file browser
     * <b>WARNING!</b>
     * Don't forget it, otherwise it will crash fileBrowser
     */
    public void init() {
        //no other way found. call this after init from outside
        onInit = false;
    }

    private void initBrowserComboBox() {
        ArrayList<Browser> browsers = BrowserManager.getBrowserList();

        Browser others = new Browser(
                Translation.get("SettingsDialogBundle", "customBrowserName"), null);
        browsers.add(others);

        browserComboBox.setModel(new DefaultComboBoxModel<Browser>(toBrowsersArray(browsers.toArray())));
        browserComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Browser) {
                    return super.getListCellRendererComponent(list, ((Browser) value).getName(), index, isSelected, cellHasFocus);
                } else {
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            }
        });

        browserComboBox.addActionListener(e -> {
            if (browserComboBox.getSelectedIndex() == browserComboBox.getItemCount() - 1) {
                log.debug("init: " + onInit);
                if (!onInit) {
                    log.info("Opening file browser for custom browser search");
                    String path;
                    path = createBrowserPathForCurrentOs();
                    if (path != null) {
                        showBrowserCallTextField(path);
                    } else {
                        final int browser = findBrowser(PreferencesManager.getBrowserValue());
                        browserComboBox.setSelectedIndex(browser);
                        if (browser == browserComboBox.getItemCount() - 1) {
                            callTextField.setVisible(true);
                            callTextField.setText(PreferencesManager.getBrowserValue());
                            incognitoCheckBox.setEnabled(false);
                        }
                    }
                } else {
                    callLabel.setVisible(true);
                    callTextField.setText(PreferencesManager.getBrowserValue());
                    callTextField.setVisible(true);
                    syntaxInfoLabel.setVisible(true);
                }
            } else {
                if (browserComboBox.getSelectedIndex() == 0) {
                    incognitoCheckBox.setEnabled(false);
                    incognitoCheckBox.setSelected(false);
                } else {
                    if (browsers.get(browserComboBox.getSelectedIndex()).getIncognitoCall() != null) {
                        incognitoCheckBox.setEnabled(true);
                    } else {
                        incognitoCheckBox.setSelected(false);
                        incognitoCheckBox.setEnabled(false);
                    }
                }
                callLabel.setVisible(false);
                callTextField.setVisible(false);
            }
        });
    }

    /**
     * Creates path for current system.
     * Unix system does not need file browser
     *
     * @return path to browser or {@link UrlsProceed#SITE} string
     */
    private String createBrowserPathForCurrentOs() {
        if (OS.isUnix()) {
            return UrlsProceed.SITE;
        } else {
            return openFileBrowser();
        }
    }

    private void showBrowserCallTextField(String path) {
        callLabel.setVisible(true);
        callTextField.setVisible(true);
        callTextField.setText(path);
        incognitoCheckBox.setEnabled(false);
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        syntaxInfoLabel.setVisible(false);
        callTextField.setVisible(false);
        callLabel.setVisible(false);

        setSyntaxInfoButtonToolTip();

        translate();
    }

    private void initListeners() {
        callTextField.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                syntaxInfoLabel.setVisible(false);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                syntaxInfoLabel.setVisible(true);
            }
        });
    }

    private void loadBrowserSettings() {
        final Browser browser = (Browser) browserComboBox.getSelectedItem();

        if (browser != null) {
            if (browser.getIncognitoCall() != null) {
                incognitoCheckBox.setSelected(PreferencesManager.getBrowserValue().equals(browser.getIncognitoCall()));
            } else {
                incognitoCheckBox.setSelected(false);
                incognitoCheckBox.setEnabled(false);
            }
        } else {
            incognitoCheckBox.setSelected(false);
            incognitoCheckBox.setEnabled(false);
        }
    }

    public void loadSettings() {
        browserComboBox.setSelectedIndex(findBrowser(PreferencesManager.getBrowserValue()));
        loadBrowserSettings();
    }

    private String openFileBrowser() {
        log.debug("Opening File Browser");

        FileDialog fd = new FileDialog(FrameUtils.findDialog(this),
                Translation.get("SettingsDialogBundle", "chooseAFile"),
                FileDialog.LOAD);
        try {
            fd.setIconImage(Toolkit.getDefaultToolkit()
                    .getImage(getClass().getResource("/images/balloonIcon256.png")));
            fd.setDirectory(System.getProperty("user.dir"));
            fd.setFile("*.exe");
            fd.setMultipleMode(false);
            return FrameUtils.getFilePathFromFileDialog(fd, log);
        } catch (Exception e) {
            log.warn("Could not launch File Browser", e);
            fd.dispose();
            return null;
        }
    }

    public void saveSettings() {
        Browser browser = (Browser) browserComboBox.getSelectedItem();
        if (browser != null) {
            final String call = browser.getCall();
            final int selectedIndex = browserComboBox.getSelectedIndex();
            final int itemCount = browserComboBox.getItemCount();

            final String incognitoCall = browser.getIncognitoCall();
            if (selectedIndex != itemCount - 1) {
                if (call != null) {
                    if (!PreferencesManager.getBrowserValue().equals(call)) {
                        if (!incognitoCheckBox.isSelected()) {
                            log.debug("Browser call: {}", call);
                            PreferencesManager.setBrowserValue(call);
                        }
                    }
                }
                if (incognitoCall != null) {
                    if (!PreferencesManager.getBrowserValue().equals(incognitoCall)) {
                        if (incognitoCheckBox.isSelected()) {
                            log.debug("Browser call: {}", incognitoCall);
                            PreferencesManager.setBrowserValue(incognitoCall);
                        }
                    }
                }
            } else {
                final String text = callTextField.getText();
                if (!text.equals(incognitoCall)) {
                    log.debug("Browser call: {}", text);
                    PreferencesManager.setBrowserValue(text);
                }
            }
        }

        log.info("Saving settings: browser: {}", browser);
    }

    private void setSyntaxInfoButtonToolTip() {

        syntaxInfoLabel.addMouseListener(new MouseAdapter() {
            final int DEFAULT_TIME = 10_000;
            final int SHORT_TIME = 6_000;
            private final String translatedBalloonTip = Translation.get("SettingsDialogBundle", "toolTipText");
            BalloonTip balloonTip = generateBalloonTip(translatedBalloonTip);

            @Override
            public void mouseClicked(MouseEvent e) {
                balloonTip.setVisible(true);
                TimingUtils.showTimedBalloon(balloonTip, DEFAULT_TIME);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                balloonTip.setVisible(true);
                TimingUtils.showTimedBalloon(balloonTip, SHORT_TIME);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                balloonTip = generateBalloonTip(
                        translatedBalloonTip);
            }
        });
    }

    private Browser[] toBrowsersArray(Object[] toArray) {
        Browser[] array = new Browser[toArray.length];
        for (int i = 0; i < toArray.length; i++) {
            Object o = toArray[i];
            if (o instanceof Browser) {
                array[i] = (Browser) o;
            }
        }
        return array;
    }

    @Override
    public void translate() {
        Translation translation = new Translation("BrowserSetterPanelBundle");
        openInLabel.setText(translation.get("openInBrowser"));
        callLabel.setText(translation.get("customCallLabel"));

        translateComboBox();
    }

    private void translateComboBox() {
        DefaultComboBoxModel<Browser> model = ((DefaultComboBoxModel<Browser>) browserComboBox.getModel());
        final Browser defaultBrowser = model.getElementAt(0);
        if (defaultBrowser != null) {
            defaultBrowser.setName(Translation.get("CommonsBundle", "defaultBrowserName"));
        }
        final Browser customBrowser = model.getElementAt(model.getSize() - 1);
        if (customBrowser != null) {
            customBrowser.setName(Translation.get("SettingsDialogBundle", "customBrowserName"));
        }
    }
}
