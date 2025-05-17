package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CompilationUnitMetadataTest {

    @TempDir
    Path tempDir;
    
    private Path validFilePath;
    private GFQNSupportedLanguages language;

    @BeforeEach
    void setUp() throws IOException {
        // Create a valid file for testing
        validFilePath = tempDir.resolve("TestFile.java");
        String fileContent = "public class TestFile {}";
        Files.writeString(validFilePath, fileContent);
        
        // Set a language for testing
        language = GFQNSupportedLanguages.JAVA;
    }

    @Test
    void testConstructorWithValidInputs() {
        // Test constructor with valid inputs
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(validFilePath, language);
        
        // Verify that the object was created successfully
        assertNotNull(metadata);
        assertEquals(validFilePath, metadata.getPath());
        assertEquals(language, metadata.getLanguage());
    }

    @Test
    void testConstructorWithNullPath() {
        // Test constructor with null path
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new CompilationUnitMetadata(null, language));
        
        // Verify the exception message
        assertEquals("Path and language must not be null.", exception.getMessage());
    }

    @Test
    void testConstructorWithNullLanguage() {
        // Test constructor with null language
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new CompilationUnitMetadata(validFilePath, null));
        
        // Verify the exception message
        assertEquals("Path and language must not be null.", exception.getMessage());
    }

    @Test
    void testConstructorWithNonExistentFile() {
        // Create a path to a non-existent file
        Path nonExistentPath = tempDir.resolve("NonExistent.java");
        
        // Test constructor with a non-existent file
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> new CompilationUnitMetadata(nonExistentPath, language));
        
        // Verify the exception message
        assertEquals("The file does not exist: " + nonExistentPath, exception.getMessage());
    }

    @Test
    void testGetPath() {
        // Create a metadata object
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(validFilePath, language);
        
        // Test getPath method
        assertEquals(validFilePath, metadata.getPath());
    }

    @Test
    void testGetLanguage() {
        // Create a metadata object
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(validFilePath, language);
        
        // Test getLanguage method
        assertEquals(language, metadata.getLanguage());
    }

    @Test
    void testLoadContent() throws IOException {
        // Create a file with specific content
        String expectedContent = "Test content for loadContent method";
        Path contentFilePath = tempDir.resolve("ContentFile.java");
        Files.writeString(contentFilePath, expectedContent);
        
        // Create a metadata object
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(contentFilePath, language);
        
        // Test loadContent method
        String actualContent = metadata.loadContent();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testLoadContentWithIOException() throws IOException {
        // Create a file that will be deleted
        Path deletedFilePath = tempDir.resolve("DeletedFile.java");
        Files.writeString(deletedFilePath, "This file will be deleted");
        
        // Create a metadata object
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(deletedFilePath, language);
        
        // Delete the file to cause an IOException when loading content
        Files.delete(deletedFilePath);
        
        // Test loadContent method with a deleted file
        assertThrows(IOException.class, metadata::loadContent);
    }

    @Test
    void testIsValidWithValidFile() {
        // Create a metadata object with a valid file
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(validFilePath, language);
        
        // Test isValid method with a valid file
        assertTrue(metadata.isValid());
    }

    @Test
    void testIsValidWithEmptyFile() throws IOException {
        // Create an empty file
        Path emptyFilePath = tempDir.resolve("EmptyFile.java");
        Files.writeString(emptyFilePath, "");
        
        // Create a metadata object with an empty file
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(emptyFilePath, language);
        
        // Test isValid method with an empty file
        assertFalse(metadata.isValid());
    }

    @Test
    void testIsValidWithDeletedFile() throws IOException {
        // Create a file that will be deleted
        Path deletedFilePath = tempDir.resolve("AnotherDeletedFile.java");
        Files.writeString(deletedFilePath, "This file will be deleted");
        
        // Create a metadata object
        CompilationUnitMetadata metadata = new CompilationUnitMetadata(deletedFilePath, language);
        
        // Delete the file to cause an IOException when checking validity
        Files.delete(deletedFilePath);
        
        // Test isValid method with a deleted file
        assertFalse(metadata.isValid());
    }
}