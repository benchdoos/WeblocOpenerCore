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

package com.github.benchdoos.weblocopenercore.gui.panels;

import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.core.constants.SettingsConstants;
import com.github.benchdoos.weblocopenercore.gui.PlaceholderTextField;
import com.github.benchdoos.weblocopenercore.gui.Translatable;
import com.github.benchdoos.weblocopenercore.gui.panels.simpleTimePickerListeners.ValueChangeListener;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.DarkModeValue;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.Location;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.LocationManager;
import com.github.benchdoos.weblocopenercore.service.gui.darkMode.SimpleTime;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.extern.log4j.Log4j2;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

@Log4j2
public class AppearanceSetterPanel extends JPanel implements SettingsPanel, Translatable {


    private JPanel contentPane;
    private JPanel byLocationPanel;
    private JComboBox<Location> locationComboBox;
    private JRadioButton disabledDarkModeRadioButton;
    private JRadioButton alwaysDarkModeRadioButton;
    private JRadioButton byTimeDarkModeRadioButton;
    private JRadioButton byLocationDarkModeRadioButton;
    private JPanel byTimePanel;
    private JTextField locationTextField;
    private JLabel locationStatusLabel;
    private JLabel timeStatusLabel;
    private SimpleTimePicker beginningTimePicker;
    private SimpleTimePicker endingTimePicker;
    private JLabel darkModeLabel;

    public AppearanceSetterPanel() {
        $$$setupUI$$$();
        initGui();
    }

