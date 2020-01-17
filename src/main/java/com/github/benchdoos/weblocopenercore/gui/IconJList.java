package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.gui.panels.Named;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.Component;

public class IconJList<E> extends JList<E> {

    private boolean minimalMode;

    public IconJList() {
        setCellRenderer(new ImageListCellRenderer());
    }

    public void setMinimalMode(boolean minimalMode) {
        this.minimalMode = minimalMode;
        updateUI();
    }

    /**
     * A FileListCellRenderer for a File.
     */
    private class ImageListCellRenderer extends DefaultListCellRenderer {

        private static final long serialVersionUID = -7799441088157759804L;
        private final JList list = new JList();
        private FileSystemView fileSystemView;
        private JLabel label;
        private Color selectionForeground = list.getSelectionForeground();
        private Color selectionBackground = list.getSelectionBackground();
        private Color foreground = list.getForeground();
        private Color background = list.getBackground();

        ImageListCellRenderer() {
            label = new JLabel();
            label.setOpaque(true);
            fileSystemView = FileSystemView.getFileSystemView();
        }

        @Override
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean selected,
                boolean expanded) {

            final IconObject<Named> iconObject = (IconObject<Named>) value;
            label.setIcon(iconObject.getIcon());
            label.setToolTipText(iconObject.getObject().getName());

            if (selected) {
                label.setBackground(selectionBackground);
                label.setForeground(selectionForeground);
            } else {
                label.setBackground(background);
                label.setForeground(foreground);
            }

            if (minimalMode) {
                label.setText("");
            } else {
                label.setText(iconObject.getObject().getName());
            }

            return label;
        }
    }

    @AllArgsConstructor
    @Data
    public static class IconObject<O extends Named> {
        private Icon icon;
        private O object;
    }


}
