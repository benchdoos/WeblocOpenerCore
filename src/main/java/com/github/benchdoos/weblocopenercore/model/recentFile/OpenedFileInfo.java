package com.github.benchdoos.weblocopenercore.model.recentFile;

import com.github.benchdoos.linksupport.links.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.nio.file.Path;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
public class OpenedFileInfo {
    private Link type;
    private String filename;
    private Path filePath;

    public static OpenedFileInfo fromFile(File file) {
        if (file.exists()) {
            final Link linkForFile = Link.getLinkForFile(file);
            return new OpenedFileInfo(linkForFile, file.getName(), file.toPath());
        }
        return null;
    }
}
