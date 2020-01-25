package com.github.benchdoos.weblocopenercore.service.feedback.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ImgbbResponseDto {
    private int status;
    private boolean success;
    private ResponseData data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class ResponseData {
        private String id;

        @JsonProperty("url_viewer")
        private String urlViewer;

        private String url;

        @JsonProperty("delete_url")
        private String deleteUrl;
    }
}
