package com.springboot.MyTodoList.model;

public enum Role {
    ADMIN(3, "Administrator"),
    MANAGER(2, "Manager"),
    DEVELOPER(1, "Developer");

    private final int roleLevel;
    private final String roleName;

    Role(int roleLevel, String roleName) {
        this.roleLevel = roleLevel;
        this.roleName = roleName;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public String getRoleName() {
        return roleName;
    }
}