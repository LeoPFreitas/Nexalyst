package com.nexalyst.apps.backend_core_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "organizations")
@NamedQueries({
        @NamedQuery(name = "Organization.findAll", query = "SELECT o FROM OrganizationEntity o"),
        @NamedQuery(name = "Organization.findById", query = "SELECT o FROM OrganizationEntity o WHERE o.id = :id"),
        @NamedQuery(name = "Organization.findByName", query = "SELECT o FROM OrganizationEntity o WHERE o.name = :name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "description")
    private String description;
}