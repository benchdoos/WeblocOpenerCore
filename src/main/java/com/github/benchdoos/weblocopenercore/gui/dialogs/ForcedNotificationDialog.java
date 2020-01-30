package com.github.benchdoos.weblocopenercore.gui.dialogs;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import com.github.benchdoos.weblocopenercore.service.UrlsProceed;
import com.github.benchdoos.weblocopenercore.service.clipboard.ClipboardManager;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.model.notification.Notification;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ForcedNotificationDialog extends JDialog implements Notification {
    private static final int MAXIMUM_MESSAGE_SIZE = 150;

    private JPanel contentPane;
    private JButton buttonOK;
    private JEditorPane notificationEditorPane;
    private JLabel iconLabel;
    private JButton copyMessage;
    private Component component;
    private String message;

    public ForcedNotificationDialog(Component component) {
        this.component = component;
        $$$setupUI$$$();
        initGui();
    }

    private void initListeners() {
        buttonOK.addActionListener(e -> onOK());
        copyMessage.addActionListener(e -> onCopyErrorMessage());
    }

    private void onCopyErrorMessage() {
        ClipboardManager.getClipboardForSystem().copy(message);
    }

    private void initGui() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);


        notificationEditorPane.setHighlighter(null);
        notificationEditorPane.setEditable(false);
        notificationEditorPane.getCaret().deinstall(notificationEditorPane);

        notificationEditorPane.addHyperlinkListener(e -> {
            if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                UrlsProceed.openUrl(e.getURL());
            }
        });

        notificationEditorPane.setBackground(notificationEditorPane.getParent().getBackground());
        UIManager.put("TextPane.disabledBackground", notificationEditorPane.getParent().getBackground());

        initListeners();

        final Dimension dimension = new Dimension(450, 250);
        setSize(dimension);
        setMinimumSize(dimension);
        pack();
        FrameUtils.setWindowOnParentWindowCenter(FrameUtils.findWindow(component), this);

    }

    private void onOK() {
        dispose();
    }

    private void showMessage(String title, String message, int messageLevel) {
        this.message = message;
        final String finalMessage;
        if (messageLevel == JOptionPane.ERROR_MESSAGE) {

            final String reportIssue = Translation.get("Commons", "reportIssue");
            finalMessage = "<HTML><BODY>" + message + "<br><br>" + reportIssue + " " +
                    "<a href=\"" + StringConstants.GITHUB_REPORT_ISSUE + "\">"
                    + StringConstants.GITHUB_NAME + "</a></BODY></HTML>";
        } else {
            finalMessage = message;
        }
        showDefaultSystemErrorMessage(title, finalMessage, messageLevel);
    }

    private void showDefaultSystemErrorMessage(String title, String message, int messageLevel) {
        notificationEditorPane.setText(message);

        final URL imageUrl;

        switch (messageLevel) {
            case JOptionPane.PLAIN_MESSAGE:
            case JOptionPane.INFORMATION_MESSAGE:
                imageUrl = getClass().getResource("/images/notificationIcons/info48.png");
                break;
            case JOptionPane.ERROR_MESSAGE:
                imageUrl = getClass().getResource("/images/notificationIcons/warning48.png");
                break;
            default:
                imageUrl = getClass().getResource("/images/notificationIcons/error48.png");
                break;
        }

        final Image image = Toolkit.getDefaultToolkit().getImage(imageUrl);
        setIconImage(image);
        iconLabel.setIcon(new ImageIcon(image));

        if (title == null) {
            title = "";
        }
        setTitle(title);


        buttonOK.requestFocus();

        setVisible(true);
    }

    @Override
    public void showInfoNotification(String title, String message) {
        showMessage(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showWarningNotification(String title, String message) {
        FrameUtils.shakeFrame(component);
        showMessage(title, message, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showErrorNotification(String title, String message) {
        FrameUtils.shakeFrame(component);
        showMessage(title, message, JOptionPane.ERROR_MESSAGE);
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
        contentPane.setLayout(new GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        this.$$$loadButtonText$$$(buttonOK, ResourceBundle.getBundle("translations/CommonsBundle").getString("okButton"));
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyMessage = new JButton();
        this.$$$loadButtonText$$$(copyMessage, ResourceBundle.getBundle("translations/CommonsBundle").getString("copyMessage"));
        panel1.add(copyMessage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        notificationEditorPane = new JEditorPane();
        notificationEditorPane.setContentType("text/html");
        notificationEditorPane.setEditable(false);
        scrollPane1.setViewportView(notificationEditorPane);
        iconLabel = new JLabel();
        iconLabel.setAlignmentX(0.0f);
        iconLabel.setIcon(new ImageIcon(getClass().getResource("/images/notificationIcons/error48.png")));
        iconLabel.setText("");
        contentPane.add(iconLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(48, 48), new Dimension(48, 48), new Dimension(48, 48), 0, false));
        final Spacer spacer2 = new Spacer();
        contentPane.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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

}
