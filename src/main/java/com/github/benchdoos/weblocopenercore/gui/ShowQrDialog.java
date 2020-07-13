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
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.ExtendedFileAnalyzer;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.service.clipboard.ClipboardManager;
import com.github.benchdoos.weblocopenercore.service.gui.MousePickListener;
import com.github.benchdoos.weblocopenercore.service.notification.NotificationManager;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.google.zxing.WriterException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

@Log4j2
public class ShowQrDialog extends JFrame implements Translatable {
    private final String url;
    private final BufferedImage qrCodeImage;
    private final File weblocFile;
    private JPanel contentPane;
    private ImagePanel imagePanel;
    private JButton openButton;
    private JButton saveImageButton;
    private JButton copyImageButton;


    public ShowQrDialog(File weblocFile) {
        this.weblocFile = weblocFile;
        try {

            url = new ExtendedFileAnalyzer(weblocFile.getAbsolutePath()).getLinkFile().getUrl().toString();

            this.qrCodeImage = UrlsProceed.generateQrCode(url);
            $$$setupUI$$$();
            initGui();
        } catch (IOException | WriterException e) {
            throw new RuntimeException(e);
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
        contentPane.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setBackground(new Color(-1));
        contentPane.add(imagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        contentPane.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 5, new Insets(0, 10, 5, 10), -1, -1));
        panel1.setBackground(new Color(-1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveImageButton = new JButton();
        saveImageButton.setIcon(new ImageIcon(getClass().getResource("/images/downloadsIcon16.png")));
        saveImageButton.setText("");
        saveImageButton.setToolTipText(this.$$$getMessageFromBundle$$$("translations/ShowQrDialogBundle", "saveImageButton"));
        panel1.add(saveImageButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        openButton = new JButton();
        this.$$$loadButtonText$$$(openButton, this.$$$getMessageFromBundle$$$("translations/ShowQrDialogBundle", "openButton"));
        panel1.add(openButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyImageButton = new JButton();
        copyImageButton.setIcon(new ImageIcon(getClass().getResource("/images/copyIcon16.png")));
        copyImageButton.setText("");
        copyImageButton.setToolTipText(this.$$$getMessageFromBundle$$$("translations/ShowQrDialogBundle", "copyButton"));
        panel1.add(copyImageButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final DonationButton donationButton1 = new DonationButton();
        panel1.add(donationButton1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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

    private void createImageForFile(File qrFile) {
        try {
            final BufferedImage image = UrlsProceed.generateQrCode(url);
            ImageIO.write(image, "jpg", qrFile);
        } catch (Exception ex) {
            log.warn("Could not save image to {}", qrFile, ex);
        }
    }

    private void createImagePanel() {
        imagePanel = new ImagePanel(qrCodeImage);
        MousePickListener mousePickListener = new MousePickListener(this);

        imagePanel.addMouseListener(mousePickListener.getMouseAdapter);
        imagePanel.addMouseMotionListener(mousePickListener.getMouseMotionAdapter);
    }

    private void createUIComponents() {
        createImagePanel();
    }

    private void initActionListeners() {
        copyImageButton.addActionListener(e -> {
            try {
                final BufferedImage image = UrlsProceed.generateQrCode(url);
                ClipboardManager.getClipboardForSystem().copy(image);

                NotificationManager.getNotificationForCurrentOS().showInfoNotification(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                        Translation.get("ShowQrDialogBundle", "successCopyImage"));
            } catch (IOException | WriterException ex) {
                log.warn("Could not create qr-code image for url: {}", url, ex);
                NotificationManager.getNotificationForCurrentOS().showErrorNotification(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                        Translation.get("ShowQrDialogBundle", "errorCopyImage"));
            }
        });

        openButton.addActionListener(e -> {
            UrlsProceed.openUrl(url);
            dispose();
        });

        saveImageButton.addActionListener(e -> {
            try {
                final String s = CoreUtils.getFileName(weblocFile);
                File qrFile = new File(weblocFile.getParentFile()
                        + File.separator + s + "-qr.jpg");
                createImageForFile(qrFile);

                if (PreferencesManager.openFolderForQrCode()) {
                    FileUtils.openFileInFileBrowser(qrFile);
                } else {
                    log.debug("Opening folder for qr-code is blocked by settings");
                }
            } catch (Exception e1) {
                log.warn("Could not save qr-code image", e1);
            }
            dispose();
        });
    }

    private void initGui() {

        setTitle(weblocFile.getName() + " — " + Translation.get("ShowQrDialogBundle", "windowTitle"));
        setIconImage(Toolkit.getDefaultToolkit().getImage(ShowQrDialog.class.getResource("/images/qrCode256.png")));


        setContentPane(contentPane);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        imagePanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(
                KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        initActionListeners();

        pack();
        setResizable(false);

        FrameUtils.setWindowOnScreenCenter(this);
        translate();
    }

    private void onCancel() {
        dispose();
    }

    @Override
    public void translate() {
        Translation translation = new Translation("ShowQrDialogBundle");
        copyImageButton.setToolTipText(translation.get("copyButton"));
        saveImageButton.setToolTipText(translation.get("saveImageButton"));
        openButton.setText(translation.get("openButton"));
    }
}
