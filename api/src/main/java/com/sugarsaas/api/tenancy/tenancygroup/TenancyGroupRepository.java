package com.sugarsaas.api.tenancy.tenancygroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenancyGroupRepository extends JpaRepository<TenancyGroup, Integer>
{
    Optional<TenancyGroup> findByShortName(String shortName);
    Optional<TenancyGroup> findByEmail(String email);
}