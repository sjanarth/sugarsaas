package com.sugarsaas.api.tenancy.tenancygroup;

import com.sugarsaas.api.core.AuditableEntity;
import com.sugarsaas.api.core.SupportsAttachments;
import com.sugarsaas.api.core.SupportsPreferences;
import com.sugarsaas.api.identity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name="sugar_tenancy_groups")
@SupportsPreferences
@SupportsAttachments
public class TenancyGroup extends AuditableEntity
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

    @Column(name="active") //, columnDefinition="BIT(1)")
    private boolean active = true;

    /*
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinTable(name="tenancies",
            joinColumns={@JoinColumn(name="tenancy_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="tenancy_group_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<Tenancy> tenancies;
    */

    /*
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinTable(name="tenancy_group_users",
            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="tenancy_group_id", referencedColumnName="id", nullable=false, updatable=false)})
    */
    @ManyToMany(mappedBy="tenancyGroups")
    private Set<User> users;
}