    @Override
    public String getName() {
        return Translation.getTranslatedString("SettingsDialogBundle", "settingsAppearancePanelName");
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
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        darkModeLabel = new JLabel();
        this.$$$loadLabelText$$$(darkModeLabel, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("darkMode"));
        panel2.add(darkModeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        byLocationPanel = new JPanel();
        byLocationPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(byLocationPanel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        byLocationPanel.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        byLocationPanel.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        locationStatusLabel = new JLabel();
        locationStatusLabel.setHorizontalAlignment(2);
        locationStatusLabel.setHorizontalTextPosition(2);
        locationStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        panel3.add(locationStatusLabel, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        panel3.add(locationTextField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(420, -1), new Dimension(420, -1), new Dimension(420, -1), 0, false));
        locationComboBox = new JComboBox();
        locationComboBox.setMaximumRowCount(10);
        panel3.add(locationComboBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(420, -1), new Dimension(420, -1), new Dimension(420, -1), 0, false));
        byTimePanel = new JPanel();
        byTimePanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(byTimePanel, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("beginning"));
        byTimePanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("ending"));
        byTimePanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timeStatusLabel = new JLabel();
        timeStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        timeStatusLabel.setText("");
        byTimePanel.add(timeStatusLabel, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        final Spacer spacer3 = new Spacer();
        byTimePanel.add(spacer3, new GridConstraints(0, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        endingTimePicker = new SimpleTimePicker();
        byTimePanel.add(endingTimePicker.$$$getRootComponent$$$(), new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        beginningTimePicker = new SimpleTimePicker();
        byTimePanel.add(beginningTimePicker.$$$getRootComponent$$$(), new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        disabledDarkModeRadioButton = new JRadioButton();
        disabledDarkModeRadioButton.setSelected(true);
        this.$$$loadButtonText$$$(disabledDarkModeRadioButton, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("disabledName"));
        panel4.add(disabledDarkModeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        byTimeDarkModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(byTimeDarkModeRadioButton, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("byTimeName"));
        panel4.add(byTimeDarkModeRadioButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        alwaysDarkModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(alwaysDarkModeRadioButton, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("alwaysName"));
        panel4.add(alwaysDarkModeRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        byLocationDarkModeRadioButton = new JRadioButton();
        this.$$$loadButtonText$$$(byLocationDarkModeRadioButton, ResourceBundle.getBundle("translations/AppearanceSetterPanelBundle").getString("byLocationName"));
        panel4.add(byLocationDarkModeRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(0, 2, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        contentPane.add(spacer5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(disabledDarkModeRadioButton);
        buttonGroup.add(alwaysDarkModeRadioButton);
        buttonGroup.add(byTimeDarkModeRadioButton);
        buttonGroup.add(byLocationDarkModeRadioButton);
        buttonGroup.add(byTimeDarkModeRadioButton);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
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
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
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

    private boolean byLocationSettingsValid() {
        return true;
    }

    private boolean byTimeSettingsValid() {
        final Date beginTime = beginningTimePicker.getSelectedSimpleTime().toDate();
        final Date endTime = endingTimePicker.getSelectedSimpleTime().toDate();

        return beginTime.after(endTime);
    }

    private void createUIComponents() {
        locationTextField = new PlaceholderTextField();
        ((PlaceholderTextField) locationTextField).setPlaceholder(
                Translation.getTranslatedString("AppearanceSetterPanelBundle", "yourLocation"));
    }

    private void initDarkModeButtonGroup() {
        disabledDarkModeRadioButton.addChangeListener(e -> {
            if (disabledDarkModeRadioButton.isSelected()) {
                byTimePanel.setVisible(false);
                byLocationPanel.setVisible(false);
            }
        });
        alwaysDarkModeRadioButton.addChangeListener(e -> {
            if (alwaysDarkModeRadioButton.isSelected()) {
                byTimePanel.setVisible(false);
                byLocationPanel.setVisible(false);
            }
        });
        byTimeDarkModeRadioButton.addChangeListener(e -> {
            if (byTimeDarkModeRadioButton.isSelected()) {
                byTimePanel.setVisible(true);
                byLocationPanel.setVisible(false);
            }
        });
        byLocationDarkModeRadioButton.addChangeListener(e -> {
            if (byLocationDarkModeRadioButton.isSelected()) {
                byTimePanel.setVisible(false);
                byLocationPanel.setVisible(true);
            }
        });
    }

    private void initDefaultsForPanels() {
        byTimePanel.setVisible(false);
        byLocationPanel.setVisible(false);
    }

    private void initGui() {
        setLayout(new GridLayout());
        add(contentPane);

        initTimePickers();

        initDefaultsForPanels();

        initLocationUI();

        initDarkModeButtonGroup();

        validateSimpleTimePanels();

        translate();
    }

    private void initLocationUI() {
        locationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Location) {
                    return super.getListCellRendererComponent(list, ((Location) value).getAddress(), index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        locationTextField.getDocument().addDocumentListener(new DocumentListener() {
            String locationName = null;
            Timer timer = new Timer(1000, e -> {
                if (locationName != null && !locationName.isEmpty()) {
                    final String encodedLocationName = new String(this.locationName.getBytes(
                            Charset.forName(ApplicationConstants.DEFAULT_APPLICATION_CHARSET)));
                    LocationManager locationManager = new LocationManager(encodedLocationName);
                    Location[] locations;
                    try {
                        locations = locationManager.getLocations();
                    } catch (IOException e1) {
                        log.warn("Could not get locations for name: {}", this.locationName, e1);
                        locations = new Location[0];
                    }

                    updateLocationVerificationStatus(locations.length > 0);
                    DefaultComboBoxModel<Location> model = new DefaultComboBoxModel<>(locations);
                    locationComboBox.setModel(model);
                } else {
                    updateLocationVerificationStatus(false);
                }
            });


            @Override
            public void changedUpdate(DocumentEvent e) {
                /*NOP*/
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateData();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateData();
            }

            private void updateData() {
                locationComboBox.setModel(new DefaultComboBoxModel<>());
                locationName = locationTextField.getText();

                if (locationName.isEmpty()) {
                    updateLocationVerificationStatus(false);
                } else {
                    setLocationStatusLabelLoading();
                }

                timer.setRepeats(false);
                timer.restart();

            }
        });

        locationTextField.addKeyListener(FrameUtils.getSmartKeyAdapter(locationComboBox));
    }

    private void initTimePickers() {
        beginningTimePicker.setTime(SettingsConstants.DARK_MODE_BEGINNING_DEFAULT_TIME);
        endingTimePicker.setTime(SettingsConstants.DARK_MODE_ENDING_DEFAULT_TIME);

        final ValueChangeListener validate = e -> {
            validateSimpleTimePanels();
        };
        beginningTimePicker.addValueChangeListener(validate);
        endingTimePicker.addValueChangeListener(validate);
    }

    private void loadByLocationSettings(DarkModeValue realDarkModeValue) {
        byLocationDarkModeRadioButton.setSelected(true);
        locationTextField.setText(realDarkModeValue.getLocation().getAddress());
        locationComboBox.setSelectedItem(realDarkModeValue.getLocation());
    }

    private void loadByTimeSettings(DarkModeValue realDarkModeValue) {
        byTimeDarkModeRadioButton.setSelected(true);

        final Date startTime = realDarkModeValue.getNext().getStart();
        final Date endTime = realDarkModeValue.getPrevious().getEnd();

        final int startHour = startTime.toInstant().atZone(ZoneId.systemDefault()).getHour();
        final int startMinute = startTime.toInstant().atZone(ZoneId.systemDefault()).getMinute();
        final int endHour = endTime.toInstant().atZone(ZoneId.systemDefault()).getHour();
        final int endMinute = endTime.toInstant().atZone(ZoneId.systemDefault()).getMinute();


        beginningTimePicker.setTime(new SimpleTime(startHour, startMinute));
        endingTimePicker.setTime(new SimpleTime(endHour, endMinute));
    }

    @Override
    public void loadSettings() {
        final Object object = PreferencesManager.getRealDarkMode();
        if (object instanceof Boolean) {
            Boolean b = (Boolean) object;
            if (b) {
                alwaysDarkModeRadioButton.setSelected(true);
            } else {
                disabledDarkModeRadioButton.setSelected(true);
            }
        } else if (object instanceof DarkModeValue) {
            final DarkModeValue realDarkModeValue = (DarkModeValue) object;
            if (realDarkModeValue.getNext() != null && realDarkModeValue.getPrevious() != null) {
                loadByTimeSettings(realDarkModeValue);
            } else if (realDarkModeValue.getLocation() != null) {
                loadByLocationSettings(realDarkModeValue);
            }
        }
    }

    private void saveByTimeSettings() {
        final String begins = beginningTimePicker.getSelectedSimpleTime().getHour() + ":" + beginningTimePicker.getSelectedSimpleTime().getMinute();
        final String ends = endingTimePicker.getSelectedSimpleTime().getHour() + ":" + endingTimePicker.getSelectedSimpleTime().getMinute();
        final String value = begins + ";" + ends;
        PreferencesManager.setDarkMode(value);
        log.info("Saving settings: dark mode: {}", value);
    }

    @Override
    public void saveSettings() {
        if (disabledDarkModeRadioButton.isSelected()) {
            final String value = PreferencesManager.DARK_MODE.DISABLED.toString();
            PreferencesManager.setDarkMode(value);
            log.info("Saving settings: dark mode: {}", value);
        } else if (alwaysDarkModeRadioButton.isSelected()) {
            final String value = PreferencesManager.DARK_MODE.ALWAYS.toString();
            PreferencesManager.setDarkMode(value);
            log.info("Saving settings: dark mode: {}", value);
        } else if (byTimeDarkModeRadioButton.isSelected()) {
            if (byTimeSettingsValid()) {
                saveByTimeSettings();
            } else {
                final SimpleTime begins = beginningTimePicker.getSelectedSimpleTime();
                final SimpleTime ends = endingTimePicker.getSelectedSimpleTime();
                log.warn("Saving settings: dark mode: declined, settings set are not valid - begin time: {} is not after ending time: {}", begins, ends);
            }
        } else {
            if (byLocationSettingsValid()) {
                final Location location = ((Location) locationComboBox.getSelectedItem());
                if (location != null) {
                    String value = location.getAddress() + "|" + location.getLongitude() + ";" + location.getLatitude();
                    PreferencesManager.setDarkMode(value);
                    log.info("Saving settings: dark mode: {}", value);
                } else {
                    log.warn("Saving settings: dark mode: could not get location: null");
                }
            }
        }
    }

    private void setLocationStatusLabelLoading() {
        try {
            ImageIcon imageIcon;
            if (PreferencesManager.isDarkModeEnabledNow()) {
                imageIcon = new ImageIcon(AppearanceSetterPanel.class.getResource("/images/loadingDark.gif"));
            } else {
                imageIcon = new ImageIcon(AppearanceSetterPanel.class.getResource("/images/loading.gif"));
            }
            locationStatusLabel.setIcon(imageIcon);
        } catch (Exception e) {
            /*NOP*/
        }
    }

    @Override
    public void translate() {
        Translation translation = new Translation("AppearanceSetterPanelBundle");
        darkModeLabel.setText(translation.getTranslatedString("darkMode"));
        disabledDarkModeRadioButton.setText(translation.getTranslatedString("disabledName"));
        alwaysDarkModeRadioButton.setText(translation.getTranslatedString("alwaysName"));
        byTimeDarkModeRadioButton.setText(translation.getTranslatedString("byTimeName"));
        byLocationDarkModeRadioButton.setText(translation.getTranslatedString("byLocationName"));
    }

    private void updateLocationVerificationStatus(boolean b) {
        if (b) {
            locationStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiSuccess16.png")));
        } else {
            locationStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        }
    }

    private void updateTimeVerificationStatus(boolean b) {
        if (b) {
            timeStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiSuccess16.png")));
        } else {
            timeStatusLabel.setIcon(new ImageIcon(getClass().getResource("/images/emojiCross16.png")));
        }
    }

    private void validateSimpleTimePanels() {
        final SimpleTime begin = beginningTimePicker.getSelectedSimpleTime();
        final SimpleTime end = endingTimePicker.getSelectedSimpleTime();
        AppearanceSetterPanel.this.updateTimeVerificationStatus(!begin.equals(end));
    }
}