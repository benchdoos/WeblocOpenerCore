package com.github.benchdoos.weblocopenercore.utils;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.Graphics;
import java.awt.Image;

public class GuiUtils {

    public static void appendToSplitPaneDividerIcon(JSplitPane splitPane) {
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }

                    @Override
                    public void paint(Graphics g) {
                        final Image image = new ImageIcon(getClass().getResource("/images/divider.png")).getImage();
                        final int imageXPosition = getSize().width / 2 - image.getWidth(null);
                        final int imageYPosition = getSize().height / 2 - image.getHeight(null);
                        g.drawImage(image, imageXPosition, imageYPosition, null);
                        super.paint(g);
                    }
                };
            }
        });
    }

}
