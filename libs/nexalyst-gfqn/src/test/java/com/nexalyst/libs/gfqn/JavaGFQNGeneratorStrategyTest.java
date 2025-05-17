package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JavaGFQNGeneratorStrategyTest {

    private JavaGFQNGeneratorStrategy generator;
    private GFQNContext context;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        generator = new JavaGFQNGeneratorStrategy();
        context = new GFQNContext("nexalyst", "libs", "gfqn");
    }

    @Test
    void testGetSupportedLanguage() {
        // Test that the supported language is "java"
        Set<GFQNSupportedLanguages> supportedLanguages = generator.getSupportedLanguage();
        assertTrue(supportedLanguages.contains(GFQNSupportedLanguages.JAVA));
        assertEquals(1, supportedLanguages.size());
    }

    @Test
    void testGenerateLanguageQualifiedNameWithNonJavaLanguage() throws IOException {
        // Create a Python file for testing
        Path pythonFilePath = tempDir.resolve("Example.py");
        String pythonContent = "def hello():\n    print('Hello, World!')\n";
        Files.writeString(pythonFilePath, pythonContent);

        // Create a compilation unit with a non-Java language
        CompilationUnitMetadata unit = new CompilationUnitMetadata(
                pythonFilePath, 
                GFQNSupportedLanguages.PYTHON);

        // Verify that an IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> generator.generateLanguageQualifiedName(unit, context));

        assertEquals("Unsupported language: PYTHON", exception.getMessage());
    }

    @Test
    void testGenerateLanguageQualifiedNameWithNonexistentFile() throws IOException {
        // Create a temporary file that we'll delete to simulate a non-existent file
        Path javaFilePath = tempDir.resolve("Temporary.java");
        String javaContent = "package com.example;\n\npublic class Temporary {\n}";
        Files.writeString(javaFilePath, javaContent);

        // Create a compilation unit with the file
        CompilationUnitMetadata unit = new CompilationUnitMetadata(
                javaFilePath, 
                GFQNSupportedLanguages.JAVA);

        // Delete the file to make it non-existent
        Files.delete(javaFilePath);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> generator.generateLanguageQualifiedName(unit, context));

        assertEquals("File does not exist: " + javaFilePath, exception.getMessage());
    }

    @Test
    void testGenerateLanguageQualifiedNameSuccess() throws IOException {
        // Create a valid Java file
        Path javaFilePath = tempDir.resolve("Example.java");
        String javaContent = "package com.example;\n\npublic class Example {\n}";
        Files.writeString(javaFilePath, javaContent);

        // Create a compilation unit with the valid Java file
        CompilationUnitMetadata unit = new CompilationUnitMetadata(
                javaFilePath, 
                GFQNSupportedLanguages.JAVA);

        // Test generating the language-qualified name
        String qualifiedName = generator.generateLanguageQualifiedName(unit, context);

        // Expected format: {organization}.{project}.{repository}.{language}.{fileName}.{packageName}.{primaryTypeName}
        String expected = "nexalyst.libs.gfqn.java.Example.com.example.Example";
        assertEquals(expected, qualifiedName);
    }

    @Test
    void testGenerateLanguageQualifiedNameWithParsingError() throws IOException {
        // Create a malformed Java file
        Path javaFilePath = tempDir.resolve("Malformed.java");
        String javaContent = "This is not valid Java code";
        Files.writeString(javaFilePath, javaContent);

        // Create a compilation unit with the malformed Java file
        CompilationUnitMetadata unit = new CompilationUnitMetadata(
                javaFilePath, 
                GFQNSupportedLanguages.JAVA);

        // Verify that a RuntimeException is thrown
        assertThrows(RuntimeException.class, 
                () -> generator.generateLanguageQualifiedName(unit, context));
    }

    @Test
    void testGenerateLanguageQualifiedNameWithParsingError2() throws IOException {
        // Create a Java file without a package declaration
        // This is actually a parsing error in the current implementation
        Path javaFilePath = tempDir.resolve("NoPackage.java");
        String javaContent = "public class NoPackage {\n}";
        Files.writeString(javaFilePath, javaContent);

        // Create a compilation unit with the Java file
        CompilationUnitMetadata unit = new CompilationUnitMetadata(
                javaFilePath, 
                GFQNSupportedLanguages.JAVA);

        // Verify that a RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> generator.generateLanguageQualifiedName(unit, context));

        // Check if the exception is related to parsing failure
        assertTrue(exception.getMessage().contains("Failed to parse Java file"));
    }
}
