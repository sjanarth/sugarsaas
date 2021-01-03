package com.sugarsaas.api.identity.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer>
{
    Optional<Role> findByName(String name);
    @Query(nativeQuery=true,
            value="SELECT r.* " +
                " FROM roles r, privileges p, role_privileges rp "+
                " WHERE p.name = :privilegeName AND " +
                "   rp.privilege_id = p.id AND " +
                "   rp.role_id = r.id ")
    Collection<Role> findAllWithPrivilege(String privilegeName);
}