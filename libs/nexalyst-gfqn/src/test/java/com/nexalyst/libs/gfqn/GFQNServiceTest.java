package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GFQNServiceTest {

    private GFQNService gfqnService;
    private GFQNContext context;
    private CompilationUnitMetadata javaUnit;
    private CompilationUnitMetadata pythonUnit;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        // Initialize the service
        gfqnService = new GFQNService();

        // Create a context
        context = new GFQNContext("nexalyst", "libs", "gfqn");

        // Create a Java file for testing
        Path javaFilePath = tempDir.resolve("Example.java");
        String javaContent = "package com.example;\n\npublic class Example {\n}";
        Files.writeString(javaFilePath, javaContent);

        // Create a Java compilation unit
        javaUnit = new CompilationUnitMetadata(javaFilePath, GFQNSupportedLanguages.JAVA);

        // Create a Python compilation unit (file doesn't need to exist for this test)
        pythonUnit = new CompilationUnitMetadata(
                Path.of("/path/to/example.py"), 
                GFQNSupportedLanguages.PYTHON);

        // Note: JavaGFQNGeneratorStrategy is already registered in the GFQNService constructor
    }

    @Test
    void testRegisterStrategy() {
        // Create a mock strategy for Python
        GFQNGeneratorStrategy pythonStrategy = new GFQNGeneratorStrategy() {
            @Override
            public String generateLanguageQualifiedName(CompilationUnitMetadata unit, GFQNContext context) {
                // Return a mock GFQN for Python
                String organization = context.organization();
                String project = context.project();
                String repository = context.repository();
                String language = unit.getLanguageName();
                String fileName = unit.getCompilationUnitPath().getFileName().toString().replace(".py", "");

                return String.format("%s.%s.%s.%s.%s", organization, project, repository, language, fileName);
            }

            @Override
            public Set<GFQNSupportedLanguages> getSupportedLanguage() {
                return Set.of(GFQNSupportedLanguages.PYTHON);
            }
        };

        // Register the strategy
        gfqnService.registerStrategy(pythonStrategy);

        // Test that the strategy works
        String gfqn = gfqnService.generateGFQN(pythonUnit, context);
        assertEquals("nexalyst.libs.gfqn.python.example", gfqn);
    }

    @Test
    void testGenerateGFQN() {
        // Test generating a GFQN for Java
        String gfqn = gfqnService.generateGFQN(javaUnit, context);

        // Expected format: {organization}.{project}.{repository}.{language}.{fileName}.{packageName}.{primaryTypeName}
        String expected = "nexalyst.libs.gfqn.java.Example.com.example.Example";
        assertEquals(expected, gfqn);
    }

    @Test
    void testUnsupportedLanguage() {
        // Test that an exception is thrown for an unsupported language
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            gfqnService.generateGFQN(pythonUnit, context);
        });

        String expectedMessage = "No GFQN strategy found for language: PYTHON";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
