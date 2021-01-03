package com.sugarsaas.api.identity.role;

import com.sugarsaas.api.identity.privilege.SeededPrivilege;
import com.sugarsaas.api.identity.privilege.SugarSeededPrivileges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum SugarSeededRoles implements SeededRole
{
    SUPER_ADMIN("SuperAdmin", "God of everything",
            Arrays.asList(SugarSeededPrivileges.values())),
    TENANCY_GROUP_ADMIN("TenancyGroupAdmin", "God of a tenancy group",
            new ArrayList<>(Arrays.asList(
                new SugarSeededPrivileges[] {
                        SugarSeededPrivileges.MANAGE_TENANCY_GROUP,
                        SugarSeededPrivileges.ADD_TENANCY,
                        SugarSeededPrivileges.MANAGE_TENANCY,
                        SugarSeededPrivileges.ADD_TENANCY_USER,
                        SugarSeededPrivileges.MANAGE_TENANCY_USER
                }))),
    TENANCY_ADMIN("TenancyAdmin", "God of a tenancy",
            new ArrayList<>(Arrays.asList(
                new SugarSeededPrivileges[] {
                        SugarSeededPrivileges.MANAGE_TENANCY,
                        SugarSeededPrivileges.ADD_TENANCY_USER,
                        SugarSeededPrivileges.MANAGE_TENANCY_USER
                })));

    public String name;
    public String description;
    public List<SeededPrivilege> privileges;
    SugarSeededRoles(String name, String description, List<SeededPrivilege> privileges) {
        this.name = name;
        this.description = description;
        this.privileges = privileges;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<SeededPrivilege> getPrivileges() {
        return privileges;
    }

    @Override
    public String getOrigin() { return "Sugar"; }
}