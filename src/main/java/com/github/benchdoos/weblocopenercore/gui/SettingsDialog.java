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

package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.gui.buttons.DonationButton;
import com.github.benchdoos.weblocopenercore.gui.panels.AppearanceSetterPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.BrowserSetterPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.FileProcessingPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.MainSetterPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.ResentOpenedFilesPanel;
import com.github.benchdoos.weblocopenercore.gui.panels.SettingsPanel;
import com.github.benchdoos.weblocopenercore.gui.wrappers.CreateNewFileDialogWrapper;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.WindowLauncher;
import com.github.benchdoos.weblocopenercore.service.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.GuiUtils;
import com.github.benchdoos.weblocopenercore.utils.system.OS;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.github.benchdoos.weblocopenercore.utils.FrameUtils.setWindowOnScreenCenter;

@Log4j2
public class SettingsDialog extends JFrame implements Translatable {
    private static final int MINIMAL_MODE_SQUARE = 32;
    private static final int MINIMAL_MODE_DIVIDER_LOCATION = MINIMAL_MODE_SQUARE + 6;
    private Timer settingsSavedTimer = null;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane scrollpane;
    private JList<IconJList.IconObject<SettingsPanel>> settingsList;
    private JPanel scrollPaneContent;
    private JButton buttonApply;
    private JLabel settingsSavedLabel;
    private JLabel dragAndDropNotice;
    private JButton createNewFileButton;
    private DonationButton donationButton;
    private JSplitPane splitPane;
    private JButton feedbackButton;
    private BrowserSetterPanel browserSetterPanel;
    private MainSetterPanel mainSetterPanel;
    private AppearanceSetterPanel appearanceSetterPanel;
    private FileProcessingPanel fileProcessingPanel;
    private ResentOpenedFilesPanel recentOpenedFilesPanel;
    private final String launcherLocationPath;


    public SettingsDialog(String launcherLocationPath) {
        this.launcherLocationPath = launcherLocationPath;
        $$$setupUI$$$();
        log.debug("Creating settings dialog.");
        log.debug("Settings dialog launcher location: {}", launcherLocationPath);
        initGui();
        enableMinimalMode();
        log.debug("Settings dialog created.");
    }

