package com.github.benchdoos.weblocopenercore.service.feedback;

import com.github.benchdoos.weblocopenercore.service.feedback.images.ImageInfo;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImageSender;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImgbbImageSender;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.ImagesUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.StringUtil;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Log4j2
public class FeedbackService {

    public int sendFeedback(Feedback feedback) {
        final Thread thread = Thread.currentThread();

        log.info("Starting sending feedback.");
        log.debug("Feedback data: {}", feedback);

        final Base64Feedback base64Feedback;

        if (!thread.isInterrupted()) {
            base64Feedback = convertToBase64FeedbackDto(feedback);
            log.debug("Prepared base64 feedback. ({})", base64Feedback);

            final FeedbackDto feedbackDto = new FeedbackDto();

            if (!thread.isInterrupted()) {
                final List<ImageInfo> imageInfoList = sendImages(base64Feedback);

                if (!CollectionUtils.isEmpty(imageInfoList)) {
                    feedbackDto.setImages(imageInfoList);
                }
            }

            feedback.setFeedback(base64Feedback.getBase64Feedback());
            feedback.setLogFileContent(base64Feedback.getBase64LogFile());
            feedback.setUuid(base64Feedback.getUuid());

            if (!thread.isInterrupted()) {
                //todo send dto to server here
            }
        }

        if (thread.isInterrupted()) {
            log.warn("Feedback sending was interrupted.");
        }
        return -1;
    }

    private List<ImageInfo> sendImages(Base64Feedback base64Feedback) {
        final ImageSender sender = new ImgbbImageSender();
        return sender.sendImages(base64Feedback.getBase64Images());
    }

    private Base64Feedback convertToBase64FeedbackDto(@NotNull Feedback feedback) {
        final Thread thread = Thread.currentThread();

        final Base64Feedback result = new Base64Feedback();

        if (!thread.isInterrupted() && feedback.getUuid() != null) {
            result.setUuid(feedback.getUuid());
        }

        if (!thread.isInterrupted() && !StringUtil.isBlank(feedback.getFeedback())) {
            result.setBase64Feedback(Base64.getEncoder().encodeToString(feedback.getFeedback().getBytes()));
        }

        if (!thread.isInterrupted() && !CollectionUtils.isEmpty(feedback.getImages())) {
            final List<String> base64ImagesList = new ArrayList<>();

            for (Image image : feedback.getImages()) {
                if (!thread.isInterrupted()) {
                    try {
                        base64ImagesList.add(Base64.getEncoder().encodeToString(ImagesUtils.getBytes(CoreUtils.toBufferedImage(image))));
                    } catch (final IOException e) {
                        log.warn("Can not transform image to base64", e);
                    }
                }
            }
            result.setBase64Images(base64ImagesList);
        }

        if (!thread.isInterrupted() && !StringUtil.isBlank(feedback.getLogFileContent())) {
            result.setBase64LogFile(Base64.getEncoder().encodeToString(feedback.getLogFileContent().getBytes()));
        }

        return result;
    }
}
