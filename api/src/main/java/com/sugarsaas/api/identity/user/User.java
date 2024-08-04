package com.sugarsaas.api.identity.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sugarsaas.api.core.AuditableEntity;
import com.sugarsaas.api.core.SupportsAttachments;
import com.sugarsaas.api.core.SupportsPreferences;
import com.sugarsaas.api.core.SupportsTags;
import com.sugarsaas.api.identity.role.Role;
import com.sugarsaas.api.tenancy.tenancygroup.TenancyGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="sugar_users")
@SupportsTags
@SupportsAttachments
@SupportsPreferences
public class User extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="email")
    private String email;

    @Column(name="password") //@JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String passWord;

    @Column(name="active") //, columnDefinition="BIT(1)")
    private boolean active = true;

    /*
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="sugar_user_privileges",
            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="privilege_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<Privilege> privileges;
     */

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="sugar_user_roles",
            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<Role> roles = new HashSet<>();

    /*
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="sugar_tenancy_users",
            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="tenancy_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<Tenancy> tenancies;
     */

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name="sugar_tenancy_group_users",
            joinColumns={@JoinColumn(name="user_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="tenancy_group_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<TenancyGroup> tenancyGroups = new HashSet<>();
}