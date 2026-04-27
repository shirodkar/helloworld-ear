package com.example.helloworld.security;

public class RoleMapperProvider {

    private static final CustomPropertiesMappedRoleMapper INSTANCE = new CustomPropertiesMappedRoleMapper();

    public static CustomPropertiesMappedRoleMapper getInstance() {
        return INSTANCE;
    }

    private RoleMapperProvider() {
    }
}
