package com.sugarsaas.api.refdata;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountrySubDivisionRepository extends JpaRepository<CountrySubDivision, Integer>
{
    Optional<CountrySubDivision> findByCountryCode(String countryCode);
    Optional<CountrySubDivision> findByCountryCodeAndSubdivCode(String countryCode, String subdivCode);
}
