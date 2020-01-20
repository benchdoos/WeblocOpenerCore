package com.github.benchdoos.weblocopenercore.service.windows;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WindowSetting {
    @EqualsAndHashCode.Include
    private String windowClassName;
    private Map<WindowSettingName, Object> settings;
}
