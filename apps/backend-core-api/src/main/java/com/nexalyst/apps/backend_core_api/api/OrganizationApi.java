package com.nexalyst.apps.backend_core_api.api;

import com.nexalyst.apps.backend_core_api.dto.OrganizationDTO;
import com.nexalyst.apps.backend_core_api.dto.OrganizationRegistrationRequest;
import com.nexalyst.apps.backend_core_api.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/organizations")
public class OrganizationApi {

    private final OrganizationService organizationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@Valid @RequestBody OrganizationRegistrationRequest request) {
        try {
            OrganizationDTO createdOrganization = organizationService.createOrganization(request);
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
}

