package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.api.request.RegisterOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.response.RegisterOrganizationResponse;
import com.nexalyst.apps.backend_core_api.api.response.UpdateOrganizationResponse;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.exceptions.DuplicateOrganizationNameException;
import com.nexalyst.apps.backend_core_api.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public RegisterOrganizationResponse createOrganization(RegisterOrganizationRequest request) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(request.name());
        entity.setDescription(request.description());

        OrganizationEntity savedEntity = organizationRepository.save(entity);

        return new RegisterOrganizationResponse(savedEntity.getId());
    }


    public UpdateOrganizationResponse updateOrganization(UpdateOrganizationRequest updateOrganizationRequest) {
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

        return new UpdateOrganizationResponse(updatedEntity.getId());
    }
}
