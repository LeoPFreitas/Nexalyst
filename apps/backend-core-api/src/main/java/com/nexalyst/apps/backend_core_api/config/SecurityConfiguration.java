package com.nexalyst.apps.backend_core_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/organizations/register").hasAuthority("SCOPE_organization:registerOrganization")
                        .requestMatchers("/api/v1/organizations/update").hasAuthority("SCOPE_organization:updateOrganization")
                        .requestMatchers("/api/v1/organizations/createProject").hasAuthority("SCOPE_organization:createProject")
                        .anyRequest().denyAll()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
