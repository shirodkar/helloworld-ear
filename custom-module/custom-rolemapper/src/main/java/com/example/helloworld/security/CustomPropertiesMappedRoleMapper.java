package com.example.helloworld.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CustomPropertiesMappedRoleMapper implements RoleMapper {

    private Properties roleMappings;

    public CustomPropertiesMappedRoleMapper() {
        loadRoleMappings();
    }

    private void loadRoleMappings() {
        roleMappings = new Properties();
        String rolePropertiesPath = System.getProperty("ROLE_PROPERTIES");

        if (rolePropertiesPath != null && !rolePropertiesPath.isEmpty()) {
            try (FileInputStream fis = new FileInputStream(rolePropertiesPath)) {
                roleMappings.load(fis);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load role mappings from: " + rolePropertiesPath, e);
            }
        } else {
            throw new RuntimeException("ROLE_PROPERTIES system property not set");
        }
    }

    @Override
    public Object mapRoles(Object authorizationIdentity) {
        String principal = getPrincipalName(authorizationIdentity);
        Set<String> roleSet = mapRolesForPrincipal(principal);
        return createRoles(roleSet);
    }

    private String getPrincipalName(Object authorizationIdentity) {
        try {
            Object principal = authorizationIdentity.getClass()
                    .getMethod("getPrincipal")
                    .invoke(authorizationIdentity);
            return (String) principal.getClass()
                    .getMethod("getName")
                    .invoke(principal);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract principal name", e);
        }
    }

    private Object createRoles(Set<String> roleSet) {
        try {
            Class<?> rolesClass = Class.forName("org.wildfly.security.authz.Roles");
            Method fromSetMethod = rolesClass.getMethod("fromSet", Set.class);
            return fromSetMethod.invoke(null, roleSet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Roles object", e);
        }
    }

    private Set<String> mapRolesForPrincipal(String principal) {
        Set<String> roles = new HashSet<>();

        if (principal == null || principal.isEmpty()) {
            return roles;
        }

        String rolesStr = roleMappings.getProperty(principal);
        if (rolesStr != null) {
            for (String role : rolesStr.split(",")) {
                roles.add(role.trim());
            }
        }

        return roles;
    }

    public boolean hasRole(String principal, String requiredRole) {
        return mapRolesForPrincipal(principal).contains(requiredRole);
    }

    public Set<String> getAvailableRoles() {
        Set<String> roles = new HashSet<>();
        for (Object value : roleMappings.values()) {
            for (String role : value.toString().split(",")) {
                roles.add(role.trim());
            }
        }
        return roles;
    }
}
