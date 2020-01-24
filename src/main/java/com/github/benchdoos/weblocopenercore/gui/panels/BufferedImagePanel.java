package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.weblocopenercore.gui.ImagePanel;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

@Log4j2
public class BufferedImagePanel extends JPanel {
    final Dimension BUFFERED_PANEL_SIZE = new Dimension(55, 55); // size of panel

    private JPanel contentPane;
    private JButton removeButton;
    private JPanel imagePanel;
    private Image image;
    private Image scaledImage;
    private JList<BufferedImagePanel> parentImageList;

    public BufferedImagePanel(Image image) {
        this.image = image;
        try {
            this.scaledImage = Thumbnails.of(CoreUtils.toBufferedImage(image)).size(BUFFERED_PANEL_SIZE.width, BUFFERED_PANEL_SIZE.height).asBufferedImage();
        } catch (IOException e) {
            log.warn("Could not scale image");
        }
        $$$setupUI$$$();
        initGui();
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

    }

    public Image getImage() {
        return image;
    }

    public void setParentImageList(JList<BufferedImagePanel> imageList) {
        this.parentImageList = imageList;
    }

    private void createUIComponents() {
        imagePanel = new ImagePanel(scaledImage);
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
        imagePanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(imagePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(55, 55), new Dimension(55, 55), new Dimension(55, 55), 0, false));
        removeButton = new JButton();
        removeButton.setBorderPainted(false);
        removeButton.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        removeButton.setIconTextGap(0);
        removeButton.setOpaque(false);
        imagePanel.add(removeButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        final Spacer spacer1 = new Spacer();
        imagePanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        imagePanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
