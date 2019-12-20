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

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.gui.AboutApplicationDialog;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.WindowLauncher;
import com.github.benchdoos.weblocopenercore.service.recentFiles.RecentFilesManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.github.benchdoos.weblocopenercore.core.Translation.SUPPORTED_LOCALES;

@Log4j2
public class MainSetterPanel extends JPanel implements SettingsPanel, Translatable {
    private JPanel contentPane;
    private JCheckBox autoUpdateEnabledCheckBox;
    private JButton checkUpdatesButton;
    private JCheckBox openFolderForQRCheckBox;
    private JCheckBox showNotificationsToUserCheckBox;
    private JLabel versionLabel;
    private JLabel versionStringLabel;
    private JCheckBox betaInstallCheckBox;

    private JComboBox<Object> localeComboBox;
    private JLabel languageLabel;
    private JButton aboutButton;
    private JCheckBox savePreviouslyOpenedFilesCheckBox;
    private JButton cleanupRecentFilesButton;
    private String launcherLocationPath;

    public MainSetterPanel() {
        initGui();
    }

    @Override
    public String getName() {
        return Translation.getTranslatedString("SettingsDialogBundle", "settingsMainPanelName");
    }

    @Override
    public void setLauncherLocationPath(String launcherLocationPath) {
        this.launcherLocationPath = launcherLocationPath;
        initUpdateButtons();
    }

    private void fillLocaleComboBox() {
        String sysLocale = Translation.getTranslatedString("LocaleSetterPanelBundle", "languageDefault");
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>();
        model.addElement(sysLocale);

        for (Locale locale : SUPPORTED_LOCALES) {
            model.addElement(locale);
        }

        localeComboBox.setModel(model);
    }

