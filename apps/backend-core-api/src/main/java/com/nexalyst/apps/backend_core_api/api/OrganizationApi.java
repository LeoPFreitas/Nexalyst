package com.nexalyst.apps.backend_core_api.api;

import com.nexalyst.apps.backend_core_api.api.request.RegisterOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.request.UpdateOrganizationRequest;
import com.nexalyst.apps.backend_core_api.api.response.OrganizationId;
import com.nexalyst.apps.backend_core_api.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationApi {

    private final OrganizationService organizationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@Valid @RequestBody RegisterOrganizationRequest request) {
        try {
            OrganizationId createdOrganization = organizationService.createOrganization(request);
            return new ResponseEntity<>(createdOrganization, HttpStatus.CREATED);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {

            if (e.getMessage().contains("organizations_name_key")) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        "An organization with this name already exists"
                );
                problem.setTitle("Organization registration failed");
                problem.setType(java.net.URI.create("/errors/duplicate-organization"));
                problem.setProperty("organizationName", request.name());
                problem.setProperty("timestamp", System.currentTimeMillis());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
            }
            throw e;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody UpdateOrganizationRequest updateOrganizationRequest) {
        OrganizationId updatedOrganization = organizationService.updateOrganization(updateOrganizationRequest);
        return ResponseEntity.ok(updatedOrganization);
    }
}

