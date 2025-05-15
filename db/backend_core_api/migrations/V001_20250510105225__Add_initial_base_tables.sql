CREATE TABLE organizations
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE projects
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    organization_id UUID REFERENCES organizations (id) ON DELETE CASCADE,
    description     TEXT,
    UNIQUE (organization_id, name)
);

CREATE TABLE systems
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    project_id  INTEGER REFERENCES projects (id) ON DELETE CASCADE,
    description TEXT
);

CREATE TABLE repositories
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    system_id   INTEGER REFERENCES systems (id) ON DELETE CASCADE,
    description TEXT
);