package com.sugarsaas.api.identity.privilege;

public enum SugarSeededPrivileges implements SeededPrivilege
{
    ADD_SUPER_ADMIN("AddSuperAdmin", "Add new super admins"),
    MANAGE_SUPER_ADMIN("ManageSuperAdmin", "Manage current super admins"),

    ADD_TENANCY_GROUP("AddTenancyGroup", "Add new tenancy group"),
    MANAGE_TENANCY_GROUP("ManageTenancyGroup", "Manage current tenancy group"),
    ADD_TENANCY("AddTenancy", "Add new tenancy to a tenancy group"),
    MANAGE_TENANCY("ManageTenancy", "Manage current tenancies in a tenancy group"),
    ADD_TENANCY_USER("AddTenancyUser", "Add new users to the tenancy except new patients"),
    MANAGE_TENANCY_USER("ManageTenancyUser", "Manage current uses in a tenancy except patients");

    public final String name;
    public final String description;
    SugarSeededPrivileges(String name, String description) { this.name = name; this.description = description; }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getOrigin()   {
        return "Sugar";
    }
}
