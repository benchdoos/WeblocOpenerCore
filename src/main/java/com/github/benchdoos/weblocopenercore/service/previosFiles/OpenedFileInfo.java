package com.github.benchdoos.weblocopenercore.service.previosFiles;

import com.github.benchdoos.linksupport.links.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OpenedFileInfo {
    private Link type;
    private String filename;
    private Path filePath;
}
