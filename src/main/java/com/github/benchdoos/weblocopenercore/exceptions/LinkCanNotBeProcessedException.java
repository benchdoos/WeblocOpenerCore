package com.github.benchdoos.weblocopenercore.exceptions;

public class LinkCanNotBeProcessedException extends RuntimeException {

    public LinkCanNotBeProcessedException(String message, Throwable ex) {
        super(message, ex);
    }
}