    private void initLocaleComboBox() {
        DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Locale) {
                    final Locale locale = (Locale) value;
                    return super.getListCellRendererComponent(list, locale.getDisplayLanguage(locale), index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        };
        localeComboBox.setRenderer(renderer);
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
        contentPane.setLayout(new GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        openFolderForQRCheckBox = new JCheckBox();
        openFolderForQRCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(openFolderForQRCheckBox, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("openFolderForQRCheckBox"));
        panel1.add(openFolderForQRCheckBox, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showNotificationsToUserCheckBox = new JCheckBox();
        showNotificationsToUserCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(showNotificationsToUserCheckBox, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("showNotificationsCheckBox"));
        panel1.add(showNotificationsToUserCheckBox, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        autoUpdateEnabledCheckBox = new JCheckBox();
        autoUpdateEnabledCheckBox.setContentAreaFilled(true);
        autoUpdateEnabledCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(autoUpdateEnabledCheckBox, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("autoUpdateEnabledCheckBox"));
        autoUpdateEnabledCheckBox.setVerifyInputWhenFocusTarget(false);
        autoUpdateEnabledCheckBox.setVerticalAlignment(0);
        panel3.add(autoUpdateEnabledCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkUpdatesButton = new JButton();
        this.$$$loadButtonText$$$(checkUpdatesButton, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("checkUpdatesButton"));
        panel3.add(checkUpdatesButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betaInstallCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(betaInstallCheckBox, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("betaUpdateInstallCheckBox"));
        betaInstallCheckBox.setToolTipText(ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("betaUpdateTooltip"));
        panel3.add(betaInstallCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        savePreviouslyOpenedFilesCheckBox = new JCheckBox();
        savePreviouslyOpenedFilesCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(savePreviouslyOpenedFilesCheckBox, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("savePreviouslyOpenedFilesCheckBox"));
        panel1.add(savePreviouslyOpenedFilesCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cleanupRecentFilesButton = new JButton();
        cleanupRecentFilesButton.setText("Clear");
        cleanupRecentFilesButton.setToolTipText("Remove all records about recent opened files");
        panel1.add(cleanupRecentFilesButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        versionLabel = new JLabel();
        this.$$$loadLabelText$$$(versionLabel, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("versionLabel"));
        panel4.add(versionLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        versionStringLabel = new JLabel();
        this.$$$loadLabelText$$$(versionStringLabel, ResourceBundle.getBundle("translations/MainSetterPanelBundle").getString("versionString"));
        panel4.add(versionStringLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        aboutButton = new JButton();
        this.$$$loadButtonText$$$(aboutButton, ResourceBundle.getBundle("translations/SettingsDialogBundle").getString("buttonAbout"));
        panel4.add(aboutButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        languageLabel = new JLabel();
        this.$$$loadLabelText$$$(languageLabel, ResourceBundle.getBundle("translations/LocaleSetterPanelBundle").getString("language"));
        panel5.add(languageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        localeComboBox = new JComboBox();
        panel5.add(localeComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPane.add(separator1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        contentPane.add(separator2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        versionLabel.setText(CoreUtils.getApplicationVersionString());

        cleanupRecentFilesButton.setEnabled(new RecentFilesManager().historyExistsForCurrentUser());

        initListeners();

        fillLocaleComboBox();
        initLocaleComboBox();
        translate();
    }

    private void initListeners() {
        aboutButton.addActionListener(e -> onAbout());
        cleanupRecentFilesButton.addActionListener(e -> onRecentFilesClear());
    }

    private void onRecentFilesClear() {
        final boolean cleanup = new RecentFilesManager().cleanup();
        cleanupRecentFilesButton.setEnabled(!cleanup);
    }

    private void initUpdateButtons() {
        if (launcherLocationPath != null) {
            betaInstallCheckBox.setVisible(true);
            checkUpdatesButton.setVisible(true);
            autoUpdateEnabledCheckBox.setVisible(true);
            checkUpdatesButton.addActionListener(e -> onUpdateNow());
        } else {
            betaInstallCheckBox.setVisible(false);
            checkUpdatesButton.setVisible(false);
            autoUpdateEnabledCheckBox.setVisible(false);
        }
    }

    private void onAbout() {
        final AboutApplicationDialog aboutWindow = new WindowLauncher<AboutApplicationDialog>() {
            @Override
            public AboutApplicationDialog initWindow() {
                return new AboutApplicationDialog();
            }
        }.getWindow();
        FrameUtils.setWindowOnParentWindowCenter(FrameUtils.findWindow(this), aboutWindow);
        aboutWindow.setVisible(true);
    }

    @Override
    public void loadSettings() {
        autoUpdateEnabledCheckBox.setSelected(PreferencesManager.isAutoUpdateActive());
        betaInstallCheckBox.setSelected(PreferencesManager.isBetaUpdateInstalling());
        openFolderForQRCheckBox.setSelected(PreferencesManager.openFolderForQrCode());
        showNotificationsToUserCheckBox.setSelected(PreferencesManager.isNotificationsShown());
        localeComboBox.setSelectedItem(PreferencesManager.getLocale());
        savePreviouslyOpenedFilesCheckBox.setSelected(PreferencesManager.isRecentOpenedFilesHistoryEnabled());
    }

    @Override
    public void saveSettings() {
        final boolean update = autoUpdateEnabledCheckBox.isSelected();
        final boolean beta = betaInstallCheckBox.isSelected();
        final boolean folder = openFolderForQRCheckBox.isSelected();
        final boolean notification = showNotificationsToUserCheckBox.isSelected();
        final boolean saveOpenedFiles = savePreviouslyOpenedFilesCheckBox.isSelected();

        saveLocale();

        log.info("Saving settings: " +
                        "auto-update: {}, " +
                        "beta installing: {}, " +
                        "open folder for qr-code: {}, " +
                        "notifications available: {}," +
                        "save previously opened files enabled: {}",
                update, beta, folder, notification, saveOpenedFiles);

        PreferencesManager.setAutoUpdateActive(update);
        PreferencesManager.setBetaUpdateInstalling(beta);
        PreferencesManager.setOpenFolderForQrCode(folder);
        PreferencesManager.setNotificationsShown(notification);
        PreferencesManager.setRecentOpenedFilesHistoryEnable(saveOpenedFiles);
    }

    private void saveLocale() {
        final Object selectedItem = localeComboBox.getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem instanceof Locale) {
                log.info("Saving settings: locale: {}", selectedItem);
                PreferencesManager.setLocale(((Locale) selectedItem));
            } else {
                log.info("Saving settings: locale: {}", SettingsConstants.LOCALE_DEFAULT_VALUE);
                PreferencesManager.setLocale(null);
            }
        } else {
            log.warn("Not settings: locale: value is null");
        }
    }

    @Override
    public void translate() {
        Translation translation = new Translation("MainSetterPanelBundle");
        versionStringLabel.setText(translation.getTranslatedString("versionString"));
        versionLabel.setText(CoreUtils.getApplicationVersionString());
        aboutButton.setText(Translation.getTranslatedString("SettingsDialogBundle", "buttonAbout"));

        autoUpdateEnabledCheckBox.setText(translation.getTranslatedString("autoUpdateEnabledCheckBox"));
        checkUpdatesButton.setText(translation.getTranslatedString("checkUpdatesButton"));
        openFolderForQRCheckBox.setText(translation.getTranslatedString("openFolderForQRCheckBox"));
        showNotificationsToUserCheckBox.setText(translation.getTranslatedString("showNotificationsCheckBox"));
        savePreviouslyOpenedFilesCheckBox.setText(translation.getTranslatedString("savePreviouslyOpenedFilesCheckBox"));

        betaInstallCheckBox.setText(translation.getTranslatedString("betaUpdateInstallCheckBox"));
        betaInstallCheckBox.setToolTipText(translation.getTranslatedString("betaUpdateTooltip"));

        translateLanguagePanel();
    }

    private void translateLanguagePanel() {
        Translation translation = new Translation("LocaleSetterPanelBundle");
        languageLabel.setText(translation.getTranslatedString("language"));
        fillLocaleComboBox();
        localeComboBox.setSelectedItem(PreferencesManager.getLocale());
    }

    private void onUpdateNow() {
        FrameUtils.findWindow(this).dispose();
        Application.launchApplication(launcherLocationPath, ApplicationArgument.OPENER_UPDATE_ARGUMENT.getArgument());
    }
}
