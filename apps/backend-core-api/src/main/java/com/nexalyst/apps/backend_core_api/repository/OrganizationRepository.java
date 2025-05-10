package com.nexalyst.apps.backend_core_api.repository;

import com.nexalyst.apps.backend_core_api.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID>, JpaSpecificationExecutor<OrganizationEntity> {
}