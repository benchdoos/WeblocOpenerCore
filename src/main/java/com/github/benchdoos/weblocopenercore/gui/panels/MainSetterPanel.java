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
import java.lang.reflect.Method;
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
    private JCheckBox shareInfoCheckBox;
    private String launcherLocationPath;

    public MainSetterPanel() {
        initGui();
    }

    @Override
    public String getName() {
        return Translation.get("SettingsDialogBundle", "settingsMainPanelName");
    }

    @Override
    public void setLauncherLocationPath(String launcherLocationPath) {
        this.launcherLocationPath = launcherLocationPath;
        initUpdateButtons();
    }

    private void fillLocaleComboBox() {
        String sysLocale = Translation.get("LocaleSetterPanelBundle", "languageDefault");
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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        openFolderForQRCheckBox = new JCheckBox();
        openFolderForQRCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(openFolderForQRCheckBox, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "openFolderForQRCheckBox"));
        panel2.add(openFolderForQRCheckBox, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showNotificationsToUserCheckBox = new JCheckBox();
        showNotificationsToUserCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(showNotificationsToUserCheckBox, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "showNotificationsCheckBox"));
        panel2.add(showNotificationsToUserCheckBox, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(5, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        savePreviouslyOpenedFilesCheckBox = new JCheckBox();
        savePreviouslyOpenedFilesCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(savePreviouslyOpenedFilesCheckBox, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "savePreviouslyOpenedFilesCheckBox"));
        panel2.add(savePreviouslyOpenedFilesCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cleanupRecentFilesButton = new JButton();
        this.$$$loadButtonText$$$(cleanupRecentFilesButton, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "cleanRecentFilesButtonText"));
        cleanupRecentFilesButton.setToolTipText(this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "cleanRecentFilesButtonToolTip"));
        panel2.add(cleanupRecentFilesButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel2.add(separator1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        shareInfoCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(shareInfoCheckBox, this.$$$getMessageFromBundle$$$("translations/ShareAnonymousInfoBundle", "shareUserInfoLabel"));
        shareInfoCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("translations/ShareAnonymousInfoBundle", "message"));
        panel2.add(shareInfoCheckBox, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        versionLabel = new JLabel();
        this.$$$loadLabelText$$$(versionLabel, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "versionLabel"));
        panel4.add(versionLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        versionStringLabel = new JLabel();
        this.$$$loadLabelText$$$(versionStringLabel, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "versionString"));
        panel4.add(versionStringLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        aboutButton = new JButton();
        this.$$$loadButtonText$$$(aboutButton, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "buttonAbout"));
        panel4.add(aboutButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        autoUpdateEnabledCheckBox = new JCheckBox();
        autoUpdateEnabledCheckBox.setContentAreaFilled(true);
        autoUpdateEnabledCheckBox.setSelected(true);
        this.$$$loadButtonText$$$(autoUpdateEnabledCheckBox, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "autoUpdateEnabledCheckBox"));
        autoUpdateEnabledCheckBox.setVerifyInputWhenFocusTarget(false);
        autoUpdateEnabledCheckBox.setVerticalAlignment(0);
        panel6.add(autoUpdateEnabledCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkUpdatesButton = new JButton();
        this.$$$loadButtonText$$$(checkUpdatesButton, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "checkUpdatesButton"));
        panel6.add(checkUpdatesButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betaInstallCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(betaInstallCheckBox, this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "betaUpdateInstallCheckBox"));
        betaInstallCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("translations/MainSetterPanelBundle", "betaUpdateTooltip"));
        panel6.add(betaInstallCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel5.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        languageLabel = new JLabel();
        this.$$$loadLabelText$$$(languageLabel, this.$$$getMessageFromBundle$$$("translations/LocaleSetterPanelBundle", "language"));
        panel7.add(languageLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        localeComboBox = new JComboBox();
        panel7.add(localeComboBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        panel7.add(separator2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
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
        shareInfoCheckBox.setSelected(PreferencesManager.isShareAnonymousInfoEnabled());
    }

    @Override
    public void saveSettings() {
        final boolean update = autoUpdateEnabledCheckBox.isSelected();
        final boolean beta = betaInstallCheckBox.isSelected();
        final boolean folder = openFolderForQRCheckBox.isSelected();
        final boolean notification = showNotificationsToUserCheckBox.isSelected();
        final boolean saveOpenedFiles = savePreviouslyOpenedFilesCheckBox.isSelected();
        final boolean shareAnonymousInfo = shareInfoCheckBox.isSelected();

        saveLocale();

        log.info("Saving settings: " +
                        "auto-update: {}, " +
                        "beta installing: {}, " +
                        "open folder for qr-code: {}, " +
                        "notifications available: {}," +
                        "save previously opened files enabled: {}," +
                        "share anonymous info enabled: {}",
                update, beta, folder, notification, saveOpenedFiles, shareAnonymousInfo);

        PreferencesManager.setAutoUpdateActive(update);
        PreferencesManager.setBetaUpdateInstalling(beta);
        PreferencesManager.setOpenFolderForQrCode(folder);
        PreferencesManager.setNotificationsShown(notification);
        PreferencesManager.setRecentOpenedFilesHistoryEnable(saveOpenedFiles);
        PreferencesManager.setShareAnonymousInfoEnabled(shareAnonymousInfo);
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
        final Translation translation = new Translation("MainSetterPanelBundle");
        versionStringLabel.setText(translation.get("versionString"));
        versionLabel.setText(CoreUtils.getApplicationVersionString());
        aboutButton.setText(Translation.get("SettingsDialogBundle", "buttonAbout"));

        autoUpdateEnabledCheckBox.setText(translation.get("autoUpdateEnabledCheckBox"));
        checkUpdatesButton.setText(translation.get("checkUpdatesButton"));
        openFolderForQRCheckBox.setText(translation.get("openFolderForQRCheckBox"));
        showNotificationsToUserCheckBox.setText(translation.get("showNotificationsCheckBox"));
        savePreviouslyOpenedFilesCheckBox.setText(translation.get("savePreviouslyOpenedFilesCheckBox"));

        betaInstallCheckBox.setText(translation.get("betaUpdateInstallCheckBox"));
        betaInstallCheckBox.setToolTipText(translation.get("betaUpdateTooltip"));

        cleanupRecentFilesButton.setText(translation.get("cleanRecentFilesButtonText"));
        cleanupRecentFilesButton.setToolTipText(translation.get("cleanRecentFilesButtonToolTip"));

        shareInfoCheckBox.setText(Translation.get("ShareAnonymousInfoBundle", "shareUserInfoLabel"));
        shareInfoCheckBox.setToolTipText(Translation.get("ShareAnonymousInfoBundle", "message"));

        translateLanguagePanel();
    }

    private void translateLanguagePanel() {
        final Translation translation = new Translation("LocaleSetterPanelBundle");
        languageLabel.setText(translation.get("language"));
        fillLocaleComboBox();
        localeComboBox.setSelectedItem(PreferencesManager.getLocale());
    }

    private void onUpdateNow() {
        FrameUtils.findWindow(this).dispose();
        Application.launchApplication(launcherLocationPath, ApplicationArgument.OPENER_UPDATE_ARGUMENT.getArgument());
    }
}
