package com.nexalyst.libs.gfqn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GFQNContextTest {

    @Test
    void testConstructorAndAccessors() {
        // Test constructor and accessor methods
        String organization = "nexalyst";
        String project = "project";
        String system = "system";
        String repository = "repository";
        
        GFQNContext context = new GFQNContext(organization, project, system, repository);
        
        // Test accessor methods
        assertEquals(organization, context.organization());
        assertEquals(project, context.project());
        assertEquals(system, context.system());
        assertEquals(repository, context.repository());
    }
    
    @Test
    void testEquality() {
        // Test equality of two identical contexts
        GFQNContext context1 = new GFQNContext("nexalyst", "project", "system", "repository");
        GFQNContext context2 = new GFQNContext("nexalyst", "project", "system", "repository");
        
        // Test equals method
        assertEquals(context1, context2);
        
        // Test hashCode method
        assertEquals(context1.hashCode(), context2.hashCode());
    }
    
    @Test
    void testInequality() {
        // Test inequality of two different contexts
        GFQNContext context1 = new GFQNContext("nexalyst", "project1", "system", "repository");
        GFQNContext context2 = new GFQNContext("nexalyst", "project2", "system", "repository");
        
        // Test not equals
        assertNotEquals(context1, context2);
        
        // Test different hashCodes
        assertNotEquals(context1.hashCode(), context2.hashCode());
    }
    
    @Test
    void testToString() {
        // Test toString method
        GFQNContext context = new GFQNContext("nexalyst", "project", "system", "repository");
        
        // Test that toString contains all field values
        String toString = context.toString();
        assertTrue(toString.contains("nexalyst"));
        assertTrue(toString.contains("project"));
        assertTrue(toString.contains("system"));
        assertTrue(toString.contains("repository"));
    }
    
    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Condition is false");
        }
    }
}