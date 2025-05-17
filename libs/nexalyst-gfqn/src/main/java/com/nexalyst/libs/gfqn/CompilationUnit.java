package com.nexalyst.libs.gfqn;

import java.util.Map;

public class CompilationUnit {
    private final String path;
    private final String name;
    private final GFQNSupportedLanguages language;
    private final Map<String, String> metadata;

    public CompilationUnit(String path, String name, GFQNSupportedLanguages language, Map<String, String> metadata) {
        this.path = path;
        this.name = name;
        this.language = language;
        this.metadata = metadata;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public GFQNSupportedLanguages getLanguage() {
        return language;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}