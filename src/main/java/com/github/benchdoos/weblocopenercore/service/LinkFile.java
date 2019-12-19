package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.linksupport.links.Link;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.net.URL;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LinkFile {
    private File file;
    private Link type;
    private URL url;
}
