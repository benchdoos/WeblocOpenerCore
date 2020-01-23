package com.github.benchdoos.weblocopenercore.service.feedback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Extensions for images that are supported
 */
@Getter
@RequiredArgsConstructor
public enum FileExtension {
    JPG(Arrays.asList("jpg","jpeg")),
    PNG(Collections.singletonList("png"));

    private final List<String> extensions;
}
