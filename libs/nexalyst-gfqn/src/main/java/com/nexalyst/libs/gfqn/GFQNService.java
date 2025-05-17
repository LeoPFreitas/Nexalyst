package com.nexalyst.libs.gfqn;

import java.util.HashMap;
import java.util.Map;

public class GFQNService {

    private final Map<GFQNSupportedLanguages, GFQNGeneratorStrategy> strategyRegistry = new HashMap<>();

    public GFQNService() {
        registerStrategy(new JavaGFQNGeneratorStrategy());
    }

    public void registerStrategy(GFQNGeneratorStrategy strategy) {
        for (GFQNSupportedLanguages language : strategy.getSupportedLanguage()) {
            strategyRegistry.put(language, strategy);
        }
    }

    public String generateGFQN(CompilationUnitMetadata unit, GFQNContext context) {
        GFQNSupportedLanguages language = unit.getLanguage();

        GFQNGeneratorStrategy strategy = strategyRegistry.get(language);
        if (strategy == null) {
            throw new UnsupportedOperationException("No GFQN strategy found for language: " + language);
        }

        return strategy.generateLanguageQualifiedName(unit, context);
    }
}