package com.sugarsaas.api.loader;

import com.sugarsaas.api.identity.privilege.Privilege;
import com.sugarsaas.api.identity.privilege.PrivilegeRepository;
import com.sugarsaas.api.identity.privilege.SeededPrivilege;
import com.sugarsaas.api.identity.role.Role;
import com.sugarsaas.api.identity.role.RoleRepository;
import com.sugarsaas.api.identity.role.SeededRole;
import com.sugarsaas.api.identity.role.SugarSeededRoles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Order(12)
@Component
public class SeededRolesLoader extends AbstractSeedDataLoader implements CommandLineRunner
{
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run (String ... args) {
        /*
         * Scan the classpath to find all implementations of SeededRole.
         *
         * Next, force static initializers in them to run which is
         * important as it allows implementations a chance to modify the
         * SeedRoles provided by Sugar (ex: adding/updating nested privileges).
         *
         * However, while loading we load SugarSeededRoles before all
         * other implementations to support dependencies on SugarSeededRoles.
         */
        Set<Class<? extends SeededRole>> seededRoleClasses = scanProviders(SeededRole.class);
        forceLoadAllSeededRoleClasses(seededRoleClasses);
        log.info("Processing {} ", SugarSeededRoles.class.getCanonicalName());
        loadSeededRoles(SugarSeededRoles.values());
        for (Class<? extends SeededRole> cls : seededRoleClasses) {
            if (!cls.getCanonicalName().equals(SugarSeededRoles.class.getCanonicalName())) {
                log.info("Processing {} ", cls.getCanonicalName());
                loadSeededRoles(cls.getEnumConstants());
            }
        }
    }

    private void forceLoadAllSeededRoleClasses(Set<Class<? extends SeededRole>> seededRoleClasses)  {
        for (Class<? extends SeededRole> cls : seededRoleClasses)   {
            cls.getEnumConstants();
        }
    }

    // TODO: Look at lambdas to simplify this
    private void loadSeededRoles(SeededRole[] seededRoles)  {
        Map<String, Role> currentRoles = roleRepository.findAll().stream().collect(toMap(r -> r.getName(), r -> r));
        for (SeededRole sr : seededRoles) {
            Role r = currentRoles.get(sr.getName());
            if (r == null)  {
                r = new Role();
                r.setName(sr.getName());
                r.setDescription(sr.getDescription());
                r.setSeeded(true);
                r.setPrivileges(mapSeededPrivileges2Privileges(sr.getPrivileges()));
                log.info("  Loading {} Seeded Role {} ", sr.getOrigin(), sr.getName());
                roleRepository.saveAndFlush(r);
            } else {
                boolean syncNeeded = false;
                if (!r.getDescription().equals(sr.getDescription()))  {
                    syncNeeded = true;
                }
                Set<String> rolePrivilegeNames = r.getPrivileges().stream().map(p -> p.getName()).collect(Collectors.toSet());
                // Cheap check
                if (rolePrivilegeNames.size() != sr.getPrivileges().size())  {
                    syncNeeded = true;
                } else {
                    // Compare each privilege
                    for (SeededPrivilege sp : sr.getPrivileges()) {
                        if (!rolePrivilegeNames.contains(sp.getName())) {
                            syncNeeded = true;
                            break;
                        }
                    }
                }
                if (syncNeeded) {
                    r.setDescription(sr.getDescription());
                    r.getPrivileges().clear();
                    r.setPrivileges(mapSeededPrivileges2Privileges(sr.getPrivileges()));
                    log.info("  Updating {} Seeded Role {} ", sr.getOrigin(), sr.getName());
                    roleRepository.saveAndFlush(r);
                }
            }
        }
    }

    private Set<Privilege> mapSeededPrivileges2Privileges(List<SeededPrivilege> seededPrivileges)  {
        Set<Privilege> privileges = new HashSet<>();
        for (SeededPrivilege sp : seededPrivileges)    {
            privileges.add(privilegeRepository.findByName(sp.getName()).get());
        }
        return privileges;
    }
}