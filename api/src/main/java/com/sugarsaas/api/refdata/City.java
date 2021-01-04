package com.sugarsaas.api.refdata;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="sugar_refdata_country_cities")
public class City
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="city_name")
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="sub_id", nullable=false)
    private CountrySubDivision countrySubDivision;

    public City(Integer id, String name)  {
        this.id = id;
        this.name = name;
    }
}