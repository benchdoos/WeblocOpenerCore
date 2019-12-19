package com.github.benchdoos.weblocopenercore.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

@AllArgsConstructor
@Data
public class ComparedFile {
    private int equalSymbols;
    private File file;
}