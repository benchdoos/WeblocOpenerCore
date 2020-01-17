package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.weblocopenercore.gui.panels.Named;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

public class IconJList<E> extends JList<E> {

    private boolean minimalMode;
    private final int minimalModeSquare;
    private final int normalModeSquare;

    public IconJList(int normalModeSquare, int minimalModeSquare) {
        this.normalModeSquare = normalModeSquare;
        this.minimalModeSquare = minimalModeSquare;
        setCellRenderer(new ImageListCellRenderer());
    }

    public void setMinimalMode(boolean minimalMode) {
        this.minimalMode = minimalMode;
        updateUI();
    }

    @AllArgsConstructor
    @Data
    public static class IconObject<O extends Named> {
        private Image selectedIcon;
        private Image unselectedIcon;
        private O object;
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

            label.setToolTipText(iconObject.getObject().getName());

            if (selected) {
                label.setBackground(selectionBackground);
                label.setForeground(selectionForeground);
                updateIcon(iconObject.getSelectedIcon());
            } else {
                label.setBackground(background);
                label.setForeground(foreground);
                updateIcon(iconObject.getUnselectedIcon());
            }

            if (minimalMode) {
                label.setText("");
            } else {
                label.setText(iconObject.getObject().getName());
            }

            return label;
        }

        private void updateIcon(Image icon) {
            if (icon != null) {
                if (!minimalMode) {
                    if (icon.getHeight(null) != normalModeSquare && icon.getWidth(null) != normalModeSquare) {
                        label.setIcon(new ImageIcon(CoreUtils.resize(icon, normalModeSquare, normalModeSquare)));
                    } else {
                        label.setIcon(new ImageIcon(icon));
                    }
                } else {
                    if (icon.getHeight(null) != minimalModeSquare && icon.getWidth(null) != minimalModeSquare) {
                        label.setIcon(new ImageIcon(CoreUtils.resize(icon, minimalModeSquare, minimalModeSquare)));
                    } else {
                        label.setIcon(new ImageIcon(icon));
                    }
                }
            } else {
                label.setIcon(null);
            }
        }
    }


}
