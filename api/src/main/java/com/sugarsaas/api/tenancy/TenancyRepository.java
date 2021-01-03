package com.sugarsaas.api.tenancy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TenancyRepository extends JpaRepository<Tenancy, Integer>
{
    Optional<Tenancy> findByShortName(String shortName);
    Optional<Tenancy> findByEmail(String email);
    @Query(nativeQuery=true,
            value="SELECT t.* " +
                    " FROM tenancies t, tenancy_groups tg "+
                    " WHERE tg.shortname = :tenancyGroupName AND " +
                    "   t.tenancy_group_id = tg.id ")
    Collection<Tenancy> findAllWithTenancyGroup(String tenancyGroupName);

}