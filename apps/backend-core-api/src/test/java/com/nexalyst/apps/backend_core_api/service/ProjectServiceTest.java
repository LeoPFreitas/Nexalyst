package com.nexalyst.apps.backend_core_api.service;

import com.nexalyst.apps.backend_core_api.api.request.CreateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.response.OrganizationId;
import com.nexalyst.apps.backend_core_api.api.response.ProjectId;
import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import com.nexalyst.apps.backend_core_api.entity.ProjectEntity;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(organizationRepository, projectRepository);
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
        ProjectId result = projectService.createProject(request);

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
        assertThrows(OrganizationNotFoundException.class, () -> projectService.createProject(request));

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
        assertThrows(DuplicatedProjectNameException.class, () -> projectService.createProject(request));

        verify(organizationRepository, times(1)).findById(organizationId);
        verify(projectRepository, times(1)).existsByNameAndOrganizationId(projectName, organizationId);
        verify(projectRepository, never()).save(any(ProjectEntity.class));
    }

    @Test
    void updateProject_ShouldReturnProjectId_WhenValidRequestProvided() {
        // Arrange
        Integer projectId = 1;
        String originalName = "Original Project";
        String originalDescription = "Original Description";
        String updatedName = "Updated Project";
        String updatedDescription = "Updated Description";

        UUID organizationId = UUID.randomUUID();
        OrganizationEntity organizationEntity = new OrganizationEntity(organizationId, "Test Organization", "Test Description", new LinkedHashSet<>());

        ProjectEntity existingProject = new ProjectEntity();
        existingProject.setId(projectId);
        existingProject.setName(originalName);
        existingProject.setDescription(originalDescription);
        existingProject.setOrganization(organizationEntity);

        ProjectEntity updatedProject = new ProjectEntity();
        updatedProject.setId(projectId);
        updatedProject.setName(updatedName);
        updatedProject.setDescription(updatedDescription);
        updatedProject.setOrganization(organizationEntity);

        UpdateProjectRequest request = new UpdateProjectRequest(projectId, updatedName, updatedDescription);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.existsByNameAndOrganizationId(updatedName, organizationId)).thenReturn(false);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(updatedProject);

        // Act
        ProjectId result = projectService.updateProject(request);

        // Assert
        assertNotNull(result);
        assertEquals(projectId, result.projectId());

        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).existsByNameAndOrganizationId(updatedName, organizationId);
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void updateProject_ShouldThrowOrganizationNotFoundException_WhenProjectNotFound() {
        // Arrange
        Integer projectId = 1;
        String updatedName = "Updated Project";
        String updatedDescription = "Updated Description";

        UpdateProjectRequest request = new UpdateProjectRequest(projectId, updatedName, updatedDescription);

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrganizationNotFoundException.class, () -> projectService.updateProject(request));

        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).existsByNameAndOrganizationId(anyString(), any(UUID.class));
        verify(projectRepository, never()).save(any(ProjectEntity.class));
    }

    @Test
    void updateProject_ShouldThrowDuplicatedProjectNameException_WhenProjectNameAlreadyExists() {
        // Arrange
        Integer projectId = 1;
        String originalName = "Original Project";
        String originalDescription = "Original Description";
        String updatedName = "Updated Project";
        String updatedDescription = "Updated Description";

        UUID organizationId = UUID.randomUUID();
        OrganizationEntity organizationEntity = new OrganizationEntity(organizationId, "Test Organization", "Test Description", new LinkedHashSet<>());

        ProjectEntity existingProject = new ProjectEntity();
        existingProject.setId(projectId);
        existingProject.setName(originalName);
        existingProject.setDescription(originalDescription);
        existingProject.setOrganization(organizationEntity);

        UpdateProjectRequest request = new UpdateProjectRequest(projectId, updatedName, updatedDescription);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.existsByNameAndOrganizationId(updatedName, organizationId)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedProjectNameException.class, () -> projectService.updateProject(request));

        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).existsByNameAndOrganizationId(updatedName, organizationId);
        verify(projectRepository, never()).save(any(ProjectEntity.class));
    }
}
