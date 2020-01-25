package com.github.benchdoos.weblocopenercore.service.feedback;

import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.ImagesUtils;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.StringUtil;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Log4j2
public class FeedbackService {

    public int sendFeedback(FeedbackDto feedbackDto) {
        final Thread thread = Thread.currentThread();

        log.info("Starting sending feedback.");
        log.debug("Feedback data: {}", feedbackDto);

        if (!thread.isInterrupted()) {
            final Base64FeedbackDto base64FeedbackDto = convertToBase64FeedbackDto(feedbackDto);
            log.debug("Prepared base64 feedback. ({})", base64FeedbackDto);
        }

        if (thread.isInterrupted()) {
            log.warn("Feedback sending was interrupted.");
        }
        return -1;
    }

    private Base64FeedbackDto convertToBase64FeedbackDto(@NotNull FeedbackDto feedbackDto) {
        final Thread thread = Thread.currentThread();

        final Base64FeedbackDto result = new Base64FeedbackDto();

        if (!thread.isInterrupted() && feedbackDto.getUuid() != null) {
            result.setUuid(feedbackDto.getUuid());
        }

        if (!thread.isInterrupted() && feedbackDto.getFeedback() != null) {
            result.setBase64Feedback(Base64.getEncoder().encodeToString(feedbackDto.getFeedback().getBytes()));
        }

        if (!thread.isInterrupted() && feedbackDto.getImages() != null) {
            final List<String> base64ImagesList = new ArrayList<>();

            for (Image image : feedbackDto.getImages()) {
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

        if (!thread.isInterrupted() && !StringUtil.isBlank(feedbackDto.getLogFileContent())) {
            result.setBase64LogFile(Base64.getEncoder().encodeToString(feedbackDto.getLogFileContent().getBytes()));
        }

        return result;
    }
}
