package com.github.benchdoos.weblocopenercore.gui;

import com.github.benchdoos.linksupport.links.Link;
import com.github.benchdoos.weblocopenercore.core.Translation;
import com.github.benchdoos.weblocopenercore.core.constants.ApplicationConstants;
import com.github.benchdoos.weblocopenercore.utils.Converter;
import com.github.benchdoos.weblocopenercore.utils.FrameUtils;
import com.github.benchdoos.weblocopenercore.utils.notification.NotificationManager;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ConverterDialog extends JDialog implements Translatable{
    private final ImageIcon rickAndMortyIcon = new ImageIcon(Toolkit.getDefaultToolkit()
            .getImage(getClass().getResource("/images/easter/rickAndMorty.gif")));

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Link> converterComboBox;
    private List<File> filesToConvert;

    public ConverterDialog(List<File> filesToConvert) {
        Assertions.assertThat(filesToConvert).isNotNull();
        Assertions.assertThat(filesToConvert.size()).isGreaterThan(0);
        this.filesToConvert = filesToConvert;

        initGui();

        fillConverterComboBox();

        translate();

        pack();
        setResizable(false);
    }

    private void fillConverterComboBox() {
        final DefaultComboBoxModel<Link> model = new DefaultComboBoxModel<>(Link.values());
        converterComboBox.setModel(model);
    }

    private void initGui() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initTitle();
        initListeners();

        initConverterComboBox();
    }

    private void initConverterComboBox() {
        converterComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jList, Object o, int i, boolean b, boolean b1) {
                if (o instanceof Link) {
                    final Link link = (Link) o;
                    return super.getListCellRendererComponent(jList, link.getExtension(), i, b, b1);
                }
                return super.getListCellRendererComponent(jList, o, i, b, b1);
            }
        });
    }

    private void initTitle() {
        final Translation translation = new Translation("ConvertDialogBundle");
        if (filesToConvert.size() == 1) {
            final String title = String.format(translation.getTranslatedString("windowTitleSolo"), filesToConvert.get(0));
            setTitle(title);
        } else {
            final String title = String.format(translation.getTranslatedString("windowTitleMultiple"), filesToConvert.size());
            setTitle(title);
        }
    }

    private void initListeners() {
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
    }

    private void convertFiles() {
        int easterCounter = 0;

        final List<File> convertedFiles = new ArrayList<>();

        for (final File file : filesToConvert) {
            try {
                if (FilenameUtils.removeExtension(file.getName()).toLowerCase().contains("rick and morty")) {
                    if (easterCounter == 0) {
                        showRickAndMortyEaster();
                        easterCounter++;
                    }
                }
            } catch (Exception e) {
                log.warn("Rick and Morty easter egg is broken", e);
            }

            final Link link = (Link) converterComboBox.getSelectedItem();

            if (link != null) {
                log.info("User selected link type: {}", link.getName());
                try {
                    convertedFiles.add(Converter.convert(file, link));

                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException e) {
                        log.warn("Could not delete file: {}", file, e);
                    }
                } catch (FileExistsException e) {
                    log.warn("File already exists, not touching it. Adding file as converted.");
                    convertedFiles.add(file);
                } catch (Exception e) {
                    log.warn("Could not convert file: {} to link: {}", file, link, e);
                }
            } else {
                log.warn("Could not convert files. Link was not selected.");
            }
        }

        final Translation translation = new Translation("ConvertDialogBundle");
        if (filesToConvert.size() == convertedFiles.size()) {
            NotificationManager.getNotificationForCurrentOS().showInfoNotification(
                    ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                    translation.getTranslatedString("allFilesSuccessfullyConverted")
                            + convertedFiles.size() + "/" + filesToConvert.size());
        } else {
            NotificationManager.getNotificationForCurrentOS().showWarningNotification(
                    ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME,
                    translation.getTranslatedString("someFilesFailedToConvert")
                            + convertedFiles.size() + "/" + filesToConvert.size());
        }
    }

    private void onOK() {
        dispose();
        convertFiles();
    }

    private void onCancel() {
        dispose();
    }

    private void showRickAndMortyEaster() {
        log.info("Look! This user has found an easter egg (Rick and Morty). Good job!");

        JFrame frame = new JFrame(ApplicationConstants.WEBLOCOPENER_APPLICATION_NAME);
        frame.setLayout(new GridLayout());
        JLabel label = new JLabel();
        label.setIcon(rickAndMortyIcon);
        frame.add(label);
        frame.setUndecorated(true);
        frame.setSize(500, 281);
        frame.setResizable(false);
        frame.setLocation(FrameUtils.getFrameOnCenterLocationPoint(frame));
        Timer timer = new Timer(5000, e -> frame.dispose());
        timer.setRepeats(false);
        timer.start();
        frame.setVisible(true);
    }

    @Override
    public void translate() {
        Translation translation = new Translation("CommonsBundle");
        buttonCancel.setText(translation.getTranslatedString("cancelButton"));
        buttonOK.setText(translation.getTranslatedString("okButton"));
    }
}
