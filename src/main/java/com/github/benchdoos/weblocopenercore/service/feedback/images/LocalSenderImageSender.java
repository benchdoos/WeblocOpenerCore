package com.github.benchdoos.weblocopenercore.service.feedback.images;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalSenderImageSender implements ImageSender {
    @Override
    public List<ImageInfo> sendImages(List<String> base64ImagesList) {
        if (CollectionUtils.isNotEmpty(base64ImagesList)) {
            final List<ImageInfo> result = new ArrayList<>();
            int index = 0;
            for (String base64Image : base64ImagesList) {
                final ImageInfo imageInfo = new ImageInfo(
                        "imageUrl-" + index + "-size-" + base64Image.length(),
                        "deleteImageUrl-" + index + "-size-" + base64Image.length());
                result.add(imageInfo);

                index++;
            }
            return result;
        }
        return null;
    }
}
