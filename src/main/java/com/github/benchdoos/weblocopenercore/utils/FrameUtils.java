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

package com.github.benchdoos.weblocopenercore.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.Logger;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

/**
 * Created by Eugene Zrazhevsky on 30.10.2016.
 */
@Log4j2
public class FrameUtils {
    private static final Timer timer = new Timer(60, null);

    /**
     * Sets window on center of the screen on default screen graphics device
     *
     * @param window to set on center
     * @see java.awt.GraphicsDevice
     */
    public static void setWindowOnScreenCenter(Window window) {
        if (window != null) {

            final GraphicsDevice defaultScreenDevice = GraphicsEnvironment.
                    getLocalGraphicsEnvironment().
                    getDefaultScreenDevice();

            final GraphicsConfiguration defaultConfiguration = defaultScreenDevice.getDefaultConfiguration();

            final Rectangle screenBounds = defaultConfiguration.getBounds();

            final Dimension screenSize = screenBounds.getSize();
            final Point initialScreenLocationPoint = screenBounds.getLocation();

            final Dimension windowSize = window.getSize();

            final double centerOfScreenWidth = (screenSize.getWidth()) / (double) 2;
            final double centerOfScreenHeight = (screenSize.getHeight()) / (double) 2;

            int width = (int) (initialScreenLocationPoint.getX() + centerOfScreenWidth - (windowSize.getWidth() / (double) 2));
            int height = (int) (initialScreenLocationPoint.getY() + centerOfScreenHeight - (windowSize.getHeight() / (double) 2));

            final Point point = new Point(width, height);

            window.setLocation(point);
        }
    }

    /**
     * Sets window on center of parent window
     *
     * @param parent parent window
     * @param window that should be in center of the parent window
     */
    public static void setWindowOnParentWindowCenter(Window parent, Window window) {
        if (parent != null) {
            if (window != null) {
                final Dimension windowSize = window.getSize();

                final double centerOfParentWidth = parent.getLocationOnScreen().getX() + (parent.getWidth()) / (double) 2;
                final double centerOfParentHeight = parent.getLocationOnScreen().getY() + (parent.getHeight()) / (double) 2;

                int width = (int) (centerOfParentWidth - (windowSize.getWidth() / (double) 2));
                int height = (int) (centerOfParentHeight - (windowSize.getHeight() / (double) 2));

                final Point point = new Point(width, height);

                window.setLocation(point);
            }
        } else {
            setWindowOnScreenCenter(window);
        }
    }

    /**
     * Finds window on component given.
     *
     * @param component Component where window is located.
     * @return Window that is searched.
     **/
    public static Window findWindow(Component component) {
        if (component == null) {
            return JOptionPane.getRootFrame();
        } else if (component instanceof Window) {
            return (Window) component;
        } else {
            return findWindow(component.getParent());
        }
    }

    /**
     * Finds window on component given.
     *
     * @param component Component where window is located.
     * @return Window that is searched.
     **/
    public static Dialog findDialog(Component component) {
        if (component == null) {
            return null;
        } else if (component instanceof Dialog) {
            return (Dialog) component;
        } else {
            return findDialog(component.getParent());
        }
    }

    /**
     * Shakes window like in MacOS.
     *
     * @param component Component to shake
     */
    public static void shakeFrame(final Component component) {
        final Window window = findWindow(component);

        if (!timer.isRunning()) {
            timer.addActionListener(new ActionListener() {
                final static int maxCounter = 6;
                Point location = window.getLocation();
                int counter = 0;
                int step = 14;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (counter <= 2) {
                        step = 14;
                    } else if (counter <= 4) {
                        step = 7;
                    } else if (counter <= 6) {
                        step = 3;
                    }

                    if (counter >= 0) {
                        if (counter <= maxCounter) {
                            counter++;

                            if (location.x < 0 || location.y < 0) {
                                FrameUtils.setWindowOnScreenCenter(window);
                                location = window.getLocation();
                            }
                            if (counter % 2 != 0) {
                                Point newLocation = new Point(location.x + step, location.y);
                                window.setLocation(newLocation);
                            } else {
                                Point newLocation = new Point(location.x - step, location.y);
                                window.setLocation(newLocation);
                            }
                        } else {
                            Point newLocation = new Point(location.x, location.y);
                            window.setLocation(newLocation);

                            counter = 0;
                            timer.removeActionListener(timer.getActionListeners()[0]);
                            timer.stop();
                        }
                    }
                }
            });
            timer.start();
        }
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Brings windows to front. If some application is already in use, window is shown on top without focus.
     * If user requests switch focus to window and goes back, window will hide.
     *
     * @param window Frame to make on the front of the screen.
     */
    public static void showOnTop(Window window) {
        EventQueue.invokeLater(() -> {
            window.setAlwaysOnTop(true);
            window.toFront();
            window.repaint();
            window.setAlwaysOnTop(false);
        });
    }

    public static KeyAdapter getSmartKeyAdapter(Component component) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (component != null) {
                        component.dispatchEvent(e);
                    }
                }
            }
        };
    }

    /**
     * Appends to DropTarget {@link DropTargetListener} that adds border to given panel
     *
     * @param dropTarget target
     * @param panel panel to add red border
     */
    public static void appendRedBorderToComponent(DropTarget dropTarget, JPanel panel) {
        try {
            dropTarget.addDropTargetListener(new DropTargetAdapter() {
                @Override
                public void dragEnter(DropTargetDragEvent dtde) {
                    panel.setBorder(BorderFactory.createLineBorder(Color.RED));
                    super.dragEnter(dtde);
                }

                @Override
                public void dragExit(DropTargetEvent dte) {
                    panel.setBorder(null);
                    super.dragExit(dte);
                }

                @Override
                public void drop(DropTargetDropEvent dtde) {
                    panel.setBorder(null);
                }
            });
        } catch (TooManyListenersException e) {
            log.warn("Can not init drag and drop dropTarget", e);
        }
    }

    public static void fillTextFieldWithClipboard(JTextField textField) {
        final String data;
        try {
            data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
            try {
                final URL url = new URL(data);
                final UrlValidator urlValidator = new UrlValidator();
                if (urlValidator.isValid(data)) {
                    textField.setText(url.toString());
                    setTextFieldFont(textField, textField.getFont(), TextAttribute.UNDERLINE_ON);
                    textField.setCaretPosition(textField.getText().length());
                    textField.selectAll();
                    log.debug("Got URL from clipboard: " + url);
                }
            } catch (IllegalStateException | HeadlessException e) {
                textField.setText("");
                log.warn("Can not read URL from clipboard: {}", data, e);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            log.warn("Can not read URL from clipboard", e);
        }
    }

    public static void setTextFieldFont(JTextField textField, Font font, int attribute2) {
        final Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        fontAttributes.put(TextAttribute.UNDERLINE, attribute2);
        textField.setFont(font.deriveFont(fontAttributes));
    }

    public static String getFilePathFromFileDialog(FileDialog fileDialog, Logger log) {
        fileDialog.setVisible(true);
        final File[] f = fileDialog.getFiles();
        if (f.length > 0) {
            final String absolutePath = fileDialog.getFiles()[0].getAbsolutePath();
            log.debug("Choice: " + absolutePath);
            fileDialog.dispose();
            return absolutePath;
        } else {
            log.debug("Choice canceled");
            fileDialog.dispose();
            return null;
        }
    }
}
