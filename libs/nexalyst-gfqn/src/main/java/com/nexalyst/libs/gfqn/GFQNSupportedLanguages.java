package com.nexalyst.libs.gfqn;

/**
 * GFQNSupportedLanguages is an enumeration of programming languages that are supported
 * for generating globally fully qualified names (GFQN). Each language is represented
 * by a unique string identifier.
 * <p>
 * This enum is used to define and refer to programming languages in the context
 * of GFQN generation, allowing the appropriate GFQN generation strategy to be
 * associated with each supported language.
 * <p>
 * Example usage scenarios include mapping language-specific strategies, registering
 * and handling languages within the GFQNService class, and supporting multi-language
 * repositories or systems.
 * <p>
 * Enum constants:
 * - JAVA: Represents the Java programming language.
 * - PYTHON: Represents the Python programming language.
 * - JAVASCRIPT: Represents the JavaScript programming language.
 * - CSHARP: Represents the C# programming language.
 * - RUBY: Represents the Ruby programming language.
 * - PHP: Represents the PHP programming language.
 * - GO: Represents the Go programming language.
 * - SWIFT: Represents the Swift programming language.
 * <p>
 * The associated string identifier for each language can be accessed with the `getLanguage` method.
 */
public enum GFQNSupportedLanguages {
    JAVA("java"),
    PYTHON("python"),
    JAVASCRIPT("javascript"),
    CSHARP("csharp"),
    RUBY("ruby"),
    PHP("php"),
    GO("go"),
    SWIFT("swift");

    private final String language;

    GFQNSupportedLanguages(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }
}
