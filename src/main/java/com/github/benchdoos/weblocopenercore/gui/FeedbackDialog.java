package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.PathConstants;
import com.github.benchdoos.weblocopenercore.gui.panels.BufferedImagePanel;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.WindowLauncher;
import com.github.benchdoos.weblocopenercore.service.feedback.Feedback;
import com.github.benchdoos.weblocopenercore.service.feedback.FeedbackService;
import com.github.benchdoos.weblocopenercore.service.feedback.FileExtension;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.ImagesUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpStatus;
import org.jsoup.internal.StringUtil;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class FeedbackDialog extends JFrame implements Translatable {
    private static final Dimension SCALED_IMAGE_SIZE = new Dimension(640, 480);
    private static final int MAXIMUM_TEXT_LENGTH = 30000;
    private static final int MAXIMUM_IMAGES_COUNT = 5;
    private static final int MAXIMUM_IMAGE_SIZE = 15_000_000; //15 megabytes
    private JPanel contentPane;
    private JButton sendButton;
    private JButton buttonCancel;
    private JTextArea feedbackTextArea;
    private JPanel imagesPanel;
    private JList<BufferedImagePanel> imagesList;
    private JTextField emailTextField;
    private JButton previewItemButton;
    private JButton removeItemButton;
    private JCheckBox appendLogsCheckBox;
    private JProgressBar sendingProgressBar;
    private JLabel appengLogsInfoLabel;
    private static final List<FileExtension> SUPPORTED_IMAGES_EXTENSIONS = Arrays.asList(FileExtension.JPG, FileExtension.PNG);
    private Thread sendFeedbackThread;

    public FeedbackDialog() {
        $$$setupUI$$$();
        initGui();
    }

    private void initGui() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(sendButton);

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/feedback256.png")));

        initListeners();

        initTextArea();

        initDropTarget();

        initImagePaste();

        initImagesList();

        imagesPanel.setVisible(false);
        sendingProgressBar.setVisible(false);

        translate();

        pack();
        setMinimumSize(getSize());
    }

    public void setFeedbackText(String text) {
        feedbackTextArea.setText(text);
    }

    private void initImagePaste() {
        contentPane.registerKeyboardAction(e -> onPaste(),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_FOCUSED);
        feedbackTextArea.registerKeyboardAction(e -> onPaste(),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void onPaste() {
        try {
            log.debug("Pasting image from clipboard");
            final Image image = CoreUtils.getImageFromClipboard();
            if (image != null) {
                onImagePaste(image);
            } else {
                final String textFromClipboard = CoreUtils.getTextFromClipboard();
                if (textFromClipboard != null) {
                    if (feedbackTextArea.getSelectionStart() == feedbackTextArea.getSelectionEnd()) {
                        feedbackTextArea.insert(textFromClipboard, feedbackTextArea.getSelectionEnd());
                    } else {
                        feedbackTextArea.replaceRange(
                                textFromClipboard,
                                feedbackTextArea.getSelectionStart(),
                                feedbackTextArea.getSelectionEnd());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Could not paste image from clipboard", e);
        }
    }

    private void onImagePaste(Image image) {
        if (imagesList.getModel().getSize() < MAXIMUM_IMAGES_COUNT) {
            final BufferedImage bufferedImage = CoreUtils.toBufferedImage(image);
            addImageToList(bufferedImage);
            updatePanel();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Can not add more images than " + MAXIMUM_IMAGES_COUNT, //todo translate
                    "Can not add image",//todo translate
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void initTextArea() {
        feedbackTextArea.getActionMap().put("paste-from-clipboard", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPaste();
            }
        });
    }

    private void initImagesList() {
        imagesList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        imagesList.setVisibleRowCount(-1);
        imagesList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        imagesList.setCellRenderer((jList, bufferedImagePanel, index, isSelected, cellHasFocus) -> {
            if (isSelected) {
                bufferedImagePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
            } else {
                bufferedImagePanel.setBorder(BorderFactory.createEmptyBorder());
            }
            return bufferedImagePanel;
        });
        imagesList.setModel(new DefaultListModel<>());
    }

    private void openImage() {
        try {
            if (imagesList.getSelectedValue() != null) {

                final BufferedImage bufferedImage = CoreUtils.toBufferedImage(imagesList.getSelectedValue().getImage());

                final BufferedImage scaled;

                if (bufferedImage.getWidth() > SCALED_IMAGE_SIZE.width ||
                        bufferedImage.getHeight() > SCALED_IMAGE_SIZE.height) {
                    scaled = Thumbnails.of(bufferedImage)
                            .size(640, 480)
                            .asBufferedImage();
                } else {
                    scaled = bufferedImage;
                }

                showImagePreview(scaled, new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()));
            }
        } catch (Exception e) {
            log.warn("Could not scale image", e);
        }
    }

    /**
     * Shows scaled image to user
     *
     * @param scaled image to show
     * @param dimension dimension of original image
     */
    private void showImagePreview(BufferedImage scaled, Dimension dimension) {
        final JDialog dialog = new WindowLauncher<JDialog>() {
            @Override
            public JDialog initWindow() {
                final JDialog dialog = new JDialog();
                dialog.setTitle(String.format("Screenshot (%sX%s)", dimension.getWidth(), dimension.getHeight()));//todo translate
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setLayout(new BorderLayout());
                dialog.add(new JLabel(new ImageIcon(scaled)));
                dialog.setModal(true);
                dialog.pack();
                dialog.setResizable(false);
                return dialog;
            }
        }.initWindow();

        FrameUtils.setWindowOnScreenCenter(dialog);

        dialog.setVisible(true);
    }

    private void initDropTarget() {
        final DropTarget dropTarget = new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {

                try {
                    contentPane.setBorder(null);

                    evt.acceptDrop(DnDConstants.ACTION_COPY);

                    final Object transferData = evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    final List<?> list = (List<?>) transferData;
                    final ArrayList<File> files = new ArrayList<>(list.size());

                    for (final Object object : list) {
                        if (object instanceof File) {
                            final File file = (File) object;
                            final String fileExtension = FileUtils.getFileExtension(file);
                            if (isSupportedImageExtension(fileExtension)) {
                                files.add(file);
                            }
                        }
                    }

                    for (File file : files) {
                        final BufferedImage read = ImageIO.read(file);
                        addImageToList(read);
                    }

                    updatePanel();

                } catch (final UnsupportedFlavorException | IOException e) {
                    log.warn("Could not append files to drop target", e);
                }
            }
        };

        FrameUtils.appendRedBorderToComponent(dropTarget, contentPane);

        imagesPanel.setDropTarget(dropTarget);
        contentPane.setDropTarget(dropTarget);
        feedbackTextArea.setDropTarget(dropTarget);

    }

    private void updatePanel() {
        if (imagesList.getModel().getSize() > 0) {
            imagesPanel.setVisible(true);
        }
    }

    private void addImageToList(BufferedImage read) {
        final BufferedImagePanel panel = new BufferedImagePanel(read);
        panel.setParentImageList(imagesList);
        ((DefaultListModel<BufferedImagePanel>) imagesList.getModel()).addElement(panel);
    }

    private void initListeners() {

        previewItemButton.addActionListener(e -> openImage());
        removeItemButton.addActionListener(e -> onRemove());

        sendButton.addActionListener(e -> sendFeedback());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onRemove() {
        if (imagesList.getSelectedValue() != null) {
            for (BufferedImagePanel panel : imagesList.getSelectedValuesList()) {
                ((DefaultListModel) imagesList.getModel()).removeElement(panel);
            }
        }
        if (imagesList.getModel().getSize() == 0) {
            imagesPanel.setVisible(false);
        }
    }

    private void onCancel() {
        if (sendFeedbackThread != null) {
            if (sendFeedbackThread.isAlive()) {
                sendFeedbackThread.interrupt();
            }
        }
        dispose();
    }

    private void sendFeedback() {
        try {
            switchInputsEnabled(false);
            validateInput();
            shareFeedback();
        } catch (final IllegalArgumentException e) {
            switchInputsEnabled(true);
            log.warn("Could not validate input", e);
            FrameUtils.shakeFrame(this);
        }
    }

    private Feedback prepareFeedback() {

        final Feedback dto = Feedback.builder()
                .uuid(PreferencesManager.getApplicationUuid())
                .feedback(feedbackTextArea.getText())
                .build();

        if (!StringUtil.isBlank(emailTextField.getText())) {
            dto.setEmail(emailTextField.getText());
        }

        if (appendLogsCheckBox.isSelected()) {
            final String traceLog = PathConstants.APP_LOG_FOLDER_PATH + File.separator + "trace.log";
            final String contentFromResource = CoreUtils.getContentFromResource(traceLog);
            if (!StringUtil.isBlank(contentFromResource)) {
                dto.setLogFileContent(contentFromResource);
            } else {
                log.warn("Could not append trace.log file (filepath: {}). Skipping.", traceLog);
            }
        }

        if (imagesList.getModel().getSize() > 0) {
            final List<Image> list = new ArrayList<>();
            for (int i = 0; i < imagesList.getModel().getSize(); i++) {
                final BufferedImagePanel panel = imagesList.getModel().getElementAt(i);
                list.add(panel.getImage());
            }
            dto.setImages(list);
        }

        return dto;
    }

    private void shareFeedback() {
        sendFeedbackThread = new Thread(() -> {
            final Feedback feedback = prepareFeedback();

            final int code;
            try {
                code = new FeedbackService().sendFeedback(feedback);
                if (HttpStatus.SC_OK == code) {
                    log.info("Feedback was sent successfully");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Feedback was not sent", //todo translate this
                            "Error", //todo translate this
                            JOptionPane.WARNING_MESSAGE);
                    switchInputsEnabled(true);
                }
            } catch (IOException e) {
                log.warn("Could not send feedback", e);
            }

        });
        sendFeedbackThread.start();
    }

    private void switchInputsEnabled(boolean enabled) {
        feedbackTextArea.setEditable(enabled);
        emailTextField.setEditable(enabled);
        appendLogsCheckBox.setEnabled(enabled);
        previewItemButton.setEnabled(enabled);
        removeItemButton.setEnabled(enabled);
        imagesList.setEnabled(enabled);
        sendingProgressBar.setVisible(!enabled);
        sendButton.setEnabled(enabled);
    }

    private void validateInput() {
        log.info("Validating all fields");
        validateTextArea();
        validateEmail();
        validateImages();
        log.info("All fields are valid, continuing");
    }

    private void validateImages() {
        for (int i = 0; i < imagesList.getModel().getSize(); i++) {
            final BufferedImagePanel panel = imagesList.getModel().getElementAt(i);
            final Image image = panel.getImage();
            final int imageWeight = ImagesUtils.getImageWeight(CoreUtils.toBufferedImage(image));
            if (imageWeight > MAXIMUM_IMAGE_SIZE) {
                JOptionPane.showMessageDialog(this,
                        String.format("Image %s is bigger than 15MB", (i + 1)), //todo add translation
                        "Image size is not valid", //todo add translation
                        JOptionPane.WARNING_MESSAGE
                );
                throw new IllegalArgumentException("Image weight is bigger that maximum value (" + MAXIMUM_IMAGE_SIZE + ") on index: " + i);
            }
        }
    }

    private void validateTextArea() {
        final String text = feedbackTextArea.getText();
        if (StringUtil.isBlank(text)) {
            feedbackTextArea.setBorder(BorderFactory.createLineBorder(Color.RED));
            throw new IllegalArgumentException("Feedback text area can not be blank");
        } else {
            if (text.length() > MAXIMUM_TEXT_LENGTH) {
                feedbackTextArea.setBorder(BorderFactory.createLineBorder(Color.RED));
                throw new IllegalArgumentException("Feedback text area length (" + text.length() + ") " +
                        "can not be bigger than maximum text length (" + MAXIMUM_TEXT_LENGTH + ")");
            } else {
                feedbackTextArea.setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }

    private void validateEmail() {
        final String email = emailTextField.getText();
        if (!StringUtil.isBlank(email)) {
            log.debug("Validating email: {}", email);
            final boolean emailValid = EmailValidator.getInstance().isValid(email);
            if (!emailValid) {
                emailTextField.setBorder(BorderFactory.createLineBorder(Color.RED));
                throw new IllegalArgumentException("Email is not valid: " + email);
            } else {
                emailTextField.setBorder(new JTextField().getBorder());
            }
        }
    }

    @Override
    public void translate() {
        final Translation translation = new Translation("FeedbackDialogBundle");
        setTitle(translation.getTranslatedString("windowTitle"));
        appendLogsCheckBox.setText(translation.getTranslatedString("appendLogsCheckBox"));
    }

    private static boolean isSupportedImageExtension(String extension) {
        return SUPPORTED_IMAGES_EXTENSIONS.stream()
                .anyMatch(e -> e.getExtensions().contains(extension.toLowerCase()));
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
        contentPane.setLayout(new GridLayoutManager(6, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sendButton = new JButton();
        sendButton.setText("Send");
        panel2.add(sendButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setVisible(true);
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 110), new Dimension(300, 180), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        feedbackTextArea = new JTextArea();
        Font feedbackTextAreaFont = this.$$$getFont$$$("Arial", -1, 14, feedbackTextArea.getFont());
        if (feedbackTextAreaFont != null) feedbackTextArea.setFont(feedbackTextAreaFont);
        scrollPane1.setViewportView(feedbackTextArea);
        imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(imagesPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        imagesPanel.add(scrollPane2, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 60), new Dimension(-1, 60), new Dimension(-1, 60), 0, false));
        imagesList = new JList();
        imagesList.setLayoutOrientation(2);
        scrollPane2.setViewportView(imagesList);
        previewItemButton = new JButton();
        previewItemButton.setBorderPainted(false);
        previewItemButton.setIcon(new ImageIcon(getClass().getResource("/images/icons/imagePreview16.png")));
        previewItemButton.setOpaque(false);
        imagesPanel.add(previewItemButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, 25), new Dimension(25, 25), new Dimension(25, 25), 0, false));
        removeItemButton = new JButton();
        removeItemButton.setBorderPainted(false);
        removeItemButton.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        removeItemButton.setIconTextGap(0);
        removeItemButton.setOpaque(false);
        imagesPanel.add(removeItemButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(25, 25), new Dimension(25, 25), new Dimension(25, 25), 0, false));
        final Spacer spacer2 = new Spacer();
        imagesPanel.add(spacer2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentPane.add(emailTextField, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        appendLogsCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(appendLogsCheckBox, ResourceBundle.getBundle("translations/FeedbackDialogBundle").getString("appendLogsCheckBox"));
        contentPane.add(appendLogsCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        appengLogsInfoLabel = new JLabel();
        appengLogsInfoLabel.setIcon(new ImageIcon(getClass().getResource("/images/infoIcon16.png")));
        appengLogsInfoLabel.setIconTextGap(0);
        appengLogsInfoLabel.setText("");
        appengLogsInfoLabel.setToolTipText(ResourceBundle.getBundle("translations/FeedbackDialogBundle").getString("appendLogsInfoLabel"));
        contentPane.add(appengLogsInfoLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sendingProgressBar = new JProgressBar();
        sendingProgressBar.setIndeterminate(true);
        contentPane.add(sendingProgressBar, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        emailTextField = new PlaceholderTextField();
        ((PlaceholderTextField) emailTextField).setPlaceholder("e-mail");
    }
}
