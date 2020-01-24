package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.gui.panels.BufferedImagePanel;
import com.github.benchdoos.weblocopenercore.service.feedback.FileExtension;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;

@Log4j2
public class FeedbackJFrame extends JFrame implements Translatable {
    final Dimension BUFFERED_PANEL_SIZE = new Dimension(55, 55); // size of panel

    private JPanel contentPane;
    private JButton sendButton;
    private JButton buttonCancel;
    private JTextArea feedBackTextArea;
    private JPanel imagesPanel;
    private JList<BufferedImagePanel> imagesList;
    private static final List<FileExtension> SUPPORTED_IMAGES_EXTENSIONS = Arrays.asList(FileExtension.JPG, FileExtension.PNG);

    public FeedbackJFrame() {
        initGui();
    }

    private void initGui() {
        setContentPane(contentPane);

        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/feedback256.png")));

        initListeners();

        initTextArea();

        initDropTarget();

        initImagePaste();

        initImagesList();

        getRootPane().setDefaultButton(sendButton);

        imagesPanel.setVisible(false);

        pack();
        setMinimumSize(getSize());
    }

    private void initImagePaste() {
        contentPane.registerKeyboardAction(e -> onPaste(),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_FOCUSED);
        feedBackTextArea.registerKeyboardAction(e -> onPaste(),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void onPaste() {
        try {
            log.debug("Pasting image from clipboard");
            final Image image = CoreUtils.getImageFromClipboard();
            if (image != null) {
                final BufferedImage bufferedImage = CoreUtils.toBufferedImage(image);
                addImageToList(bufferedImage);
                updatePanel();
            }
        } catch (Exception e) {
            log.warn("Could not paste image from clipboard", e);
        }
    }

    private void initTextArea() {
        feedBackTextArea.getActionMap().put("paste-from-clipboard", new AbstractAction() {
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
        imagesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return (Component) value;
            }
        });
        imagesList.setModel(new DefaultListModel<>());
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
                            if (contains(fileExtension)) {
                                files.add(file);
                            }
                        }
                    }

                    for (File file : files) {
                        final BufferedImage read = ImageIO.read(file);
                        addImageToList(read);
                    }

                    updatePanel();

                } catch (UnsupportedFlavorException | IOException e) {
                    log.warn("Could not append files to drop target", e);
                }
            }
        };

        try {
            dropTarget.addDropTargetListener(new DropTargetAdapter() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    contentPane.setBorder(BorderFactory.createLineBorder(Color.RED));
                    super.dragEnter(dtde);
                }

                @Override
                public void dragExit(DropTargetEvent dte) {
                    contentPane.setBorder(null);
                    super.dragExit(dte);
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    contentPane.setBorder(null);
                }
            });
        } catch (TooManyListenersException e) {
            log.warn("Can not init drag and drop dropTarget", e);
        }

        imagesPanel.setDropTarget(dropTarget);
        contentPane.setDropTarget(dropTarget);
        feedBackTextArea.setDropTarget(dropTarget);

    }

    private void updatePanel() {
        if (imagesList.getModel().getSize() > 0) {
            imagesPanel.setVisible(true);
        }
    }

    private void addImageToList(BufferedImage read) {
        final BufferedImagePanel panel = new BufferedImagePanel(scaleImageToSize(read, BUFFERED_PANEL_SIZE));
        ((DefaultListModel) imagesList.getModel()).addElement(panel);
    }

    /**
     * Primitive image scale
     *
     * @param bufferedImage image to scale
     * @param size size to scale image into it
     * @return scaled image
     */
    private Image scaleImageToSize(BufferedImage bufferedImage, Dimension size) {
        final int width = bufferedImage.getWidth(null);
        final int height = bufferedImage.getHeight(null);

        final double scale;

        if (width >= height) {
            //will check by height
            if (height >= size.getHeight()) {
                scale = size.getHeight() / height;
            } else {
                scale = height / size.getHeight();
            }
        } else {
            //will check by
            if (width >= size.getWidth()) {
                scale = size.getWidth() / width;
            } else {
                scale = width / size.getWidth();
            }
        }

        final double scaledWidth = width * scale;
        final double scaledHeight = height * scale;

        return CoreUtils.resize(bufferedImage, (int) scaledWidth, (int) scaledHeight);
    }

    private void initListeners() {
        sendButton.addActionListener(e -> sendFeedback());
        buttonCancel.addActionListener(e -> onCancel());
    }

    private void onCancel() {
        dispose();
    }

    private void sendFeedback() {
        imagesPanel.setVisible(!imagesPanel.isVisible());
        //todo send feedback
    }

    @Override
    public void translate() {
        //todo add translation
    }

    private static boolean contains(String extension) {
        return Arrays.stream(FileExtension.values())
                .anyMatch(e -> e.getExtensions().contains(extension.toLowerCase()));
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
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
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
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(300, 110), new Dimension(300, 180), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        feedBackTextArea = new JTextArea();
        Font feedBackTextAreaFont = this.$$$getFont$$$("Arial", -1, 14, feedBackTextArea.getFont());
        if (feedBackTextAreaFont != null) feedBackTextArea.setFont(feedBackTextAreaFont);
        scrollPane1.setViewportView(feedBackTextArea);
        imagesPanel = new JPanel();
        imagesPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(imagesPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 60), new Dimension(-1, 60), new Dimension(-1, 60), 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        imagesPanel.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        imagesList = new JList();
        imagesList.setLayoutOrientation(2);
        scrollPane2.setViewportView(imagesList);
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
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
