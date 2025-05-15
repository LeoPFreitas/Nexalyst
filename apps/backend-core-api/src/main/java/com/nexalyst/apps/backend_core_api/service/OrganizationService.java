package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.api.request.CreateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.request.RegisterOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.response.OrganizationId;
import com.nexalyst.apps.backend_core_api.api.response.ProjectId;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.entity.ProjectEntity;
import com.nexalyst.apps.backend_core_api.exceptions.DuplicateOrganizationNameException;
import com.nexalyst.apps.backend_core_api.exceptions.DuplicatedProjectNameException;
import com.nexalyst.apps.backend_core_api.exceptions.OrganizationNotFoundException;
import com.nexalyst.apps.backend_core_api.repository.OrganizationRepository;
import com.nexalyst.apps.backend_core_api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;

    public OrganizationId createOrganization(RegisterOrganizationRequest request) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(request.name());
        entity.setDescription(request.description());

        OrganizationEntity savedEntity = organizationRepository.save(entity);

        return new OrganizationId(savedEntity.getId());
    }


    public OrganizationId updateOrganization(UpdateOrganizationRequest updateOrganizationRequest) {
        OrganizationEntity entity = organizationRepository.findById(updateOrganizationRequest.organizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        if (updateOrganizationRequest.name() != null && !updateOrganizationRequest.name().equals(entity.getName())) {
            if (organizationRepository.existsByName(updateOrganizationRequest.name())) {

                throw new DuplicateOrganizationNameException(
                        "An organization with this name already exists: " + updateOrganizationRequest.name()
                );

            }

            entity.setName(updateOrganizationRequest.name());
        }
        entity.setDescription(updateOrganizationRequest.description());

        OrganizationEntity updatedEntity = organizationRepository.save(entity);

        return new OrganizationId(updatedEntity.getId());
    }

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
}
