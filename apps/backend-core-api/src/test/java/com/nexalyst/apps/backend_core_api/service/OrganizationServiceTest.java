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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private ProjectRepository projectRepository;

    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationService = new OrganizationService(organizationRepository, projectRepository);
    }

    @Test
    void createOrganization_ShouldReturnOrganizationDTO_WhenValidRequestProvided() {
        // Arrange
        String name = "Test Organization";
        String description = "Test Description";
        RegisterOrganizationRequest request = new RegisterOrganizationRequest(name, description);

        UUID id = UUID.randomUUID();
        OrganizationEntity savedEntity = new OrganizationEntity(id, name, description, new LinkedHashSet<>());

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

        OrganizationEntity existingEntity = new OrganizationEntity(id, originalName, originalDescription, new LinkedHashSet<>());
        OrganizationEntity updatedEntity = new OrganizationEntity(id, updatedName, updatedDescription, new LinkedHashSet<>());

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

        OrganizationEntity existingEntity = new OrganizationEntity(id, originalName, originalDescription, new LinkedHashSet<>());

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

    @Test
    void createProject_ShouldReturnProjectId_WhenValidRequestProvided() {
        // Arrange
        UUID organizationId = UUID.randomUUID();
        String projectName = "Test Project";
        String projectDescription = "Test Project Description";

        OrganizationId orgId = new OrganizationId(organizationId);
        CreateProjectRequest request = new CreateProjectRequest(orgId, projectName, projectDescription);

        OrganizationEntity organizationEntity = new OrganizationEntity(organizationId, "Test Organization", "Test Description", new LinkedHashSet<>());

        ProjectEntity savedProjectEntity = new ProjectEntity();
        savedProjectEntity.setId(1);
        savedProjectEntity.setName(projectName);
        savedProjectEntity.setDescription(projectDescription);
        savedProjectEntity.setOrganization(organizationEntity);

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organizationEntity));
        when(projectRepository.existsByNameAndOrganizationId(projectName, organizationId)).thenReturn(false);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(savedProjectEntity);

        // Act
        ProjectId result = organizationService.createProject(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.projectId());

        verify(organizationRepository, times(1)).findById(organizationId);
        verify(projectRepository, times(1)).existsByNameAndOrganizationId(projectName, organizationId);
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void createProject_ShouldThrowOrganizationNotFoundException_WhenOrganizationNotFound() {
        // Arrange
        UUID organizationId = UUID.randomUUID();
        String projectName = "Test Project";
        String projectDescription = "Test Project Description";

        OrganizationId orgId = new OrganizationId(organizationId);
        CreateProjectRequest request = new CreateProjectRequest(orgId, projectName, projectDescription);

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> organizationService.createProject(request));

        verify(organizationRepository, times(1)).findById(organizationId);
        verify(projectRepository, never()).existsByNameAndOrganizationId(anyString(), any(UUID.class));
        verify(projectRepository, never()).save(any(ProjectEntity.class));
    }

    @Test
    void createProject_ShouldThrowDuplicatedProjectNameException_WhenProjectNameAlreadyExists() {
        // Arrange
        UUID organizationId = UUID.randomUUID();
        String projectName = "Test Project";
        String projectDescription = "Test Project Description";

        OrganizationId orgId = new OrganizationId(organizationId);
        CreateProjectRequest request = new CreateProjectRequest(orgId, projectName, projectDescription);

        OrganizationEntity organizationEntity = new OrganizationEntity(organizationId, "Test Organization", "Test Description", new LinkedHashSet<>());

        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organizationEntity));
        when(projectRepository.existsByNameAndOrganizationId(projectName, organizationId)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedProjectNameException.class, () -> organizationService.createProject(request));

        verify(organizationRepository, times(1)).findById(organizationId);
        verify(projectRepository, times(1)).existsByNameAndOrganizationId(projectName, organizationId);
        verify(projectRepository, never()).save(any(ProjectEntity.class));
    }
}
