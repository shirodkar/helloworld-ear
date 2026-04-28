package com.example.helloworld.security;

/**
 * Local interface matching org.wildfly.security.auth.server.RoleMapper
 * This allows us to compile without external dependencies while implementing the interface WildFly expects.
 */
public interface RoleMapper {
    Object mapRoles(Object authorizationIdentity);
}
