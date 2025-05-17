package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.api.request.CreateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.response.ProjectId;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.entity.ProjectEntity;
import com.nexalyst.apps.backend_core_api.exceptions.DuplicatedProjectNameException;
import com.nexalyst.apps.backend_core_api.exceptions.OrganizationNotFoundException;
import com.nexalyst.apps.backend_core_api.repository.OrganizationRepository;
import com.nexalyst.apps.backend_core_api.repository.ProjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectId createProject(CreateProjectRequest createProjectRequest) throws DuplicatedProjectNameException {
        OrganizationEntity organizationEntity = organizationRepository.findById(createProjectRequest.organizationId().organizationId())
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Organization not found with ID: " + createProjectRequest.organizationId().organizationId()
                ));

        boolean projectExists = projectRepository.existsByNameAndOrganizationId(
                createProjectRequest.name(), organizationEntity.getId());
        if (projectExists) {
            throw new DuplicatedProjectNameException("Project with this name already exists: " + createProjectRequest.name());
        }

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(createProjectRequest.name());
        projectEntity.setDescription(createProjectRequest.description());
        projectEntity.setOrganization(organizationEntity);

        ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);

        return new ProjectId(savedProjectEntity.getId());
    }

    public ProjectId updateProject(@Valid UpdateProjectRequest updateProjectRequest) {
        ProjectEntity projectEntity = projectRepository.findById(updateProjectRequest.projectId())
                .orElseThrow(() -> new OrganizationNotFoundException(
                        "Project not found with ID: " + updateProjectRequest.projectId()
                ));

        boolean projectExists = projectRepository.existsByNameAndOrganizationId(
                updateProjectRequest.name(), projectEntity.getOrganization().getId());

        if (projectExists && !projectEntity.getName().equals(updateProjectRequest.name())) {
            throw new DuplicatedProjectNameException("Project with this name already exists: " + updateProjectRequest.name());
        }

        projectEntity.setDescription(updateProjectRequest.description());
        ProjectEntity updatedProjectEntity = projectRepository.save(projectEntity);
        return new ProjectId(updatedProjectEntity.getId());

    }
}
