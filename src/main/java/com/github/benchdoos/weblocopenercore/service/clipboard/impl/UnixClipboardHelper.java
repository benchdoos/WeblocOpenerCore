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

package com.github.benchdoos.weblocopenercore.service.clipboard.impl;

import lombok.extern.log4j.Log4j2;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;

/**
 * This holy crap is because gnome does not support normal copying for java or otherwise
 */
@Log4j2
public class UnixClipboardHelper extends Thread {
    private static BufferedReader stdInCh = new BufferedReader(
            new InputStreamReader(Channels.newInputStream((
                    new FileInputStream(FileDescriptor.in)).getChannel())));
    final InputStream in = System.in;
    private final String text;
    private final BufferedImage image;

    UnixClipboardHelper(String text) {
        this.text = text;
        this.image = null;
    }

    UnixClipboardHelper(BufferedImage image) {
        this.text = null;
        this.image = image;
    }

    @Override
    public void run() {
        try {
            if (text != null) {
                copy(text);
            }
            if (image != null) {
                copy(image);
            }
        } catch (IOException e) {
            log.warn("Can not copy or something is wrong, but I guess it's ok!");
            log.trace("Can not copy or something is wrong, but I guess it's ok!", e);
        }
        super.run();
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    private void copy(BufferedImage image) throws IOException {
        CopyImageToClipBoard ci = new CopyImageToClipBoard();
        ci.copyImage(image);
        stdInCh.read();
    }

    private void copy(String text) throws IOException {
        StringSelection stringSelection = new StringSelection(text);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        stdInCh.read();
    }
}
