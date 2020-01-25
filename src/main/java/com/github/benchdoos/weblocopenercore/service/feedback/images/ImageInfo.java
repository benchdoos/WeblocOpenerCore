package com.github.benchdoos.weblocopenercore.service.feedback.images;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImageInfo {
    private String imageUrl;
    private String deleteImageUrl;
}
