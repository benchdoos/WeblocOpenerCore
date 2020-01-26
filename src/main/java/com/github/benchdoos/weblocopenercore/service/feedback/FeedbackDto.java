package com.github.benchdoos.weblocopenercore.service.feedback;

import com.github.benchdoos.weblocopenercore.service.feedback.images.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FeedbackDto {
    private UUID userUuid;
    private String base64FeedbackMessage;
    private String base64LogFile;
    private String email;
    private List<ImageInfo> images;
}
