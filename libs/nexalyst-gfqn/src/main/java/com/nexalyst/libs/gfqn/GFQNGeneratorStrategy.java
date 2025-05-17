package com.nexalyst.libs.gfqn;

import java.util.Set;

/**
 * GFQNGeneratorStrategy defines the contract for generating globally fully qualified
 * names (GFQN) for compilation units in the context of a specific programming language.
 * Implementations of this interface provide language-specific strategies to generate
 * names based on the compilation unit metadata and contextual information.
 * <p>
 * This interface is typically used in systems where source code artifacts need to be
 * uniquely identified and referenced across organizations, projects, and repositories.
 */
public interface GFQNGeneratorStrategy {

    /**
     * Generate a language-qualified name for the given compilation unit.
     *
     * @param unit    The compilation unit to generate the name for.
     * @param context The context containing organization, project, and repository information.
     * @return The generated language-qualified name.
     */
    String generateLanguageQualifiedName(CompilationUnitMetadata unit, GFQNContext context);

    /**
     * Get the supported programming language for this generator.
     *
     * @return The supported language.
     */
    default Set<GFQNSupportedLanguages> getSupportedLanguage() {
        return Set.of(GFQNSupportedLanguages.JAVA);
    }
}
