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

import com.github.benchdoos.jcolorful.core.JColorful;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import com.github.benchdoos.weblocopenercore.gui.buttons.DonationButton;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.service.WindowLauncher;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.version.ApplicationVersion;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.positioners.BalloonTipPositioner;
import net.java.balloontip.positioners.LeftAbovePositioner;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.MinimalBalloonStyle;
import net.java.balloontip.utils.TimingUtils;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import static javax.swing.BorderFactory.createEmptyBorder;

@Log4j2
public class AboutApplicationDialog extends JDialog {

    private static final int BALLOON_TIP_DELAY = 7_000;
    private JPanel contentPane;
    private JTextPane descriptionTextPane;
    private JLabel versionLabel;
    private JPanel imagePanel;
    private JScrollPane scrollPane;
    private JButton siteLinkButton;
    private JButton githubLinkButton;
    private JButton logButton;
    private JButton emailFeedbackButton;
    private JButton librariesButton;
    private JButton telegramButton;
    private JButton shareButton;
    private JButton donateByPayPalButton;
    private JButton donateByDonationAlertsButton;
    private JButton twitterButton;
    private JButton feedbackButton;
    private JPanel buttonsPanel;
    private String shareLabelText;
    private String shareBalloonMessage;


    public AboutApplicationDialog() {
        $$$setupUI$$$();
        translateDialog();
        initGui();

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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.setMaximumSize(new Dimension(550, 300));
        contentPane.setMinimumSize(new Dimension(550, 300));
        contentPane.setOpaque(true);
        contentPane.setPreferredSize(new Dimension(550, 300));
        imagePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(imagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setBackground(new Color(-9923881));
        panel1.setOpaque(false);
        imagePanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        panel2.setOpaque(false);
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setOpaque(false);
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setAlignmentX(0.0f);
        panel4.setAlignmentY(0.0f);
        panel4.setBackground(new Color(-9923881));
        panel4.setOpaque(false);
        panel4.setRequestFocusEnabled(true);
        panel4.setVisible(true);
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPane.setBackground(new Color(-9923881));
        scrollPane.setFocusable(false);
        scrollPane.setOpaque(false);
        scrollPane.setVisible(true);
        scrollPane.setWheelScrollingEnabled(true);
        panel4.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(400, 130), null, 0, false));
        descriptionTextPane.setBackground(new Color(-9923881));
        descriptionTextPane.setCaretColor(new Color(-1118482));
        descriptionTextPane.setContentType(ResourceBundle.getBundle("spelling").getString("text.html"));
        descriptionTextPane.setDragEnabled(false);
        descriptionTextPane.setEditable(false);
        descriptionTextPane.setEnabled(true);
        descriptionTextPane.setFocusCycleRoot(false);
        descriptionTextPane.setFocusable(false);
        descriptionTextPane.setOpaque(false);
        descriptionTextPane.setRequestFocusEnabled(false);
        descriptionTextPane.setText(ResourceBundle.getBundle("translations/AboutApplicationDialogBundle").getString("aboutAppInfo"));
        descriptionTextPane.setVerifyInputWhenFocusTarget(false);
        descriptionTextPane.setVisible(true);
        scrollPane.setViewportView(descriptionTextPane);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.setAlignmentX(0.0f);
        panel5.setAlignmentY(0.0f);
        panel5.setBackground(new Color(-9923881));
        panel5.setOpaque(false);
        panel3.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 24, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setForeground(new Color(-1));
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("spelling").getString("WeblocOpener"));
        panel5.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        versionLabel = new JLabel();
        Font versionLabelFont = this.$$$getFont$$$(null, -1, 11, versionLabel.getFont());
        if (versionLabelFont != null) versionLabel.setFont(versionLabelFont);
        versionLabel.setForeground(new Color(-3153931));
        this.$$$loadLabelText$$$(versionLabel, ResourceBundle.getBundle("translations/AboutApplicationDialogBundle").getString("appVersionLabel"));
        panel5.add(versionLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 10), -1, -1));
        panel6.setBackground(new Color(-9923881));
        panel6.setOpaque(false);
        panel3.add(panel6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        buttonsPanel.setOpaque(false);
        panel6.add(buttonsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel7.setEnabled(true);
        panel7.setOpaque(false);
        buttonsPanel.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(580, 34), null, 0, false));
        siteLinkButton = new JButton();
        siteLinkButton.setBorderPainted(false);
        siteLinkButton.setContentAreaFilled(false);
        this.$$$loadButtonText$$$(siteLinkButton, ResourceBundle.getBundle("spelling").getString("site"));
        panel7.add(siteLinkButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        githubLinkButton = new JButton();
        githubLinkButton.setBorderPainted(false);
        githubLinkButton.setContentAreaFilled(false);
        this.$$$loadButtonText$$$(githubLinkButton, ResourceBundle.getBundle("spelling").getString("github"));
        panel7.add(githubLinkButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        librariesButton = new JButton();
        librariesButton.setBorderPainted(false);
        librariesButton.setContentAreaFilled(false);
        this.$$$loadButtonText$$$(librariesButton, ResourceBundle.getBundle("translations/AboutApplicationDialogBundle").getString("librariesLabel"));
        panel7.add(librariesButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logButton = new JButton();
        logButton.setBorderPainted(false);
        logButton.setContentAreaFilled(false);
        this.$$$loadButtonText$$$(logButton, ResourceBundle.getBundle("translations/AboutApplicationDialogBundle").getString("logLabelTooltip"));
        panel7.add(logButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        panel8.setOpaque(false);
        buttonsPanel.add(panel8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(580, 34), null, 0, false));
        feedbackButton = new JButton();
        feedbackButton.setBorderPainted(false);
        feedbackButton.setContentAreaFilled(false);
        feedbackButton.setIcon(new ImageIcon(getClass().getResource("/images/icons/feedback16.png")));
        feedbackButton.setIconTextGap(0);
        feedbackButton.setOpaque(false);
        feedbackButton.setText("");
        feedbackButton.setToolTipText(ResourceBundle.getBundle("translations/AboutApplicationDialogBundle").getString("feedbackLabel"));
        panel8.add(feedbackButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        telegramButton = new JButton();
        telegramButton.setBorderPainted(false);
        telegramButton.setContentAreaFilled(false);
        telegramButton.setIcon(new ImageIcon(getClass().getResource("/images/telegramIcon16.png")));
        telegramButton.setOpaque(false);
        telegramButton.setText("");
        panel8.add(telegramButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        twitterButton = new JButton();
        twitterButton.setBorderPainted(false);
        twitterButton.setContentAreaFilled(false);
        twitterButton.setIcon(new ImageIcon(getClass().getResource("/images/twitterIcon16.png")));
        twitterButton.setOpaque(false);
        panel8.add(twitterButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        shareButton = new JButton();
        shareButton.setBorderPainted(false);
        shareButton.setContentAreaFilled(false);
        shareButton.setIcon(new ImageIcon(getClass().getResource("/images/shareIcon16.png")));
        shareButton.setText("");
        panel8.add(shareButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        donateByDonationAlertsButton = new JButton();
        donateByDonationAlertsButton.setBorderPainted(false);
        donateByDonationAlertsButton.setContentAreaFilled(false);
        donateByDonationAlertsButton.setIcon(new ImageIcon(getClass().getResource("/images/donationAlertsIcon16.png")));
        donateByDonationAlertsButton.setText("");
        panel8.add(donateByDonationAlertsButton, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(20, 20), new Dimension(20, 20), new Dimension(20, 20), 0, false));
        donateByPayPalButton.setBorderPainted(false);
        donateByPayPalButton.setContentAreaFilled(false);
        donateByPayPalButton.setIcon(new ImageIcon(getClass().getResource("/images/donate16.png")));
        donateByPayPalButton.setText("");
        panel8.add(donateByPayPalButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        buttonsPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
        ImageIcon image;
        if (!PreferencesManager.isDarkModeEnabledNow()) {
            image = new ImageIcon(getClass().getResource("/images/background.png"));
        } else {
            image = new ImageIcon(getClass().getResource("/images/backgroundBlack.png"));
        }
        imagePanel = new ImagePanel(image);
        descriptionTextPane = new JTextPane();

        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(createEmptyBorder());

        donateByPayPalButton = new DonationButton();
    }

    private void initGui() {
        log.debug("Creating GUI");
        setContentPane(contentPane);
        setIconImage(Toolkit.getDefaultToolkit().getImage(AboutApplicationDialog.class.getResource("/images/balloonIcon256.png")));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (PreferencesManager.isDarkModeEnabledNow()) {
            descriptionTextPane.setText(
                    Translation.get("AboutApplicationDialogBundle", "aboutAppInfo")
                            .replace("{}", "color:white;"));
        } else {
            descriptionTextPane.setText(
                    Translation.get("AboutApplicationDialogBundle", "aboutAppInfo")
                            .replace("{}", "color:black;"));
        }

        addCursorsToButtons(buttonsPanel.getComponents());

        initListeners();

        scrollPane.setViewportBorder(null);

        setModal(true);
        setSize(550, 300);
        setResizable(false);
        FrameUtils.setWindowOnScreenCenter(this);
        log.debug("GUI created");
    }

    private void addCursorsToButtons(final Component[] components) {
        for (Component component : components) {
            if (component instanceof JPanel) {
                addCursorsToButtons(((JPanel) component).getComponents());
            } else if (component instanceof JButton) {
                component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }
    }

    private void initListeners() {
        feedbackButton.addActionListener(e -> onFeedback());
        telegramButton.addActionListener(e -> UrlsProceed.openUrl(StringConstants.BENCH_DOOS_TELEGRAM_URL));
        twitterButton.addActionListener(e -> UrlsProceed.openUrl(StringConstants.WEBLOCOPENER_TWITTER_URL));
        shareButton.addActionListener(e -> shareWeblocOpener());
        donateByDonationAlertsButton.addActionListener(e -> UrlsProceed.openUrl(StringConstants.DONATE_DONATION_ALERTS_URL));
        siteLinkButton.addActionListener(e -> UrlsProceed.openUrl(StringConstants.UPDATE_WEB_URL));
        githubLinkButton.addActionListener(e -> UrlsProceed.openUrl(StringConstants.GITHUB_WEB_URL));
        librariesButton.addActionListener(e -> createInfoDialog());
        logButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().open(new File(PathConstants.APP_LOG_FOLDER_PATH));
            } catch (final IOException e1) {
                log.warn("Can not open log folder: " + PathConstants.APP_LOG_FOLDER_PATH);
            }
        });
    }

    private void onFeedback() {
        final FeedbackDialog feedbackDialog = new WindowLauncher<FeedbackDialog>() {
            @Override
            public FeedbackDialog initWindow() {
                return new FeedbackDialog();
            }
        }.getWindow();
        FrameUtils.setWindowOnParentWindowCenter(this, feedbackDialog);
        feedbackDialog.setVisible(true);
        dispose();
    }

    private void shareWeblocOpener() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final StringSelection stringSelection = new StringSelection(Translation.get("AboutApplicationDialog", "shareLabelText"));
        clipboard.setContents(stringSelection, null);

        createBalloonTip();
    }

    private void createBalloonTip() {
        final BalloonTip balloonTip = new BalloonTip(shareButton, shareBalloonMessage);
        balloonTip.setCloseButton(null);
        final BalloonTipStyle minimalStyle = new MinimalBalloonStyle(new JPanel().getBackground(), 5);
        balloonTip.setStyle(minimalStyle);
        final BalloonTipPositioner balloonTipPositioner = new LeftAbovePositioner(0, 0);
        balloonTip.setPositioner(balloonTipPositioner);

        TimingUtils.showTimedBalloon(balloonTip, BALLOON_TIP_DELAY);
    }

    private void createInfoDialog() {
        final InfoDialog infoDialog = new InfoDialog();
        if (PreferencesManager.isDarkModeEnabledNow()) {
            final JColorful colorful = new JColorful(ApplicationConstants.DARK_MODE_THEME);
            colorful.colorize(infoDialog);
        }

        infoDialog.setTitle(Translation.get("AboutApplicationDialogBundle", "librariesLabelToolTip"));

        infoDialog.setContent(getDarkStyle(CoreUtils.getContentFromLocalResource("/pages/libs.html")));
        infoDialog.setVisible(true);
    }

    private String getDarkStyle(String content) {
        if (PreferencesManager.isDarkModeEnabledNow()) {
            return content.replace("{}", "color:#ffffff;");
        } else {
            return content.replace("{}", "");
        }
    }

    private String createHtmlLink(String string) {
        if (!PreferencesManager.isDarkModeEnabledNow()) {
            return "<html><a href=\"\">" + string + "</a></html>";
        } else {
            return "<html><a style=\"color:#BAE5FF;\" href=\"\">" + string + "</a></html>";
        }
    }

    private void translateDialog() {

        final Translation translation = new Translation("AboutApplicationDialogBundle");
        setTitle(translation.get("windowTitle"));

        final ApplicationVersion currentApplicationVersion = CoreUtils.getCurrentApplicationVersion();
        final String versionAdditionalInfo = currentApplicationVersion.isBeta() ?
                " (beta." + currentApplicationVersion.getBeta().getVersion() + ")" : "";

        versionLabel.setText(translation.get("appVersionLabel") + " " + currentApplicationVersion.getVersion() + versionAdditionalInfo);

        siteLinkButton.setText(createHtmlLink(translation.get("visitLabel")));
        siteLinkButton.setToolTipText(StringConstants.UPDATE_WEB_URL);

        githubLinkButton.setText(createHtmlLink("Github"));
        githubLinkButton.setToolTipText(StringConstants.GITHUB_WEB_URL);

        librariesButton.setText(createHtmlLink(translation.get("librariesLabel")));

        logButton.setText(createHtmlLink(translation.get("logLabel")));
        logButton.setToolTipText(translation.get("logLabelTooltip"));

        feedbackButton.setToolTipText(translation.get("feedbackLabel"));

        telegramButton.setToolTipText(translation.get("telegramLabel"));

        twitterButton.setToolTipText(translation.get("twitterLabel"));

        shareBalloonMessage = translation.get("shareBalloonMessage");

        shareButton.setToolTipText(translation.get("shareLabel"));

        shareLabelText = translation.get("shareLabelText");

        donateByPayPalButton.setToolTipText(translation.get("donateLabelTooltip"));
        donateByDonationAlertsButton.setToolTipText(translation.get("donateByDonationAlertsLabelTooltip"));
    }

}
