package com.github.benchdoos.weblocopenercore.service.feedback.images;

import java.util.List;

public interface ImageSender {
    /**
     * Send images in base64
     * @param base64ImagesList list of base64 images
     * @return list of urls
     */
    List<ImageInfo> sendImages(List<String> base64ImagesList);
}
