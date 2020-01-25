package com.github.benchdoos.weblocopenercore.utils;

import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j2
public class ImagesUtils {
    public static int getImageWeight(BufferedImage image) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            final byte[] data = outputStream.toByteArray();
            return data.length;
        } catch (IOException e) {
            log.warn("Could not count image weight. Returning -1.", e);
            return -1;
        }
    }
}
