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

        // Create a Python file for testing
        Path pythonFilePath = tempDir.resolve("example.py");
        String pythonContent = "def hello():\n    print('Hello, World!')\n";
        Files.writeString(pythonFilePath, pythonContent);

        // Create a Python compilation unit
        pythonUnit = new CompilationUnitMetadata(pythonFilePath, GFQNSupportedLanguages.PYTHON);
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
                String language = unit.getLanguage().getLanguage();
                String fileName = unit.getPath().getFileName().toString().replace(".py", "");
                String moduleName = "hello"; // Simulated module name extraction

                // Format similar to Java GFQN but with module name instead of package and class
                return String.format("%s.%s.%s.%s.%s.%s", organization, project, repository, language, fileName, moduleName);
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
        assertEquals("nexalyst.libs.gfqn.python.example.hello", gfqn);
    }

    @Test
    void testDefaultConstructor() {
        // Test that the default constructor registers the JavaGFQNGeneratorStrategy
        // by generating a GFQN for Java without registering any strategies
        String gfqn = gfqnService.generateGFQN(javaUnit, context);

        // Expected format: {organization}.{project}.{repository}.{language}.{fileName}.{packageName}.{primaryTypeName}
        String expected = "nexalyst.libs.gfqn.java.Example.com.example.Example";
        assertEquals(expected, gfqn);
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
