package com.sugarsaas.api.identity.role;

import com.sugarsaas.api.identity.privilege.SeededPrivilege;

import java.util.List;

public interface SeededRole
{
    String getName();
    String getDescription();
    List<SeededPrivilege> getPrivileges();
    String getOrigin();
}
