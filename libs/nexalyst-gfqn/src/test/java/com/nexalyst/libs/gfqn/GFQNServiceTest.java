package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GFQNServiceTest {

    private GFQNService gfqnService;
    private GFQNContext context;
    private CompilationUnit javaUnit;
    private CompilationUnit pythonUnit;

    @BeforeEach
    void setUp() {
        // Initialize the service
        gfqnService = new GFQNService();

        // Create a context
        context = new GFQNContext("nexalyst", "libs", "gfqn");

        // Create a Java compilation unit
        Map<String, String> javaMetadata = new HashMap<>();
        javaMetadata.put("package", "com.example");
        javaUnit = new CompilationUnit("/path/to/Example.java", "Example", GFQNSupportedLanguages.JAVA, javaMetadata);

        // Create a Python compilation unit
        Map<String, String> pythonMetadata = new HashMap<>();
        pythonUnit = new CompilationUnit("/path/to/example.py", "example", GFQNSupportedLanguages.PYTHON, pythonMetadata);

        // Note: JavaGFQNGeneratorStrategy is already registered in the GFQNService constructor
    }

    @Test
    void testRegisterStrategy() {
        // Create a mock strategy for Python
        GFQNGeneratorStrategy pythonStrategy = new GFQNGeneratorStrategy() {
            @Override
            public String generateLanguageQualifiedName(CompilationUnit unit, GFQNContext context) {
                return unit.getName();
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
        assertEquals("example", gfqn);
    }

    @Test
    void testGenerateGFQN() {
        // Test generating a GFQN for Java
        String gfqn = gfqnService.generateGFQN(javaUnit, context);
        assertEquals("com.example.Example", gfqn);
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
