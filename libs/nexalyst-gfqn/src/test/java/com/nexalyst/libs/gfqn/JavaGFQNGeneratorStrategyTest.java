package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavaGFQNGeneratorStrategyTest {

    private JavaGFQNGeneratorStrategy generator;
    private GFQNContext context;

    @BeforeEach
    void setUp() {
        generator = new JavaGFQNGeneratorStrategy();
        context = new GFQNContext("nexalyst", "libs", "gfqn");
    }

    @Test
    void testGenerateLanguageQualifiedNameWithPackage() {
        // Create a compilation unit with a package
        Map<String, String> metadata = new HashMap<>();
        metadata.put("package", "com.example");
        CompilationUnit unit = new CompilationUnit("/path/to/Example.java", "Example", GFQNSupportedLanguages.JAVA, metadata);

        // Test generating the language-qualified name
        String qualifiedName = generator.generateLanguageQualifiedName(unit, context);
        assertEquals("com.example.Example", qualifiedName);
    }

    @Test
    void testGenerateLanguageQualifiedNameWithoutPackage() {
        // Create a compilation unit without a package
        Map<String, String> metadata = new HashMap<>();
        CompilationUnit unit = new CompilationUnit("/path/to/Example.java", "Example", GFQNSupportedLanguages.JAVA, metadata);

        // Test generating the language-qualified name
        String qualifiedName = generator.generateLanguageQualifiedName(unit, context);
        assertEquals("Example", qualifiedName);
    }

    @Test
    void testGenerateLanguageQualifiedNameWithEmptyPackage() {
        // Create a compilation unit with an empty package
        Map<String, String> metadata = new HashMap<>();
        metadata.put("package", "");
        CompilationUnit unit = new CompilationUnit("/path/to/Example.java", "Example", GFQNSupportedLanguages.JAVA, metadata);

        // Test generating the language-qualified name
        String qualifiedName = generator.generateLanguageQualifiedName(unit, context);
        assertEquals("Example", qualifiedName);
    }

    @Test
    void testGetSupportedLanguage() {
        // Test that the supported language is "java"
        assertTrue(generator.getSupportedLanguage().contains(GFQNSupportedLanguages.JAVA));
    }
}