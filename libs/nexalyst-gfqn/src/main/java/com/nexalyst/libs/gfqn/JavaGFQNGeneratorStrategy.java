package com.nexalyst.libs.gfqn;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.nodeTypes.NodeWithName;

import java.io.File;

/**
 * This class implements the GFQNGeneratorStrategy interface for generating
 * language-qualified names (GFQNs) for Java compilation units.
 * <p>
 * The generated GFQN is based on the organization, project, repository, language,
 * file name, package name, and primary type name of the compilation unit.
 * <p>
 * The GFQN is constructed in the following format:
 * <pre>{organization}.{project}.{repository}.{language}.{fileName}.{packageName}.{primaryTypeName}</pre>
 */
public class JavaGFQNGeneratorStrategy implements GFQNGeneratorStrategy {

    /**
     * Generates a language-qualified name for a given Java compilation unit.
     * The generated name is based on the organization, project, repository, language,
     * file name, package name, and primary type name of the compilation unit.
     *
     * @param unit    The metadata of the compilation unit, which includes the path to the file
     *                and the programming language of the unit.
     * @param context The contextual information, such as organization, project, and repository,
     *                to be included in the qualified name.
     * @return A string representing the fully qualified language-specific name for the compilation unit.
     * @throws RuntimeException If the file cannot be parsed, the package name or primary type name
     *                          is not found, or any other unexpected error occurs during processing.
     */
    @Override
    public String generateLanguageQualifiedName(CompilationUnitMetadata unit, GFQNContext context) {
        // Ensure the unit is of type Java
        if (!unit.getLanguage().equals(GFQNSupportedLanguages.JAVA)) {
            throw new IllegalArgumentException("Unsupported language: " + unit.getLanguage());
        }

        if (!unit.getCompilationUnitPath().toFile().exists()) {
            throw new RuntimeException("File does not exist: " + unit.getCompilationUnitPath());
        }

        File file = unit.getCompilationUnitPath().toFile();
        CompilationUnit cu;

        try {
            cu = StaticJavaParser.parse(file);

            String packageName = cu.getPackageDeclaration()
                    .map(NodeWithName::getNameAsString)
                    .orElseThrow(() -> new RuntimeException("Package name not found in file: " + file.getAbsolutePath()));

            String primaryTypeName = cu.getPrimaryTypeName()
                    .map(String::toString)
                    .orElseThrow(() -> new RuntimeException("Primary type name not found in file: " + file.getAbsolutePath()));

            String organization = context.organization();
            String project = context.project();
            String repository = context.repository();
            String language = unit.getLanguageName();
            String fileName = file.getName().replace(".java", "");

            return String.format("%s.%s.%s.%s.%s.%s.%s", organization, project, repository, language, fileName, packageName, primaryTypeName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Java file: " + file.getAbsolutePath(), e);
        }
    }
}
