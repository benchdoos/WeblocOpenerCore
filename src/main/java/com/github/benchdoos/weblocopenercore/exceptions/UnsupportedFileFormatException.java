package com.github.benchdoos.weblocopenercore.exceptions;

public class UnsupportedFileFormatException extends RuntimeException {
    public UnsupportedFileFormatException(String filePath) {
        super(String.format("File format is not supported: %s", filePath));
    }
}
