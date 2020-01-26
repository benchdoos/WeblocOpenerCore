package com.github.benchdoos.weblocopenercore.service.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.Image;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Feedback {
    private UUID uuid;
    private String feedback;
    private String email;
    private String logFileContent;
    private List<Image> images;
}
