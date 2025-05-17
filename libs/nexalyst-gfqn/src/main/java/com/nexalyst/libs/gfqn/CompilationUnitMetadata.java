package com.nexalyst.libs.gfqn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CompilationUnitMetadata implements GFQNCompilationUnit {
    private final Path path;
    private final GFQNSupportedLanguages language;

    public CompilationUnitMetadata(Path path, GFQNSupportedLanguages language) {
        if (path == null || language == null) {
            throw new IllegalArgumentException("Path and language must not be null.");
        }
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("The file does not exist: " + path);
        }
        this.path = path;
        this.language = language;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public GFQNSupportedLanguages getLanguage() {
        return language;
    }

    @Override
    public String loadContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public boolean isValid() {
        try {
            return Files.size(path) > 0;
        } catch (IOException e) {
            return false;
        }
    }
}