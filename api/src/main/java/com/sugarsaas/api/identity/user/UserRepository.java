package com.sugarsaas.api.identity.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>
{
    Optional<User> findByEmail(String email);

    /*
   @Query(nativeQuery=true,
            value="SELECT u.* " +
                " FROM users u, privileges p, user_privileges up "+
                " WHERE p.name = :privilegeName AND " +
                "   up.privilege_id = p.id AND " +
                "   up.user_id = u.id ")

    Collection<User> findAllWithPrivilege(String privilegeName);

    @Query(nativeQuery=true,
            value="SELECT u.* " +
                    " FROM users u, roles r, user_roles ru "+
                    " WHERE r.name = :roleName AND " +
                    "   ru.role_id = r.id AND " +
                    "   ru.user_id = u.id ")

    Collection<User> findAllWithRole(String roleName);

     */
}