package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.dto.OrganizationDTO;
import com.nexalyst.apps.backend_core_api.dto.OrganizationRegistrationRequest;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationDTO createOrganization(OrganizationRegistrationRequest request) {
        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(request.name());
        entity.setDescription(request.description());

        OrganizationEntity savedEntity = organizationRepository.save(entity);

        return new OrganizationDTO(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getDescription()
        );
    }
}
