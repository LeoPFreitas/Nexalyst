package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.api.request.RegisterOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.response.OrganizationId;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.exceptions.DuplicateOrganizationNameException;
import com.nexalyst.apps.backend_core_api.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationService(organizationRepository);
    }

    @Test
    void createOrganization_ShouldReturnOrganizationDTO_WhenValidRequestProvided() {
        // Arrange
        String name = "Test Organization";
        String description = "Test Description";
        RegisterOrganizationRequest request = new RegisterOrganizationRequest(name, description);

        UUID id = UUID.randomUUID();
        OrganizationEntity savedEntity = new OrganizationEntity(id, name, description);

        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(savedEntity);

        // Act
        OrganizationId result = organizationService.createOrganization(request);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.organizationId());

        verify(organizationRepository, times(1)).save(any(OrganizationEntity.class));
    }

    @Test
    void updateOrganization_ShouldReturnUpdatedOrganizationDTO_WhenValidRequestProvided() {
        // Arrange
        UUID id = UUID.randomUUID();
        String originalName = "Original Name";
        String originalDescription = "Original Description";

        String updatedName = "Updated Name";
        String updatedDescription = "Updated Description";

        OrganizationEntity existingEntity = new OrganizationEntity(id, originalName, originalDescription);
        OrganizationEntity updatedEntity = new OrganizationEntity(id, updatedName, updatedDescription);

        UpdateOrganizationRequest request = new UpdateOrganizationRequest(id, updatedName, updatedDescription);

        when(organizationRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(organizationRepository.existsByName(updatedName)).thenReturn(false);
        when(organizationRepository.save(any(OrganizationEntity.class))).thenReturn(updatedEntity);

        // Act
        OrganizationId result = organizationService.updateOrganization(request);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.organizationId());

        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, times(1)).existsByName(updatedName);
        verify(organizationRepository, times(1)).save(any(OrganizationEntity.class));
    }

    @Test
    void updateOrganization_ShouldThrowDuplicateOrganizationNameException_WhenNameAlreadyExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        String originalName = "Original Name";
        String originalDescription = "Original Description";

        String duplicateName = "Duplicate Name";
        String updatedDescription = "Updated Description";

        OrganizationEntity existingEntity = new OrganizationEntity(id, originalName, originalDescription);

        UpdateOrganizationRequest request = new UpdateOrganizationRequest(id, duplicateName, updatedDescription);

        when(organizationRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(organizationRepository.existsByName(duplicateName)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateOrganizationNameException.class, () -> organizationService.updateOrganization(request));

        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, times(1)).existsByName(duplicateName);
        verify(organizationRepository, never()).save(any(OrganizationEntity.class));
    }

    @Test
    void updateOrganization_ShouldThrowRuntimeException_WhenOrganizationNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        String updatedName = "Updated Name";
        String updatedDescription = "Updated Description";

        UpdateOrganizationRequest request = new UpdateOrganizationRequest(id, updatedName, updatedDescription);

        when(organizationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> organizationService.updateOrganization(request));

        verify(organizationRepository, times(1)).findById(id);
        verify(organizationRepository, never()).existsByName(anyString());
        verify(organizationRepository, never()).save(any(OrganizationEntity.class));
    }
}
