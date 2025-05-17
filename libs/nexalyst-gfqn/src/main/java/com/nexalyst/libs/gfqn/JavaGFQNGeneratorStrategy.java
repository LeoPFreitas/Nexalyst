package com.nexalyst.libs.gfqn;

public class JavaGFQNGeneratorStrategy implements GFQNGeneratorStrategy {
    @Override
    public String generateLanguageQualifiedName(CompilationUnit unit, GFQNContext context) {

        String packageName = unit.getMetadata().getOrDefault("package", "");
        return packageName.isEmpty() ? unit.getName() : packageName + "." + unit.getName();
    }
}
