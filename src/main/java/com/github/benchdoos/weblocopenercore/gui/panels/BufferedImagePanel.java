package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.weblocopenercore.gui.ImagePanel;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;

public class BufferedImagePanel extends JPanel {
    private JPanel contentPane;
    private JButton removeButton;
    private JPanel imagePanel;
    private Image image;
    private JList<ImagePanel> parentImageList;

    public BufferedImagePanel(Image image) {
        this.image = image;
        setLayout(new GridLayout());
        add(contentPane);
    }

    private void createUIComponents() {
        imagePanel = new ImagePanel(image);
    }

    public void setParentImageList(JList<ImagePanel> imageList) {
        this.parentImageList = imageList;
    }

    private void remove() {
        if (parentImageList != null) {
            parentImageList.remove(this);
        }
    }
}
