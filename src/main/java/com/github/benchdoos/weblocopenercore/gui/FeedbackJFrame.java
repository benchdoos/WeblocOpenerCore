package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.gui.panels.BufferedImagePanel;
import com.github.benchdoos.weblocopenercore.service.feedback.FileExtension;
import com.github.benchdoos.weblocopenercore.utils.FileUtils;
import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TooManyListenersException;

@Log4j2
public class FeedbackJFrame extends JFrame implements Translatable {
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

        initDropTarget();

        initImagesList();

        getRootPane().setDefaultButton(sendButton);

        imagesPanel.setVisible(false);

        pack();
        setMinimumSize(getSize());
    }

    private void initImagesList() {
        imagesList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return (Component) value;
            }
        });
        imagesList.setModel(new DefaultListModel<>());
    }

    private void initDropTarget() {
        final DropTarget dropTarget = new DropTarget(){
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
                        final BufferedImagePanel panel = new BufferedImagePanel(ImageIO.read(file));
                        ((DefaultListModel) imagesList.getModel()).addElement(panel);
                    }

                    if (imagesList.getModel().getSize() > 0) {
                        imagesPanel.setVisible(true);
                    }

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

    private void initListeners() {
        sendButton.addActionListener(e -> sendFeedback());
        buttonCancel.addActionListener(e-> onCancel());
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
}
