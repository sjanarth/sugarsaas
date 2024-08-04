package com.sugarsaas.api.identity.role;

import com.sugarsaas.api.core.AuditableEntity;
import com.sugarsaas.api.identity.privilege.Privilege;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name="sugar_roles")
public class Role extends AuditableEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="seeded") //, columnDefinition="BIT(1)")
    private boolean seeded = true;

    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinTable(name="sugar_role_privileges",
            joinColumns={@JoinColumn(name="role_id", referencedColumnName="id", nullable=false, updatable=false)},
            inverseJoinColumns={@JoinColumn(name="privilege_id", referencedColumnName="id", nullable=false, updatable=false)})
    private Set<Privilege> privileges;
}
