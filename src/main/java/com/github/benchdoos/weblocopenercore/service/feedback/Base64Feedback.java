package com.github.benchdoos.weblocopenercore.service.feedback;

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
public class Base64Feedback {
    private UUID uuid;
    private String base64Feedback;
    private String email;
    private String base64LogFile;
    private List<String> base64Images;
}
