package com.nexalyst.libs.gfqn;

import java.nio.file.Path;

public class CompilationUnitMetadata {
    private final Path compilationUnitPath;
    private final GFQNSupportedLanguages language;

    public CompilationUnitMetadata(Path compilationUnitPath, GFQNSupportedLanguages language) {
        this.compilationUnitPath = compilationUnitPath;
        this.language = language;
    }

    public Path getCompilationUnitPath() {
        return compilationUnitPath;
    }

    public GFQNSupportedLanguages getLanguage() {
        return language;
    }

    public String getLanguageName() {
        return language.getLanguage();
    }

}