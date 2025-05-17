package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GFQNSupportedLanguagesTest {

    @Test
    void testJavaLanguage() {
        assertEquals("java", GFQNSupportedLanguages.JAVA.getLanguage());
    }

    @Test
    void testPythonLanguage() {
        assertEquals("python", GFQNSupportedLanguages.PYTHON.getLanguage());
    }

    @Test
    void testJavaScriptLanguage() {
        assertEquals("javascript", GFQNSupportedLanguages.JAVASCRIPT.getLanguage());
    }

    @Test
    void testCSharpLanguage() {
        assertEquals("csharp", GFQNSupportedLanguages.CSHARP.getLanguage());
    }

    @Test
    void testRubyLanguage() {
        assertEquals("ruby", GFQNSupportedLanguages.RUBY.getLanguage());
    }

    @Test
    void testPhpLanguage() {
        assertEquals("php", GFQNSupportedLanguages.PHP.getLanguage());
    }

    @Test
    void testGoLanguage() {
        assertEquals("go", GFQNSupportedLanguages.GO.getLanguage());
    }

    @Test
    void testSwiftLanguage() {
        assertEquals("swift", GFQNSupportedLanguages.SWIFT.getLanguage());
    }
}