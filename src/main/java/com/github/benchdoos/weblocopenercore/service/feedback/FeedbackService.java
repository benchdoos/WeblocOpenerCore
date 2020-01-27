package com.github.benchdoos.weblocopenercore.service.feedback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benchdoos.weblocopenercore.core.constants.StringConstants;
import com.github.benchdoos.weblocopenercore.preferences.PreferencesManager;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImageInfo;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImageSender;
import com.github.benchdoos.weblocopenercore.service.feedback.images.ImgbbImageSender;
import com.github.benchdoos.weblocopenercore.service.feedback.images.LocalSenderImageSender;
import com.github.benchdoos.weblocopenercore.utils.CoreUtils;
import com.github.benchdoos.weblocopenercore.utils.ImagesUtils;
import com.github.benchdoos.weblocopenercore.utils.http.HttpUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.StringUtil;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Log4j2
public class FeedbackService {
    private final HttpUtils<UUID> httpUtils = new HttpUtils<>(UUID.class);

    public int sendFeedback(Feedback feedback) throws IOException {
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

                if (!thread.isInterrupted()) {
                    if (!CollectionUtils.isEmpty(imageInfoList)) {
                        feedbackDto.setImages(imageInfoList);
                    }
                }
            }

            feedbackDto.setEmail(base64Feedback.getEmail());
            feedbackDto.setBase64FeedbackMessage(base64Feedback.getBase64Feedback());
            feedbackDto.setBase64LogFile(base64Feedback.getBase64LogFile());
            feedbackDto.setUserUuid(base64Feedback.getUuid());

            if (!thread.isInterrupted()) {
                log.info("Preparing to send feedback: {}", feedbackDto);
                return sendFeedback(feedbackDto);
            }
        }

        log.warn("Feedback sending was interrupted.");
        return -1;
    }

    private int sendFeedback(FeedbackDto feedbackDto) throws IOException {
        final HttpPost post = preparePostRequest(feedbackDto);
        final HttpUtils.HttpResponse<UUID> httpResponse = httpUtils.sendHttpRequest(post);
        log.info("Response for sending feedback: {}", httpResponse);
        return httpResponse.getCode();
    }

    private HttpPost preparePostRequest(FeedbackDto feedbackDto) throws IOException {
        final HttpPost post = new HttpPost(getUrl());
        post.setEntity(preparePostEntity(feedbackDto));
        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        return post;
    }

    private HttpEntity preparePostEntity(FeedbackDto feedbackDto) throws JsonProcessingException {
        final BasicHttpEntity result = new BasicHttpEntity();

        final ObjectMapper mapper = new ObjectMapper();
        final byte[] bytes = mapper.writeValueAsBytes(feedbackDto);

        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        result.setContent(byteArrayInputStream);
        return result;
    }

    private String getUrl() {
        if (!PreferencesManager.isDevMode()) {
            return StringConstants.SEND_FEEDBACK_URL;
        } else {
            return StringConstants.SEND_FEEDBACK_DEV_MODE_URL;
        }
    }

    private List<ImageInfo> sendFakeImages(Base64Feedback base64Feedback) {
        log.warn("[DEV MODE ENABLED] sending fake images");
        return new LocalSenderImageSender().sendImages(base64Feedback.getBase64Images());
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

        if (!thread.isInterrupted() && !StringUtil.isBlank(feedback.getEmail())) {
            result.setEmail(feedback.getEmail());
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