    private void enableMinimalMode() {
        if (PreferencesManager.isMinimalListModeEnabled()) {
            splitPane.setDividerLocation(MINIMAL_MODE_DIVIDER_LOCATION);
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
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 6, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "buttonOk"));
        panel2.add(buttonOK, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "buttonCancel"));
        panel2.add(buttonCancel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonApply = new JButton();
        this.$$$loadButtonText$$$(buttonApply, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "buttonApply"));
        panel2.add(buttonApply, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dragAndDropNotice = new JLabel();
        dragAndDropNotice.setForeground(new Color(-6316129));
        this.$$$loadLabelText$$$(dragAndDropNotice, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "dragAndDropNotice"));
        panel1.add(dragAndDropNotice, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        settingsSavedLabel = new JLabel();
        settingsSavedLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiSuccess16.png")));
        settingsSavedLabel.setToolTipText(this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "settingsSaved"));
        panel1.add(settingsSavedLabel, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createNewFileButton = new JButton();
        Font createNewFileButtonFont = this.$$$getFont$$$("Courier New", Font.BOLD, 18, createNewFileButton.getFont());
        if (createNewFileButtonFont != null) createNewFileButton.setFont(createNewFileButtonFont);
        createNewFileButton.setMargin(new Insets(0, 5, 0, 5));
        createNewFileButton.setText("+");
        createNewFileButton.setToolTipText(this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "createNewFile"));
        panel1.add(createNewFileButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        donationButton = new DonationButton();
        panel1.add(donationButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        feedbackButton = new JButton();
        this.$$$loadButtonText$$$(feedbackButton, this.$$$getMessageFromBundle$$$("translations/SettingsDialogBundle", "feedbackButton"));
        panel1.add(feedbackButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        splitPane = new JSplitPane();
        contentPane.add(splitPane, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setMinimumSize(new Dimension(128, 15));
        splitPane.setLeftComponent(scrollPane1);
        settingsList.setSelectionMode(0);
        scrollPane1.setViewportView(settingsList);
        scrollpane = new JScrollPane();
        splitPane.setRightComponent(scrollpane);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollpane.setViewportView(panel3);
        panel3.add(scrollPaneContent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
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

    private void createUIComponents() {
        scrollPaneContent = new JPanel();
        scrollPaneContent.setLayout(new GridLayout());
        settingsList = new IconJList<>(20, MINIMAL_MODE_SQUARE);
    }


    private void initDragAndDropTarget() {
        final DropTarget dropTarget = new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                onDrop(evt);
            }

            private void onDrop(DropTargetDropEvent evt) {
                final Translation translation = new Translation("SettingsDialogBundle");
                try {
                    contentPane.setBorder(null);
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    final Object transferData = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    final List<?> list = (List<?>) transferData;
                    final ArrayList<File> files = new ArrayList<>(list.size());

                    for (final Object object : list) {
                        if (object instanceof File) {
                            files.add((File) object);
                        }
                    }

                    final ConverterDialog converterDialog = new WindowLauncher<ConverterDialog>() {
                        @Override
                        public ConverterDialog initWindow() {
                            return new ConverterDialog(files);
                        }
                    }.getWindow();
                    FrameUtils.setWindowOnParentWindowCenter(getCurrentWindow(), converterDialog);
                    converterDialog.setVisible(true);
                } catch (final Exception ex) {
                    log.warn("Can not open files from drop", ex);
                    NotificationManager.getNotificationForCurrentOS().showErrorNotification(
                            ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                            translation.get("couldNotConvertFiles"));
                }
            }
        };
        contentPane.setDropTarget(dropTarget);

        FrameUtils.appendRedBorderToComponent(dropTarget, contentPane);
    }

    @NotNull
    private Window getCurrentWindow() {
        return this;
    }

    private void initGui() {
        setContentPane(contentPane);
        setTitle(Translation.get("SettingsDialogBundle", "windowTitle"));

        getRootPane().setDefaultButton(buttonOK);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/balloonIcon256.png")));

        initSettingsList();

        fillSettingsList();

        loadSettings();

        initSplitPane();

        initButtons();

        initKeyListeners();

        scrollpane.setBorder(null);

        settingsSavedLabel.setVisible(false);

        initDragAndDropTarget();

        browserSetterPanel.init(); //don't forget it or it will crash fileBrowser

        pack();
        setMinimumSize(new Dimension(768, 400));
        setWindowOnScreenCenter(this);
        translate();
    }

    private void initKeyListeners() {
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        contentPane.registerKeyboardAction(e -> createNewFile(), KeyStroke.getKeyStroke("control N"),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void initButtons() {
        buttonApply.addActionListener(e -> onApply());

        buttonOK.addActionListener(e -> onSave());

        buttonCancel.addActionListener(e -> onCancel());

        createNewFileButton.addActionListener(e -> createNewFile());

        feedbackButton.addActionListener(e -> createFeedBackButton());
    }

    private void createFeedBackButton() {
        final FeedbackDialog feedbackDialog = new WindowLauncher<FeedbackDialog>() {
            @Override
            public FeedbackDialog initWindow() {
                return new FeedbackDialog();
            }
        }.getWindow();
        FrameUtils.setWindowOnScreenCenter(feedbackDialog);
        feedbackDialog.setVisible(true);
    }

    private void initSplitPane() {
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY,
                e -> {
                    final Integer newValue = (Integer) e.getNewValue();

                    final boolean minimalMode = newValue < 145;
                    ((IconJList) settingsList).setMinimalMode(minimalMode);
                    if (minimalMode) {
                        splitPane.setDividerLocation(MINIMAL_MODE_DIVIDER_LOCATION);
                    }

                    updateMinimalModeValue(minimalMode);
                });
        if (OS.isUnix()) {
            GuiUtils.appendToSplitPaneDividerIcon(splitPane);
        }

        splitPane.setBorder(null);
    }

    private void updateMinimalModeValue(boolean minimalMode) {
        if (PreferencesManager.isMinimalListModeEnabled() != minimalMode) {
            log.info("Updating minimal mode");
            PreferencesManager.setMinimalListModeEnabled(minimalMode);
            PreferencesManager.flushPreferences();
        }
    }

    private void createNewFile() {
        final CreateNewFileDialogWrapper jFrameWrapper = new WindowLauncher<CreateNewFileDialogWrapper>() {
            @Override
            public CreateNewFileDialogWrapper initWindow() {
                return new CreateNewFileDialogWrapper();
            }
        }.getWindow();

        jFrameWrapper.setModal(true);
        FrameUtils.setWindowOnParentWindowCenter(this, jFrameWrapper);
        jFrameWrapper.setVisible(true);
    }

    private void initSettingsList() {
        settingsList.addListSelectionListener(e -> {
            scrollPaneContent.removeAll();
            scrollPaneContent.add((Component) settingsList.getSelectedValue().getObject());
            scrollpane.updateUI();
        });
    }

    private void loadSettings() {
        final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            final IconJList.IconObject<SettingsPanel> elementAt = model.getElementAt(i);
            final SettingsPanel settingsPanel = elementAt.getObject();
            settingsPanel.loadSettings();
        }
        settingsList.setSelectedIndex(0);
    }

    private void fillSettingsList() {
        final DefaultListModel<IconJList.IconObject<SettingsPanel>> model = new DefaultListModel<>();

        mainSetterPanel = new MainSetterPanel();
        mainSetterPanel.setLauncherLocationPath(launcherLocationPath);

        browserSetterPanel = new BrowserSetterPanel();
        appearanceSetterPanel = new AppearanceSetterPanel();
        fileProcessingPanel = new FileProcessingPanel();
        recentOpenedFilesPanel = new ResentOpenedFilesPanel();

        addSettingsPanelToModel(
                model,
                "/images/lists/selected/baseline_settings_applications_white_36dp.png",
                "/images/lists/unselected/baseline_settings_applications_black_36dp.png",
                mainSetterPanel);
        addSettingsPanelToModel(
                model,
                "/images/lists/selected/baseline_link_white_36dp.png",
                "/images/lists/unselected/baseline_link_black_36dp.png",
                browserSetterPanel);

        addSettingsPanelToModel(
                model,
                "/images/lists/selected/baseline_insert_drive_file_white_36dp.png",
                "/images/lists/unselected/baseline_insert_drive_file_black_36dp.png",
                fileProcessingPanel);

        addSettingsPanelToModel(
                model,
                "/images/lists/selected/baseline_remove_red_eye_white_36dp.png",
                "/images/lists/unselected/baseline_remove_red_eye_black_36dp.png",
                appearanceSetterPanel);

        addSettingsPanelToModel(
                model,
                "/images/lists/selected/baseline_settings_backup_restore_white_36dp.png",
                "/images/lists/unselected/baseline_settings_backup_restore_black_36dp.png",
                recentOpenedFilesPanel);

        settingsList.setModel(model);
    }

    private void addSettingsPanelToModel(DefaultListModel<IconJList.IconObject<SettingsPanel>> model,
                                         String selectedImageUrl,
                                         String unselectedImageUrl,
                                         SettingsPanel panel) {
        try {
            final BufferedImage selected = ImageIO.read(SettingsDialog.class.getResourceAsStream(selectedImageUrl));
            final BufferedImage unselected = ImageIO.read(SettingsDialog.class.getResourceAsStream(unselectedImageUrl));
            model.addElement(new IconJList.IconObject<>(selected, unselected, panel));
        } catch (final IOException e) {
            log.warn("Can not load images from given source: '{}','{}'", selectedImageUrl, unselectedImageUrl);
            model.addElement(new IconJList.IconObject<>(null, null, panel));
        }

    }


    private void onApply() {
        saveSettings();
        SwingUtilities.invokeLater(this::updateUIDarkMode);
        updateLocale();

        showOnApplyMessage();
    }

    @Override
    public void translate() {
        final Translation translation = new Translation("SettingsDialogBundle");
        setTitle(translation.get("windowTitle"));
        settingsSavedLabel.setToolTipText(translation.get("settingsSaved"));
        buttonApply.setText(translation.get("buttonApply"));
        buttonOK.setText(translation.get("buttonOk"));
        buttonCancel.setText(translation.get("buttonCancel"));

        createNewFileButton.setToolTipText(translation.get("createNewFile"));
        feedbackButton.setText(translation.get("feedbackButton"));

        dragAndDropNotice.setText(translation.get("dragAndDropNotice"));

        refreshSettingsList();
        settingsList.updateUI();
    }

    private void refreshSettingsList() {
        final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            final SettingsPanel panel = model.getElementAt(i).getObject();
            panel.setName(null);
        }
    }

    private void updateLocale() {
        final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            final IconJList.IconObject<SettingsPanel> elementAt = model.getElementAt(i);
            if (elementAt != null) {
                final SettingsPanel object = elementAt.getObject();

                if (object instanceof Translatable) {
                    final Translatable translatable = ((Translatable) object);
                    translatable.translate();
                }
            }
        }
        this.translate();
    }

    private void onCancel() {
        final DefaultListModel<IconJList.IconObject<SettingsPanel>> model =
                (DefaultListModel<IconJList.IconObject<SettingsPanel>>) settingsList.getModel();

        for (int i = 0; i < model.size(); i++) {
            final SettingsPanel panel = model.get(i).getObject();
            if (panel instanceof Closeable) {
                try {
                    log.debug("Closing operations for panel: {}", panel.getClass().getName());
                    ((Closeable) panel).close();
                } catch (final IOException e) {
                    log.warn("Can not close operations at panel: {}", panel.getClass().getName(), e);
                }
            }
        }

        dispose();
    }

    private void onSave() {
        saveSettings();
        dispose();
    }

    private void showOnApplyMessage() {
        if (PreferencesManager.isNotificationsShown()) {
            NotificationManager.getNotificationForCurrentOS().showInfoNotification(
                    Translation.get(
                            "SettingsDialogBundle", "settingsSaved"), null);
        } else {
            showInsideSettingsWindowApplyMessage();
        }
    }

    private void showInsideSettingsWindowApplyMessage() {
        settingsSavedLabel.setVisible(true);
        if (settingsSavedTimer == null) {
            settingsSavedTimer = new Timer(5000, e -> settingsSavedLabel.setVisible(false));
            settingsSavedTimer.setRepeats(false);
        }
        settingsSavedTimer.restart();
    }

    private void saveSettings() {
        final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            final SettingsPanel panel = model.getElementAt(i).getObject();
            panel.saveSettings();
        }
        PreferencesManager.flushPreferences();
    }

    private void updateUIDarkMode() {
        CoreUtils.enableLookAndFeel();
        SwingUtilities.updateComponentTreeUI(this);
        try {
            final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();

            for (int i = 0; i < model.getSize(); i++) {
                final Object o = model.getElementAt(i).getObject();
                if (o instanceof Component) {
                    SwingUtilities.updateComponentTreeUI((Component) o);
                }
            }
        } catch (final Exception e) {
            log.warn("Could not colorize component", e);
        }
        //todo: add repaint to settingsList
    }

    public void loadSettingsForPanel(Class clazz) {
        if (clazz != null) {
            log.debug("Trying to load settings for panel with class: {}", clazz);
            final ListModel<IconJList.IconObject<SettingsPanel>> model = settingsList.getModel();
            if (model != null) {
                for (int i = 0; i < model.getSize(); i++) {
                    final SettingsPanel panel = model.getElementAt(i).getObject();
                    log.trace("Panel class is: {}, checking if class: {} is equal to it.", panel.getClass(), clazz);
                    if (panel.getClass().equals(clazz)) {
                        log.info("Loading settings for panel with class: {}", panel.getClass());
                        panel.loadSettings();
                        break;
                    }
                }
            }
        } else {
            log.warn("Given class is null");
        }
    }
}
