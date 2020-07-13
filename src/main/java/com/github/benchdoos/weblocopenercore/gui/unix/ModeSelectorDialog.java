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

package com.github.benchdoos.weblocopenercore.gui.unix;

import com.github.benchdoos.weblocopenercore.core.Application;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationArgument;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.ResourceBundle;

@Log4j2
public class ModeSelectorDialog extends JFrame implements Translatable {
    private final File file;
    private ApplicationArgument mode = PreferencesManager.getUnixOpeningMode();
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton openRadioButton;
    private JRadioButton editRadioButton;
    private JCheckBox saveSelectionCheckBox;
    private JRadioButton copyRadioButton;
    private JRadioButton generateQrRadioButton;
    private JRadioButton copyQrRadioButton;
    private JLabel windowTitleLabel;
    private ButtonGroup buttonGroup;

    public ModeSelectorDialog(File file) {
        this.file = file;
        initGui();

    }

    public void initGui() {
        setContentPane(contentPane);
        setTitle(file.getName());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/balloonIcon256.png")));

        getRootPane().setDefaultButton(buttonOK);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initActionListeners();

        initButtonGroup();

        initSelectionMode();

        translate();

        pack();
        FrameUtils.setWindowOnScreenCenter(this);
        setResizable(false);
    }

    private void initSelectionMode() {
        if (mode.equals(SettingsConstants.OPENER_UNIX_DEFAULT_SELECTOR_MODE)) {
            mode = ApplicationArgument.OPENER_OPEN_ARGUMENT;
        }

        switch (mode) {
            case OPENER_EDIT_ARGUMENT:
                editRadioButton.setSelected(true);
                break;
            case OPENER_QR_ARGUMENT:
                generateQrRadioButton.setSelected(true);
                break;
            case OPENER_COPY_LINK_ARGUMENT:
                copyRadioButton.setSelected(true);
                break;
            case OPENER_COPY_QR_ARGUMENT:
                copyQrRadioButton.setSelected(true);
                break;
            default:
                openRadioButton.setSelected(true);
                break;

        }
    }

    private void initButtonGroup() {
        buttonGroup = new ButtonGroup();
        buttonGroup.add(openRadioButton);
        buttonGroup.add(editRadioButton);
        buttonGroup.add(copyRadioButton);
        buttonGroup.add(generateQrRadioButton);
        buttonGroup.add(copyQrRadioButton);
    }

    private void initActionListeners() {
        buttonOK.addActionListener(e -> onOk());
        buttonCancel.addActionListener(e -> onCancel());

        rootPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        rootPane.registerKeyboardAction(e -> onSelectionMove(-1), KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
        rootPane.registerKeyboardAction(e -> onSelectionMove(1), KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );


        openRadioButton.addItemListener(getRadioButtonListener(ApplicationArgument.OPENER_OPEN_ARGUMENT));
        editRadioButton.addItemListener(getRadioButtonListener(ApplicationArgument.OPENER_EDIT_ARGUMENT));
        copyRadioButton.addItemListener(getRadioButtonListener(ApplicationArgument.OPENER_COPY_LINK_ARGUMENT));
        generateQrRadioButton.addItemListener(getRadioButtonListener(ApplicationArgument.OPENER_QR_ARGUMENT));
        copyQrRadioButton.addItemListener(getRadioButtonListener(ApplicationArgument.OPENER_COPY_QR_ARGUMENT));
    }

    private ItemListener getRadioButtonListener(ApplicationArgument mode) {
        return e -> {
            if (((JRadioButton) e.getItem()).isSelected()) {
                this.mode = mode;
            }
        };
    }

    private void onSelectionMove(int move) {
        final Enumeration<AbstractButton> elements = buttonGroup.getElements();
        ArrayList<JRadioButton> buttons = new ArrayList<>();
        while (elements.hasMoreElements()) {
            final AbstractButton abstractButton = elements.nextElement();
            buttons.add(((JRadioButton) abstractButton));
        }

        for (int i = 0; i < buttons.size(); i++) {
            JRadioButton current = buttons.get(i);
            if (current.isSelected()) {
                final int nextIndex = i + move;
                if (nextIndex >= 0) {
                    if (nextIndex < buttons.size()) {
                        buttons.get(nextIndex).setSelected(true);
                    } else {
                        buttons.get(0).setSelected(true);
                    }
                } else {
                    buttons.get(buttons.size() - 1).setSelected(true);
                }
                break;
            }
        }
    }

    private void onCancel() {
        dispose();
    }

    private void onOk() {
        if (saveSelectionCheckBox.isSelected()) {
            PreferencesManager.setUnixOpeningMode(mode);
            PreferencesManager.flushPreferences();
        }
        if (!mode.equals(SettingsConstants.OPENER_UNIX_DEFAULT_SELECTOR_MODE)) {
            log.info("Starting processing of file: {} in mode: {}", file, mode);
            String[] args = new String[]{mode.getArgument(), file.getAbsolutePath()};
            Application.manageArguments(args);
            dispose();
        } else {
            FrameUtils.shakeFrame(this);
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, this.$$$getMessageFromBundle$$$("translations/CommonsBundle", "okButton"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, this.$$$getMessageFromBundle$$$("translations/CommonsBundle", "cancelButton"));
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel3.add(spacer3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        saveSelectionCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(saveSelectionCheckBox, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "saveCheckBox"));
        saveSelectionCheckBox.setToolTipText(this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "saveCheckBoxToolTip"));
        panel3.add(saveSelectionCheckBox, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        panel3.add(separator1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        windowTitleLabel = new JLabel();
        this.$$$loadLabelText$$$(windowTitleLabel, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "windowTitle"));
        panel3.add(windowTitleLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 2, false));
        openRadioButton = new JRadioButton();
        openRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(openRadioButton, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "selectionOpen"));
        panel4.add(openRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(editRadioButton, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "selectionEdit"));
        panel4.add(editRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(copyRadioButton, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "selectionCopy"));
        panel4.add(copyRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        generateQrRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(generateQrRadioButton, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "selectionQr"));
        panel4.add(generateQrRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyQrRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(copyQrRadioButton, this.$$$getMessageFromBundle$$$("translations/ModeSelectorDialogBundle", "selectionCopyQr"));
        panel4.add(copyQrRadioButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(openRadioButton);
        buttonGroup.add(openRadioButton);
        buttonGroup.add(copyQrRadioButton);
        buttonGroup.add(editRadioButton);
        buttonGroup.add(copyRadioButton);
        buttonGroup.add(generateQrRadioButton);
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

    @Override
    public void translate() {
        Translation translation = new Translation("ModeSelectorDialogBundle");
        windowTitleLabel.setText(translation.get("windowTitle"));
        openRadioButton.setText(translation.get("selectionOpen"));
        editRadioButton.setText(translation.get("selectionEdit"));
        copyRadioButton.setText(translation.get("selectionCopy"));
        generateQrRadioButton.setText(translation.get("selectionQr"));
        copyQrRadioButton.setText(translation.get("selectionCopyQr"));
        saveSelectionCheckBox.setText(translation.get("saveCheckBox"));
        saveSelectionCheckBox.setToolTipText(translation.get("saveCheckBoxToolTip"));


        Translation common = new Translation("CommonsBundle");
        buttonOK.setText(common.get("okButton"));
        buttonCancel.setText(common.get("cancelButton"));
    }
}
