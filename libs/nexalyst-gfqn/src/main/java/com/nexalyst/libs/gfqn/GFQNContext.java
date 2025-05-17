package com.nexalyst.libs.gfqn;

public class GFQNContext {
    private final String organization;
    private final String project;
    private final String repository;

    public GFQNContext(String organization, String project, String repository) {
        this.organization = organization;
        this.project = project;
        this.repository = repository;
    }

    public String getOrganization() {
        return organization;
    }

    public String getProject() {
        return project;
    }

    public String getRepository() {
        return repository;
    }
}
