package com.github.benchdoos.weblocopenercore.service.windows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.awt.Dimension;
import java.awt.Point;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WindowSettings {
    @EqualsAndHashCode.Include
    private String window;
    private Settings settings;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Settings {
        private Point location;
        private Dimension size;
        private Integer extendedState;
    }
}
