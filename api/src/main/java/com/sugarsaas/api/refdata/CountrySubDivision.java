package com.sugarsaas.api.refdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="sugar_refdata_country_subs")
public class CountrySubDivision
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="country_code")
    private String countryCode;

    @Column(name="subdivision_code")
    private String subdivCode;

    @Column(name="subdivision_name")
    private String subdivName;

    @Column(name="subdivision_type")
    private String subdivType;

    @OneToMany(mappedBy="countrySubDivision", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Set<City> cities;

    public CountrySubDivision(String countryCode, String subdivCode, String subdivName, String subdivType)  {
        this.countryCode = countryCode;
        this.subdivCode = subdivCode;
        this.subdivName = subdivName;
        this.subdivType = subdivType;
    }
}
