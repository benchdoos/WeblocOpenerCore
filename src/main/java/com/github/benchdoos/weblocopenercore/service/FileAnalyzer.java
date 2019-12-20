package com.github.benchdoos.weblocopenercore.service;

import com.github.benchdoos.weblocopenercore.exceptions.LinkCanNotBeProcessedException;

public interface FileAnalyzer {
    LinkFile getLinkFile() throws LinkCanNotBeProcessedException;
}
