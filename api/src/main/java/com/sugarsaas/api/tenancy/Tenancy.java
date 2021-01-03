package com.sugarsaas.api.tenancy;

import com.sugarsaas.api.core.AuditableEntity;
import com.sugarsaas.api.core.SupportsAttachments;
import com.sugarsaas.api.core.SupportsPreferences;
import com.sugarsaas.api.tenancy.tenancygroup.TenancyGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="sugar_tenancies")
@SupportsPreferences
@SupportsAttachments
public class Tenancy extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="shortname")
    private String shortName;

    @Column(name="longname")
    private String longName;

    @Column(name="email")
    private String email;

    @Column(name="active", columnDefinition="BIT(1)")
    private boolean active = true;

    @JoinColumn(name="tenancy_group_id")
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    private TenancyGroup tenancyGroup;

    //@ManyToMany(mappedBy="tenancies")
    //private Set<User> users;
}