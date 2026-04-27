package com.example.helloworld.security;

public class RoleMapperProvider {

    private static final CustomRoleMapper INSTANCE = new CustomRoleMapper();

    public static CustomRoleMapper getInstance() {
        return INSTANCE;
    }

    private RoleMapperProvider() {
    }
}
