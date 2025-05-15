package com.nexalyst.apps.backend_core_api.repository;

import com.nexalyst.apps.backend_core_api.entity.ProjectEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer>, JpaSpecificationExecutor<ProjectEntity> {
    boolean existsByNameAndOrganizationId(@NotNull String name, UUID id);
}