package com.nexalyst.apps.backend_core_api.api;

import com.nexalyst.apps.backend_core_api.api.request.CreateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateProjectRequest;
import com.nexalyst.apps.backend_core_api.api.response.ProjectId;
import com.nexalyst.apps.backend_core_api.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectApi {

    private final ProjectService projectService;

    @PostMapping("/createProject")
    public ResponseEntity<?> createProject(@Valid @RequestBody CreateProjectRequest createProjectRequest) {
        ProjectId createdProject = projectService.createProject(createProjectRequest);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/updateProject")
    public ResponseEntity<?> updateProject(@Valid @RequestBody UpdateProjectRequest updateProjectRequest) {
        ProjectId updatedProject = projectService.updateProject(updateProjectRequest);
        return ResponseEntity.ok(updatedProject);
    }

}

