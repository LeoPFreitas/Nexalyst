package com.nexalyst.libs.gfqn;

import java.io.IOException;
import java.nio.file.Path;

public interface GFQNCompilationUnit {

    /**
     * Gets the path of the compilation unit file.
     *
     * @return the path to the file.
     */
    Path getPath();

    /**
     * Gets the language associated with this compilation unit.
     *
     * @return the supported language.
     */
    GFQNSupportedLanguages getLanguage();

    /**
     * Loads the raw content of the compilation unit file.
     *
     * @return the file content as a string.
     * @throws IOException if there is an issue reading the file.
     */
    String loadContent() throws IOException;

    /**
     * Validates whether the compilation unit is valid.
     *
     * @return true if valid, false otherwise.
     */
    boolean isValid();
}