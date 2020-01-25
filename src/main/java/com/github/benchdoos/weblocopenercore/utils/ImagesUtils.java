package com.github.benchdoos.weblocopenercore.utils;

import lombok.extern.log4j.Log4j2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j2
public class ImagesUtils {
    public static int getImageWeight(BufferedImage image) {
        try  {
            final byte[] bytes = getBytes(image);
            return bytes.length;
        } catch (final IOException e) {
            log.warn("Could not count image weight. Returning -1.", e);
            return -1;
        }
    }

    public static byte[] getBytes(BufferedImage image) throws IOException {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        }
    }
}